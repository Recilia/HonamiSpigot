package rein.honami.spigot.world;

import rein.honami.spigot.config.HonamiConfig;

public class CaveConfig {

    public static boolean customCaveGenerationEnabled = false;
    public static int caveFrequency = 13;
    public static int caveMinSize = 1;
    public static int caveMaxSize = 5;
    public static double oreMultiplier = 1.0;

    public static void init() {
        customCaveGenerationEnabled = HonamiConfig.customCaveGenerationEnabled;
        caveFrequency = HonamiConfig.caveFrequency;
        caveMinSize = HonamiConfig.caveMinSize;
        caveMaxSize = HonamiConfig.caveMaxSize;
        oreMultiplier = HonamiConfig.oreMultiplier;
    }
}
