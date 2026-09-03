package rein.honami.spigot.taco;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Throwables;
import rein.honami.spigot.Honami;

public class HonamiTacoConfig {

	private static File CONFIG_FILE;
	private static final String HEADER = "This is the main configuration file for Honami's Taco features.\n"
			+ "As you can see, there's tons to configure. Some options may impact gameplay, so use\n"
			+ "with caution, and make sure you know what each option does before configuring.\n"
			+ "\n"
			+ "If you need help with the configuration or have any questions related to Honami,\n"
			+ "join us in our Discord.\n"
			+ "\n"
			+ "Discord: @ncros\n";
	
	static YamlConfiguration config;
	static int version;

	public static void init(File configFile) {
		CONFIG_FILE = configFile;

		if (!configFile.exists()) {
			File legacy = new File(configFile.getAbsoluteFile().getParentFile(), "taco.yml");
			if (legacy.exists()) {
				try {
					java.nio.file.Files.move(legacy.toPath(), configFile.toPath());
					Honami.LOGGER.info("Migrated the existing taco.yml to honami-taco.yml. If your start script uses --taco-settings, rename it to --honami-taco-settings.");
				} catch (IOException ex) {
					Honami.LOGGER.warn("Could not migrate taco.yml to honami-taco.yml: " + ex.getMessage());
				}
			}
		}

		config = new YamlConfiguration();
		try {
			Honami.LOGGER.info("Loading Honami taco config from " + configFile.getName());
			config.load(CONFIG_FILE);
		} catch (IOException ignored) {
		} catch (InvalidConfigurationException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not load honami-taco.yml, please correct your syntax errors", ex);
			throw Throwables.propagate(ex);
		}
		config.options().header(HEADER);
		config.options().copyDefaults(true);

		version = getInt("config-version", 1);
		set("config-version", 1);
		readConfig(HonamiTacoConfig.class, null);
	}

	static void readConfig(Class<?> clazz, Object instance) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (Modifier.isPrivate(method.getModifiers())) {
				if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
					try {
						method.setAccessible(true);
						method.invoke(instance);
					} catch (InvocationTargetException ex) {
						throw Throwables.propagate(ex.getCause());
					} catch (Exception ex) {
						Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
					}
				}
			}
		}

		try {
			config.save(CONFIG_FILE);
		} catch (IOException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
		}
	}

	private static void set(String path, Object val) {
		config.set(path, val);
	}

	private static boolean getBoolean(String path, boolean def) {
		config.addDefault(path, def);
		return config.getBoolean(path, config.getBoolean(path));
	}

	private static double getDouble(String path, double def) {
		config.addDefault(path, def);
		return config.getDouble(path, config.getDouble(path));
	}

	private static float getFloat(String path, float def) {
		config.addDefault(path, def);
		return config.getFloat(path, config.getFloat(path));
	}

	private static int getInt(String path, int def) {
		config.addDefault(path, def);
		return config.getInt(path, config.getInt(path));
	}

	private static <T> List getList(String path, T def) {
		config.addDefault(path, def);
		return config.getList(path, config.getList(path));
	}

	private static String getString(String path, String def) {
		config.addDefault(path, def);
		return config.getString(path, config.getString(path));
	}
}
