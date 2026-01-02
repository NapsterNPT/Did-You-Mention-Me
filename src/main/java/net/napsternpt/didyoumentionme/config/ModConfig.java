package net.napsternpt.didyoumentionme.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import net.minecraft.client.MinecraftClient;

@Config(name = "didyoumentionme")
public class ModConfig implements ConfigData {
    public boolean enable = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean onlyOnUnfocus = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public String[] namesList = {MinecraftClient.getInstance().getSession().getUsername()};

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int volume = 100;

    public String sound = "entity.experience_orb.pickup";
}