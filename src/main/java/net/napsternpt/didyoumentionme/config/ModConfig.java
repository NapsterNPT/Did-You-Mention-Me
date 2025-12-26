package net.napsternpt.didyoumentionme.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "didyoumentionme")
public class ModConfig implements ConfigData {
    public boolean enable = true;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
    public int volume = 100;

    public String sound = "entity.experience_orb.pickup";
}
