package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.Warning.WarningState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.AuthorNagException;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.PluginLogger;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.DataSourceConfig;
import com.avaje.ebean.config.ServerConfig;
import com.avaje.ebeaninternal.api.SpiEbeanServer;
import com.avaje.ebeaninternal.server.ddl.DdlGenerator;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;

public abstract class JavaPlugin extends PluginBase {
	private boolean isEnabled = false;
	private PluginLoader loader = null;
	private Server server = null;
	private File file = null;
	private PluginDescriptionFile description = null;
	private File dataFolder = null;
	private ClassLoader classLoader = null;
	private boolean naggable = true;
	private EbeanServer ebean = null;
	private FileConfiguration newConfig = null;
	private File configFile = null;
	private PluginLogger logger = null;

	public JavaPlugin() {
		final ClassLoader classLoader = this.getClass().getClassLoader();
		if (!(classLoader instanceof PluginClassLoader)) {
			throw new IllegalStateException("JavaPlugin requires " + PluginClassLoader.class.getName());
		}
		((PluginClassLoader) classLoader).initialize(this);
	}

	@Deprecated
	protected JavaPlugin(final PluginLoader loader, final Server server, final PluginDescriptionFile description,
			final File dataFolder, final File file) {
		final ClassLoader classLoader = this.getClass().getClassLoader();
		if (classLoader instanceof PluginClassLoader) {
			throw new IllegalStateException("Cannot use initialization constructor at runtime");
		}
		init(loader, server, description, dataFolder, file, classLoader);
	}

	protected JavaPlugin(final JavaPluginLoader loader, final PluginDescriptionFile description, final File dataFolder,
			final File file) {
		final ClassLoader classLoader = this.getClass().getClassLoader();
		if (classLoader instanceof PluginClassLoader) {
			throw new IllegalStateException("Cannot use initialization constructor at runtime");
		}
		init(loader, loader.server, description, dataFolder, file, classLoader);
	}

	@Override
	public final File getDataFolder() {
		return dataFolder;
	}

	@Override
	public final PluginLoader getPluginLoader() {
		return loader;
	}

	@Override
	public final Server getServer() {
		return server;
	}

	@Override
	public final boolean isEnabled() {
		return isEnabled;
	}

	protected File getFile() {
		return file;
	}

	@Override
	public final PluginDescriptionFile getDescription() {
		return description;
	}

	@Override
	public FileConfiguration getConfig() {
		if (newConfig == null) {
			reloadConfig();
		}
		return newConfig;
	}

	@SuppressWarnings("deprecation")
	protected final Reader getTextResource(String file) {
		final InputStream in = getResource(file);

		return in == null ? null
				: new InputStreamReader(in, isStrictlyUTF8() || FileConfiguration.UTF8_OVERRIDE ? Charsets.UTF_8
						: Charset.defaultCharset());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void reloadConfig() {
		newConfig = YamlConfiguration.loadConfiguration(configFile);

		final InputStream defConfigStream = getResource("config.yml");
		if (defConfigStream == null) {
			return;
		}

		final YamlConfiguration defConfig;
		if (isStrictlyUTF8() || FileConfiguration.UTF8_OVERRIDE) {
			defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8));
		} else {
			final byte[] contents;
			defConfig = new YamlConfiguration();
			try {
				contents = ByteStreams.toByteArray(defConfigStream);
			} catch (final IOException e) {
				getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml", e);
				return;
			}

			final String text = new String(contents, Charset.defaultCharset());
			if (!text.equals(new String(contents, Charsets.UTF_8))) {
				getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
			}

			try {
				defConfig.loadFromString(text);
			} catch (final InvalidConfigurationException e) {
				getLogger().log(Level.SEVERE, "Cannot load configuration from jar", e);
			}
		}

		newConfig.setDefaults(defConfig);
	}

	private boolean isStrictlyUTF8() {
		return getDescription().getAwareness().contains(PluginAwareness.Flags.UTF8);
	}

	@Override
	public void saveConfig() {
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Could not save config to " + configFile, ex);
		}
	}

	@Override
	public void saveDefaultConfig() {
		if (!configFile.exists()) {
			saveResource("config.yml", false);
		}
	}

	@Override
	public void saveResource(String resourcePath, boolean replace) {
		if (resourcePath == null || resourcePath.length() == 0) {
			throw new IllegalArgumentException("ResourcePath cannot be null or empty");
		}

		resourcePath = resourcePath.replace('\\', '/');
		InputStream in = getResource(resourcePath);
		if (in == null) {
			throw new IllegalArgumentException(
					"The embedded resource '" + resourcePath + "' cannot be found in " + file);
		}

		File outFile = new File(dataFolder, resourcePath);
		int lastIndex = resourcePath.lastIndexOf('/');
		File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

		if (!outDir.exists()) {
			outDir.mkdirs();
		}

		try {
			if (!outFile.exists() || replace) {
				OutputStream out = new FileOutputStream(outFile);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
			} else {
				logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because "
						+ outFile.getName() + " already exists.");
			}
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
		}
	}

	@Override
	public InputStream getResource(String filename) {
		if (filename == null) {
			throw new IllegalArgumentException("Filename cannot be null");
		}

		try {
			URL url = getClassLoader().getResource(filename);

			if (url == null) {
				return null;
			}

			URLConnection connection = url.openConnection();
			connection.setUseCaches(false);
			return connection.getInputStream();
		} catch (IOException ex) {
			return null;
		}
	}

	protected final ClassLoader getClassLoader() {
		return classLoader;
	}

	protected final void setEnabled(final boolean enabled) {
		if (isEnabled != enabled) {
			isEnabled = enabled;

			if (isEnabled) {
				onEnable();
			} else {
				onDisable();
			}
		}
	}

	@Deprecated
	protected final void initialize(PluginLoader loader, Server server, PluginDescriptionFile description,
			File dataFolder, File file, ClassLoader classLoader) {
		if (server.getWarningState() == WarningState.OFF) {
			return;
		}
		getLogger().log(Level.WARNING, getClass().getName() + " is already initialized",
				server.getWarningState() == WarningState.DEFAULT ? null
						: new AuthorNagException("Explicit initialization"));
	}

	final void init(PluginLoader loader, Server server, PluginDescriptionFile description, File dataFolder, File file,
			ClassLoader classLoader) {
		this.loader = loader;
		this.server = server;
		this.file = file;
		this.description = description;
		this.dataFolder = dataFolder;
		this.classLoader = classLoader;
		this.configFile = new File(dataFolder, "config.yml");
		this.logger = new PluginLogger(this);

		if (description.isDatabaseEnabled()) {
			ServerConfig db = new ServerConfig();

			db.setDefaultServer(false);
			db.setRegister(false);
			db.setClasses(getDatabaseClasses());
			db.setName(description.getName());
			server.configureDbConfig(db);

			DataSourceConfig ds = db.getDataSourceConfig();

			ds.setUrl(replaceDatabaseString(ds.getUrl()));
			dataFolder.mkdirs();

			ClassLoader previous = Thread.currentThread().getContextClassLoader();

			Thread.currentThread().setContextClassLoader(classLoader);
			ebean = EbeanServerFactory.create(db);
			Thread.currentThread().setContextClassLoader(previous);
		}
	}

	public List<Class<?>> getDatabaseClasses() {
		return new ArrayList<Class<?>>();
	}

	private String replaceDatabaseString(String input) {
		input = input.replaceAll("\\{DIR\\}", dataFolder.getPath().replaceAll("\\\\", "/") + "/");
		input = input.replaceAll("\\{NAME\\}", description.getName().replaceAll("[^\\w_-]", ""));
		return input;
	}

	@Deprecated
	public final boolean isInitialized() {
		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		return null;
	}

	public PluginCommand getCommand(String name) {
		String alias = name.toLowerCase();
		PluginCommand command = getServer().getPluginCommand(alias);

		if (command == null || command.getPlugin() != this) {
			command = getServer().getPluginCommand(description.getName().toLowerCase() + ":" + alias);
		}

		if (command != null && command.getPlugin() == this) {
			return command;
		} else {
			return null;
		}
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEnable() {
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
		return null;
	}

	@Override
	public final boolean isNaggable() {
		return naggable;
	}

	@Override
	public final void setNaggable(boolean canNag) {
		this.naggable = canNag;
	}

	@Override
	public EbeanServer getDatabase() {
		return ebean;
	}

	protected void installDDL() {
		SpiEbeanServer serv = (SpiEbeanServer) getDatabase();
		DdlGenerator gen = serv.getDdlGenerator();

		gen.runScript(false, gen.generateCreateDdl());
	}

	protected void removeDDL() {
		SpiEbeanServer serv = (SpiEbeanServer) getDatabase();
		DdlGenerator gen = serv.getDdlGenerator();

		gen.runScript(true, gen.generateDropDdl());
	}

	@Override
	public final Logger getLogger() {
		return logger;
	}

	@Override
	public String toString() {
		return description.getFullName();
	}

	public static <T extends JavaPlugin> T getPlugin(Class<T> clazz) {
		Validate.notNull(clazz, "Null class cannot have a plugin");
		if (!JavaPlugin.class.isAssignableFrom(clazz)) {
			throw new IllegalArgumentException(clazz + " does not extend " + JavaPlugin.class);
		}
		final ClassLoader cl = clazz.getClassLoader();
		if (!(cl instanceof PluginClassLoader)) {
			throw new IllegalArgumentException(clazz + " is not initialized by " + PluginClassLoader.class);
		}
		JavaPlugin plugin = ((PluginClassLoader) cl).plugin;
		if (plugin == null) {
			throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
		}
		return clazz.cast(plugin);
	}

	public static JavaPlugin getProvidingPlugin(Class<?> clazz) {
		Validate.notNull(clazz, "Null class cannot have a plugin");
		final ClassLoader cl = clazz.getClassLoader();
		if (!(cl instanceof PluginClassLoader)) {
			throw new IllegalArgumentException(clazz + " is not provided by " + PluginClassLoader.class);
		}
		JavaPlugin plugin = ((PluginClassLoader) cl).plugin;
		if (plugin == null) {
			throw new IllegalStateException("Cannot get plugin for " + clazz + " from a static initializer");
		}
		return plugin;
	}
}
