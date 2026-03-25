package net.napsternpt.didyoumentionme;

import net.fabricmc.api.ClientModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.napsternpt.didyoumentionme.config.ModConfig;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.Queue;

public class DidYouMentionMeClient implements ClientModInitializer {

    private final Queue<String> sentMessages = new LinkedList<>();
    private static final int MAX_TRACKED_MESSAGES = 5;

    private void checkAndPlaySound(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (!config.enable || client.player == null) return;

        if (config.onlyOnUnfocus && client.isWindowFocused()) return;

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
            if (messageLower.contains(name.toLowerCase())) {

                SoundEvent soundEvent;
                if (config.sound.contains(":")) soundEvent = Registries.SOUND_EVENT.get(Identifier.of(config.sound));
                else soundEvent = Registries.SOUND_EVENT.get(Identifier.of("minecraft", config.sound));
                if (soundEvent == null) return;

                float volume = config.volume / 100f;
                client.getSoundManager().play(
                        new PositionedSoundInstance(soundEvent.id(), SoundCategory.MASTER, volume, 1.0f,
                                SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.NONE, 0.0, 0.0, 0.0, true
                        )
                );
                return;
            }
        }
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        ClientSendMessageEvents.CHAT.register((message) -> {
            synchronized (sentMessages) {
                sentMessages.add(message);
                while (sentMessages.size() > MAX_TRACKED_MESSAGES) {
                    sentMessages.poll();
                }
            }
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!overlay) {
                checkAndPlaySound(message);
            }
        });
    }
}