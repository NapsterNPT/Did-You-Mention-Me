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

public class DidYouMentionMeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ClientReceiveMessageEvents.CHAT.register(
                (message, signedMessage, sender, params, receptionTimestamp) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null || client.isWindowFocused()) return;

                    String playerName = client.player.getName().getString();
                    if (message.getString().toLowerCase().contains(playerName.toLowerCase())) {

                        boolean enable = config.enable;

                        if (enable) {
                            String mentionSound = "minecraft:" + config.sound;
                            SoundEvent soundEvent = Registries.SOUND_EVENT.get(
                                    Identifier.of(mentionSound)
                            );

                            float volume = config.volume / 100f;
                            System.out.println(volume);

                            client.getSoundManager().play(PositionedSoundInstance.master(soundEvent, 1.0f, volume));

                        }
                    }
                }
        );
    }
}
