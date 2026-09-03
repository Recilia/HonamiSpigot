package rein.honami.spigot.knockback;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import rein.honami.spigot.yaml.YamlCommenter;

import com.google.common.base.Throwables;
import rein.honami.spigot.Honami;

import rein.honami.spigot.nacho.knockback.KnockbackProfile;

public class KnockbackConfig {
	private static final Logger LOGGER = LogManager.getLogger(KnockbackConfig.class);
	private static File CONFIG_FILE;
	protected static final YamlCommenter c = new YamlCommenter();
	private static final String HEADER = "This is the knockback configuration file for Honami.\n"
			+ "For configuration info see https://github.com/Recilia";
	static YamlConfiguration config;

	private static volatile KnockbackProfile currentKb;
	private static volatile Set<KnockbackProfile> kbProfiles = new HashSet<>();

	public static void init(File configFile) {
		CONFIG_FILE = configFile;
		config = new YamlConfiguration();
		try {
			Honami.LOGGER.info("Loading Honami knockback config from " + configFile.getName());
			config.load(CONFIG_FILE);
		} catch (IOException ignored) {
		} catch (InvalidConfigurationException ex) {
			LOGGER.log(Level.ERROR, "Could not load knockback.yml, please correct your syntax errors", ex);
			throw Throwables.propagate(ex);
		}
		config.options().copyDefaults(true);
		c.setHeader(HEADER);

		Set<String> keys = getKeys("knockback.profiles");
		
		if (!keys.contains("vanilla")) {
			final KnockbackProfile vanillaProfile = new CraftKnockbackProfile("vanilla");
			vanillaProfile.save(true);
		}
		
		if (!keys.contains("honami")) {
			
			final KnockbackProfile honamiProfile = new CraftKnockbackProfile("honami");
			
			honamiProfile.setHorizontal(0.35);
			honamiProfile.setRodHorizontal(0.425);
			honamiProfile.setArrowHorizontal(0.425);
			honamiProfile.setPearlHorizontal(0.35);
			honamiProfile.setSnowballHorizontal(0.425);
			honamiProfile.setEggHorizontal(0.425);
			honamiProfile.setExtraHorizontal(0.425);
			honamiProfile.setWTapExtraHorizontal(0.425);

			honamiProfile.setVertical(0.35);
			honamiProfile.setRodVertical(0.425);
			honamiProfile.setArrowVertical(0.425);
			honamiProfile.setPearlVertical(0.35);
			honamiProfile.setSnowballVertical(0.425);
			honamiProfile.setEggVertical(0.425);
			honamiProfile.setExtraVertical(0.085);
			honamiProfile.setWTapExtraVertical(0.085);
			
			honamiProfile.save(true);
		}
		
		if (!keys.contains("hypixel")) {

			final KnockbackProfile hypixelProfile = new CraftKnockbackProfile("hypixel");
			
			hypixelProfile.setVertical(0.36);
			hypixelProfile.setVerticalMax(0.43075);
			
			hypixelProfile.save(true);
		}
		
		if (!keys.contains("kohi")) {
			final KnockbackProfile defaultProfile = new CraftKnockbackProfile("kohi"); 

			defaultProfile.setHorizontal(0.35);
			defaultProfile.setRodHorizontal(0.35);
			defaultProfile.setArrowHorizontal(0.35);
			defaultProfile.setPearlHorizontal(0.35);
			defaultProfile.setSnowballHorizontal(0.35);
			defaultProfile.setEggHorizontal(0.35);
			defaultProfile.setExtraHorizontal(0.425);
			defaultProfile.setWTapExtraHorizontal(0.425);

			defaultProfile.setVertical(0.35);
			defaultProfile.setRodVertical(0.35);
			defaultProfile.setArrowVertical(0.35);
			defaultProfile.setPearlVertical(0.35);
			defaultProfile.setSnowballVertical(0.35);
			defaultProfile.setEggVertical(0.35);
			defaultProfile.setExtraVertical(0.085);
			defaultProfile.setWTapExtraVertical(0.085);

			defaultProfile.save(true);
		}
		
		if (!keys.contains("easy")) {
			final KnockbackProfile easyProfile = new CraftKnockbackProfile("easy");
			easyProfile.setHorizontal(0.35);
			easyProfile.setRodHorizontal(0.4);
			easyProfile.setArrowHorizontal(0.4);
			easyProfile.setPearlHorizontal(0.35);
			easyProfile.setSnowballHorizontal(0.4);
			easyProfile.setEggHorizontal(0.4);
			easyProfile.setExtraHorizontal(0.4);
			easyProfile.setWTapExtraHorizontal(0.4);
			easyProfile.setVertical(0.35);
			easyProfile.setRodVertical(0.4);
			easyProfile.setArrowVertical(0.4);
			easyProfile.setPearlVertical(0.35);
			easyProfile.setSnowballVertical(0.4);
			easyProfile.setEggVertical(0.4);
			easyProfile.setExtraVertical(0.08);
			easyProfile.setWTapExtraVertical(0.08);
			easyProfile.save(true);
		}
		
		if (!keys.contains("smooth")) {
			final KnockbackProfile smoothProfile = new CraftKnockbackProfile("smooth");
			smoothProfile.setHorizontal(0.4);
			smoothProfile.setRodHorizontal(0.45);
			smoothProfile.setArrowHorizontal(0.45);
			smoothProfile.setPearlHorizontal(0.4);
			smoothProfile.setSnowballHorizontal(0.45);
			smoothProfile.setEggHorizontal(0.45);
			smoothProfile.setExtraHorizontal(0.45);
			smoothProfile.setWTapExtraHorizontal(0.45);
			smoothProfile.setVertical(0.38);
			smoothProfile.setRodVertical(0.42);
			smoothProfile.setArrowVertical(0.42);
			smoothProfile.setPearlVertical(0.38);
			smoothProfile.setSnowballVertical(0.42);
			smoothProfile.setEggVertical(0.42);
			smoothProfile.setExtraVertical(0.09);
			smoothProfile.setWTapExtraVertical(0.09);
			smoothProfile.save(true);
		}
		
		if (!keys.contains("detailed")) {
			final KnockbackProfile detailedProfile = new CraftKnockbackProfile("detailed");
			detailedProfile.setHorizontal(0.42);
			detailedProfile.setRodHorizontal(0.48);
			detailedProfile.setArrowHorizontal(0.48);
			detailedProfile.setPearlHorizontal(0.42);
			detailedProfile.setSnowballHorizontal(0.48);
			detailedProfile.setEggHorizontal(0.48);
			detailedProfile.setExtraHorizontal(0.48);
			detailedProfile.setWTapExtraHorizontal(0.48);
			detailedProfile.setVertical(0.4);
			detailedProfile.setRodVertical(0.45);
			detailedProfile.setArrowVertical(0.45);
			detailedProfile.setPearlVertical(0.4);
			detailedProfile.setSnowballVertical(0.45);
			detailedProfile.setEggVertical(0.45);
			detailedProfile.setExtraVertical(0.1);
			detailedProfile.setWTapExtraVertical(0.1);
			detailedProfile.save(true);
		}
		
		if (!keys.contains("exclusive")) {
			final KnockbackProfile exclusiveProfile = new CraftKnockbackProfile("exclusive");
			exclusiveProfile.setHorizontal(0.45);
			exclusiveProfile.setRodHorizontal(0.5);
			exclusiveProfile.setArrowHorizontal(0.5);
			exclusiveProfile.setPearlHorizontal(0.45);
			exclusiveProfile.setSnowballHorizontal(0.5);
			exclusiveProfile.setEggHorizontal(0.5);
			exclusiveProfile.setExtraHorizontal(0.5);
			exclusiveProfile.setWTapExtraHorizontal(0.5);
			exclusiveProfile.setVertical(0.42);
			exclusiveProfile.setRodVertical(0.48);
			exclusiveProfile.setArrowVertical(0.48);
			exclusiveProfile.setPearlVertical(0.42);
			exclusiveProfile.setSnowballVertical(0.48);
			exclusiveProfile.setEggVertical(0.48);
			exclusiveProfile.setExtraVertical(0.11);
			exclusiveProfile.setWTapExtraVertical(0.11);
			exclusiveProfile.save(true);
		}
		
		if (!keys.contains("expert")) {
			final KnockbackProfile expertProfile = new CraftKnockbackProfile("expert");
			expertProfile.setHorizontal(0.48);
			expertProfile.setRodHorizontal(0.55);
			expertProfile.setArrowHorizontal(0.55);
			expertProfile.setPearlHorizontal(0.48);
			expertProfile.setSnowballHorizontal(0.55);
			expertProfile.setEggHorizontal(0.55);
			expertProfile.setExtraHorizontal(0.55);
			expertProfile.setWTapExtraHorizontal(0.55);
			expertProfile.setVertical(0.45);
			expertProfile.setRodVertical(0.5);
			expertProfile.setArrowVertical(0.5);
			expertProfile.setPearlVertical(0.45);
			expertProfile.setSnowballVertical(0.5);
			expertProfile.setEggVertical(0.5);
			expertProfile.setExtraVertical(0.12);
			expertProfile.setWTapExtraVertical(0.12);
			expertProfile.save(true);
		}
		
		if (!keys.contains("explicit")) {
			final KnockbackProfile explicitProfile = new CraftKnockbackProfile("explicit");
			explicitProfile.setHorizontal(0.5);
			explicitProfile.setRodHorizontal(0.6);
			explicitProfile.setArrowHorizontal(0.6);
			explicitProfile.setPearlHorizontal(0.5);
			explicitProfile.setSnowballHorizontal(0.6);
			explicitProfile.setEggHorizontal(0.6);
			explicitProfile.setExtraHorizontal(0.6);
			explicitProfile.setWTapExtraHorizontal(0.6);
			explicitProfile.setVertical(0.48);
			explicitProfile.setRodVertical(0.55);
			explicitProfile.setArrowVertical(0.55);
			explicitProfile.setPearlVertical(0.48);
			explicitProfile.setSnowballVertical(0.55);
			explicitProfile.setEggVertical(0.55);
			explicitProfile.setExtraVertical(0.13);
			explicitProfile.setWTapExtraVertical(0.13);
			explicitProfile.save(true);
		}

		keys = getKeys("knockback.profiles");
		
		for (String key : keys) {
			final String path = "knockback.profiles." + key;
			CraftKnockbackProfile profile = (CraftKnockbackProfile) getKbProfileByName(key);
			if (profile == null) {
				profile = new CraftKnockbackProfile(key);
				kbProfiles.add(profile);
			}
			profile.setStopSprint(getBoolean(path + ".stop-sprint", true));
			profile.setFrictionHorizontal(getDouble(path + ".friction-horizontal", 2.0D));
			profile.setFrictionVertical(getDouble(path + ".friction-vertical", 2.0D));
			profile.setHorizontal(getDouble(path + ".horizontal", 0.4D));
			profile.setVertical(getDouble(path + ".vertical", 0.4D));
			profile.setVerticalMax(getDouble(path + ".vertical-max", 0.4D));
			profile.setVerticalMin(getDouble(path + ".vertical-min", -1.0D));
			profile.setExtraHorizontal(getDouble(path + ".extra-horizontal", 0.5D));
			profile.setExtraVertical(getDouble(path + ".extra-vertical", 0.1D));

			profile.setWTapExtraHorizontal(getDouble(path + ".wtap-extra-horizontal", 0.5));
			profile.setWTapExtraVertical(getDouble(path + ".wtap-extra-vertical", 0.1));
			
			profile.setAddHorizontal(getDouble(path + ".add-horizontal", 0));
			profile.setAddVertical(getDouble(path + ".add-vertical", 0));

			profile.setRodHorizontal(getDouble(path + ".projectiles.rod.horizontal", 0.4D));
			profile.setRodVertical(getDouble(path + ".projectiles.rod.vertical", 0.4D));
			profile.setArrowHorizontal(getDouble(path + ".projectiles.arrow.horizontal", 0.4D));
			profile.setArrowVertical(getDouble(path + ".projectiles.arrow.vertical", 0.4D));
			profile.setPearlHorizontal(getDouble(path + ".projectiles.pearl.horizontal", 0.4D));
			profile.setPearlVertical(getDouble(path + ".projectiles.pearl.vertical", 0.4D));
			profile.setSnowballHorizontal(getDouble(path + ".projectiles.snowball.horizontal", 0.4D));
			profile.setSnowballVertical(getDouble(path + ".projectiles.snowball.vertical", 0.4D));
			profile.setEggHorizontal(getDouble(path + ".projectiles.egg.horizontal", 0.4D));
			profile.setEggVertical(getDouble(path + ".projectiles.egg.vertical", 0.4D));
		}
		currentKb = getKbProfileByName(getString("knockback.current", "kohi"));
		if (currentKb == null) {
			Honami.LOGGER.warn("Knockback profile selected was not found, using profile 'kohi' for now!");
			currentKb = getKbProfileByName("kohi");
			
			Honami.LOGGER.info("Setting default knockback as 'kohi'...");
			set("knockback.current", "kohi");
		}
		save();
	}

	public static KnockbackProfile getCurrentKb() {
		if (currentKb == null) {
			setCurrentKb(getKbProfileByName("vanilla"));
		}
		return currentKb;
	}

	public static void setCurrentKb(KnockbackProfile kb) {
		currentKb = kb;
	}

	public static KnockbackProfile getKbProfileByName(String name) {
		for (KnockbackProfile profile : kbProfiles) {
			if (profile.getName().equalsIgnoreCase(name)) {
				return profile;
			}
		}
		return null;
	}

	public static Set<KnockbackProfile> getKbProfiles() {
		return kbProfiles;
	}

	public static void save() {
		try {
			config.save(CONFIG_FILE);
		} catch (IOException ex) {
			LOGGER.log(Level.ERROR, "Could not save " + CONFIG_FILE, ex);
		}
	}

	public static void set(String path, Object val) {
		config.set(path, val);

		save();
	}

	public static Set<String> getKeys(String path) {
		if (!config.isConfigurationSection(path)) {
			config.createSection(path);
			return new HashSet<>();
		}

		return config.getConfigurationSection(path).getKeys(false);
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
