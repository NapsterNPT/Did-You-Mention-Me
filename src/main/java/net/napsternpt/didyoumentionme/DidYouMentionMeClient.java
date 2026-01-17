package net.napsternpt.didyoumentionme;

import net.fabricmc.api.ClientModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.napsternpt.didyoumentionme.config.ModConfig;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.text.Text;

public class DidYouMentionMeClient implements ClientModInitializer {

    private void checkAndPlaySound(Text message) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (!config.enable || client.player == null) return;

        if (config.onlyOnUnfocus && client.isWindowFocused()) return;

        String messageText = message.getString().toLowerCase();
        String playerName = client.player.getName().getString().toLowerCase();

        if (messageText.contains(playerName) && messageText.startsWith("<" + playerName)) {
            return;
        }

        for (String name : config.namesList) {
            if (messageText.contains(name.toLowerCase())) {
                String mentionSound = "minecraft:" + config.sound;
                SoundEvent soundEvent = Registries.SOUND_EVENT.get(
                        Identifier.of(mentionSound)
                );

                float volume = config.volume / 100f;
                client.getSoundManager().play(
                        PositionedSoundInstance.master(soundEvent, 1.0f, volume)
                );

                System.out.println("Mention detected: " + messageText);
                return;
            }
        }
    }

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        ClientReceiveMessageEvents.CHAT.register(
                (message, signedMessage, sender, params, receptionTimestamp) -> {
                    checkAndPlaySound(message);
                }
        );

        ClientReceiveMessageEvents.GAME.register(
                (message, overlay) -> {
                    if (!overlay) {
                        checkAndPlaySound(message);
                    }
                }
        );
    }
}