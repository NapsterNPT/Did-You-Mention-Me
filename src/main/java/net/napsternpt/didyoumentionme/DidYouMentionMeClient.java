package net.napsternpt.didyoumentionme;

import net.fabricmc.api.ClientModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.napsternpt.didyoumentionme.config.ModConfig;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;


public class DidYouMentionMeClient implements ClientModInitializer {

    private final Queue<String> sentMessages = new LinkedList<>();
    private static final int MAX_TRACKED_MESSAGES = 5;

    private void checkAndPlaySound(Component message) {
        Minecraft client = Minecraft.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (!config.enable || client.player == null) return;
        if (config.onlyOnUnfocus && client.isWindowActive()) return;

        String messageText = message.getString();
        String messageLower = messageText.toLowerCase();

        synchronized (sentMessages) {
            for (String sent : sentMessages) {
                if (messageLower.contains(sent.toLowerCase())) {
                    sentMessages.remove(sent);
                    return;
                }
            }
        }

        for (String name : config.namesList) {
            if (!messageLower.contains(name.toLowerCase())) continue;

            Identifier soundId = config.sound.contains(":")
                    ? Identifier.parse(config.sound)
                    : Identifier.fromNamespaceAndPath("minecraft", config.sound);

            Optional<SoundEvent> soundOpt = BuiltInRegistries.SOUND_EVENT.getOptional(soundId);
            if (soundOpt.isEmpty()) return;

            SoundEvent soundEvent = soundOpt.get();
            float volume = config.volume / 100f;

            client.getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, 1.0f, volume));
            return;
        }
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        ClientSendMessageEvents.CHAT.register((message) -> {
            synchronized (sentMessages) {
                sentMessages.add(message);
                while (sentMessages.size() > MAX_TRACKED_MESSAGES) sentMessages.poll();
            }
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!overlay) checkAndPlaySound(message);
        });
    }
}