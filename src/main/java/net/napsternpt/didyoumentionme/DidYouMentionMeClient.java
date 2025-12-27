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

                    boolean enable = config.enable;

                    if (config.onlyOnUnfocus) {
                        if (client.player == null || client.isWindowFocused() || !enable) return;
                    } else {
                        if (client.player == null || !enable) return;
                    }

                    for (int i = 0; i < config.namesList.length - 1; i++) {
                        String name = config.namesList[i];
                        if (message.getString().toLowerCase().contains(name.toLowerCase())) {

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
