package net.napsternpt.didyoumentionme.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.client.Minecraft;

@Config(name = "didyoumentionme")
public class ModConfig implements ConfigData {
    public boolean enable = true;

    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean onlyOnUnfocus = true;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public String[] namesList = {Minecraft.getInstance().getUser().getName()};

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int volume = 100;

    public String sound = "entity.experience_orb.pickup";
}