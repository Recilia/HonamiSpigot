package org.bukkit.plugin;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.ChunkGenerator;

import com.avaje.ebean.EbeanServer;

public interface Plugin extends TabExecutor {

	public File getDataFolder();

	public PluginDescriptionFile getDescription();

	public FileConfiguration getConfig();

	public InputStream getResource(String filename);

	public void saveConfig();

	public void saveDefaultConfig();

	public void saveResource(String resourcePath, boolean replace);

	public void reloadConfig();

	public PluginLoader getPluginLoader();

	public Server getServer();

	public boolean isEnabled();

	public void onDisable();

	public void onLoad();

	public void onEnable();

	public boolean isNaggable();

	public void setNaggable(boolean canNag);

	public EbeanServer getDatabase();

	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id);

	public Logger getLogger();

	public String getName();
}
