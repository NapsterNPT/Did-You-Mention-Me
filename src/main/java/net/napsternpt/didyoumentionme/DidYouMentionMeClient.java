package net.napsternpt.didyoumentionme;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class DidYouMentionMeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientReceiveMessageEvents.CHAT.register(
                (message, signedMessage, sender, params, receptionTimestamp) -> {

                    MinecraftClient client = MinecraftClient.getInstance();

                    // Player exist
                    if (client.player == null) return;

                    // Focus or not
                    if (client.isWindowFocused()) return;

                    // Message whit player name
                    String playerName = client.player.getName().getString();
                    String msg = message.getString();
                    if (msg.toLowerCase().contains(playerName.toLowerCase())) {
                        // Sound
                        client.getSoundManager().play(
                                PositionedSoundInstance.master(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f)
                        );
                    }
                }
        );
    }
}
