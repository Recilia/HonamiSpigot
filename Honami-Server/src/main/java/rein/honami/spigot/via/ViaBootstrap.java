package rein.honami.spigot.via;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import rein.honami.spigot.Honami;
import rein.honami.spigot.config.HonamiConfig;

public final class ViaBootstrap {

    private static final String[] BUNDLED_JARS = { "ViaVersion-5.11.0.jar", "ViaRewind-4.1.3.jar" };

    private ViaBootstrap() {
    }

    public static void extract(File pluginsFolder) {
        if (!HonamiConfig.viaVersionEnabled) {
            Honami.LOGGER.info("Bundled ViaVersion & ViaRewind are disabled (settings.via-version.enabled = false)");
            return;
        }

        if (!pluginsFolder.exists() && !pluginsFolder.mkdirs()) {
            Honami.LOGGER.warn("Could not create plugins folder at " + pluginsFolder
                    + ", skipping bundled ViaVersion & ViaRewind extraction");
            return;
        }

        for (String jar : BUNDLED_JARS) {
            File target = new File(pluginsFolder, jar);
            if (target.exists()) {
                continue; 
            }
            extractJar(jar, target);
        }
    }

    private static void extractJar(String jar, File target) {
        try (InputStream in = ViaBootstrap.class.getResourceAsStream("/via/" + jar)) {
            if (in == null) {
                Honami.LOGGER.warn("Bundled resource /via/" + jar + " was not found inside the server jar");
                return;
            }
            try (FileOutputStream out = new FileOutputStream(target)) {
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }
            Honami.LOGGER.info("Extracted bundled " + jar + " into the plugins folder (delete it to restore on next start)");
        } catch (IOException ex) {
            Honami.LOGGER.warn("Could not extract bundled " + jar + ": " + ex.getMessage());
        }
    }
}
