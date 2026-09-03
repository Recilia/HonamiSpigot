package rein.honami.spigot.nacho.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Throwables;

import rein.honami.spigot.config.HonamiConfig;

public class NachoWorldConfig {

	private static final Logger LOGGER = LogManager.getLogger(NachoWorldConfig.class);
	private final String worldName;
	private final YamlConfiguration config;

	public NachoWorldConfig(String worldName) {
		this.worldName = worldName;
		this.config = HonamiConfig.config;
		init();
	}

	public void init() {
		NachoWorldConfig.readConfig(NachoWorldConfig.class, this);
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
						LOGGER.log(org.apache.logging.log4j.Level.ERROR, "Error invoking " + method, ex);
					}
				}
			}
		}
	}

	private void set(String path, Object val) {
		config.set("world-settings.default." + path, val);
	}

	private boolean getBoolean(String path, boolean def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getBoolean("world-settings." + worldName + "." + path,
				config.getBoolean("world-settings.default." + path));
	}

	private double getDouble(String path, double def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getDouble("world-settings." + worldName + "." + path,
				config.getDouble("world-settings.default." + path));
	}

	private int getInt(String path, int def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getInt("world-settings." + worldName + "." + path,
				config.getInt("world-settings.default." + path));
	}

	private float getFloat(String path, float def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getFloat("world-settings." + worldName + "." + path,
				config.getFloat("world-settings.default." + path));
	}

	private <T> List getList(String path, T def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getList("world-settings." + worldName + "." + path,
				config.getList("world-settings.default." + path));
	}

	private String getString(String path, String def) {
		config.addDefault("world-settings.default." + path, def);
		return config.getString("world-settings." + worldName + "." + path,
				config.getString("world-settings.default." + path));
	}

	public boolean disableSpongeAbsorption;

	private void disableSpongeAbsorption() {
		disableSpongeAbsorption = getBoolean("disable-sponge-absorption", false);
	}

	public boolean doChunkUnload;

	private void doChunkUnload() {
		doChunkUnload = getBoolean("unload-chunks", true);
	}

	public boolean doBlocksOperations;

	private void doBlocksOperations() {
		doBlocksOperations = getBoolean("block-operations", true);
	}

	public boolean disablePhysicsPlace;
	public boolean disablePhysicsUpdate;

	private void physics() {
		disablePhysicsPlace = getBoolean("physics.disable-place", false);
		disablePhysicsUpdate = getBoolean("settings.physics.disable-update", false);
	}

	public boolean enableLavaToCobblestone;

	private void setEnableLavaToCobblestone() {
		enableLavaToCobblestone = getBoolean("enable-lava-to-cobblestone", true);
	}

	public boolean enableMobAI;
	public boolean enableMobSound;
	public boolean enableEntityActivation;
	public boolean endermiteSpawning;

	private void entity() {
		enableMobAI = getBoolean("entity.mob-ai", true);
		enableMobSound = getBoolean("entity.mob-sound", true);
		enableEntityActivation = getBoolean("entity.entity-activation", true);
		endermiteSpawning = getBoolean("entity.endermite-spawning", true);
	}

	public boolean infiniteWaterSources;

	private void infiniteWaterSources() {
		infiniteWaterSources = getBoolean("infinite-water-sources", true);
	}

	public boolean constantExplosions;
	public boolean reducedDensityRays;

	private void explosions() {
		constantExplosions = getBoolean("explosions.constant-radius", false);
		reducedDensityRays = getBoolean("explosions.reduced-density-rays", true);
	}

	public boolean shouldTickEnchantmentTables;

	private void shouldTickEnchantmentTables() {
		shouldTickEnchantmentTables = getBoolean("tick-enchantment-tables", true);
	}
}
