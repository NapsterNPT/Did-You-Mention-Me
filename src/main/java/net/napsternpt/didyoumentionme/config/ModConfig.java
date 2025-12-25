package net.napsternpt.didyoumentionme.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "didyoumentionme")
public class ModConfig implements ConfigData {

    public boolean toggleA = true;
    public boolean toggleB = false;

    @ConfigEntry.Gui.CollapsibleObject
    public InnerStuff stuff = new InnerStuff();

    public static class InnerStuff {
        public int a = 0;
        public int b = 1;
    }
}
