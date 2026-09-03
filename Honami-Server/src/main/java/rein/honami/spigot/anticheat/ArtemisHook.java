package rein.honami.spigot.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rein.honami.spigot.config.HonamiConfig;

public class ArtemisHook {

    private static final Logger LOGGER = LogManager.getLogger(ArtemisHook.class);
    public static boolean enabled = false;
    private static boolean artemisLoaded = false;

    public static void init() {
        if (!HonamiConfig.artemisEnabled) {
            return;
        }
        enabled = true;

        Plugin plugin = Bukkit.getPluginManager().getPlugin("Artemis");
        if (plugin != null) {
            artemisLoaded = true;
            LOGGER.info("[Honami] Artemis anticheat detected and hooked successfully.");
        } else {
            artemisLoaded = false;
            LOGGER.warn("[Honami] Artemis anticheat not found. SDK integration enabled but Artemis is not loaded.");
        }
    }

    public static boolean isArtemisLoaded() {
        return artemisLoaded;
    }
}
