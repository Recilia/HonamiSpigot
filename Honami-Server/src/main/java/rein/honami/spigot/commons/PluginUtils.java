package rein.honami.spigot.commons;
import org.bukkit.plugin.Plugin;

public class PluginUtils {
    public static int getCitizensBuild(Plugin plugin) {
        try {
            return Integer.parseInt(plugin.getDescription().getVersion().split("\\(build ")[1].replace(")", ""));
        } catch (Throwable ignored) {
            return 2396;
        }
    }
}