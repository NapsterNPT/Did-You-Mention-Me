package net.napsternpt.didyoumentionme;

import net.fabricmc.api.ClientModInitializer;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.napsternpt.didyoumentionme.config.ModConfig;

public class DidYouMentionMeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        ClientReceiveMessageEvents.CHAT.register(
                (message, signedMessage, sender, params, receptionTimestamp) -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client.player == null || client.isWindowFocused()) return;

                    String playerName = client.player.getName().getString();
                    if (message.getString().toLowerCase().contains(playerName.toLowerCase())) {
                        client.getSoundManager().play(
                                PositionedSoundInstance.master(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f)
                        );
                    }
                }
        );
    }
}
