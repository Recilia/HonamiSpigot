package org.bukkit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Warning.WarningState;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.map.MapView;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import com.avaje.ebean.config.ServerConfig;
import com.google.common.collect.ImmutableList;

public final class Bukkit {
	private static Server server;

	private Bukkit() {
	}

	public static Server getServer() {
		return server;
	}

	public static void setServer(Server server) {
		if (Bukkit.server != null) {
			throw new UnsupportedOperationException("Cannot redefine singleton Server");
		}

		Bukkit.server = server;
		server.getLogger().info("This server is running " + getName() + " version " + getVersion()
				+ " (Implementing API version " + getBukkitVersion() + ")");
	}

	public static String getName() {
		return server.getName();
	}

	public static String getVersion() {
		return server.getVersion();
	}

	public static String getBukkitVersion() {
		return server.getBukkitVersion();
	}

	@Deprecated
	public static Player[] _INVALID_getOnlinePlayers() {
		return server._INVALID_getOnlinePlayers();
	}

	public static Collection<? extends Player> getOnlinePlayers() {
		return server.getOnlinePlayers();
	}

	public static int getMaxPlayers() {
		return server.getMaxPlayers();
	}

	public static void setMaxPlayers(int maxPlayers) {
		server.setMaxPlayers(maxPlayers);
	}

	public static int getPort() {
		return server.getPort();
	}

	public static int getViewDistance() {
		return server.getViewDistance();
	}

	public static String getIp() {
		return server.getIp();
	}

	public static String getServerName() {
		return server.getServerName();
	}

	public static String getServerId() {
		return server.getServerId();
	}

	public static String getWorldType() {
		return server.getWorldType();
	}

	public static boolean getGenerateStructures() {
		return server.getGenerateStructures();
	}

	public static boolean getAllowEnd() {
		return server.getAllowEnd();
	}

	public static boolean getAllowNether() {
		return server.getAllowNether();
	}

	public static boolean hasWhitelist() {
		return server.hasWhitelist();
	}

	public static void setWhitelist(boolean value) {
		server.setWhitelist(value);
	}

	public static Set<OfflinePlayer> getWhitelistedPlayers() {
		return server.getWhitelistedPlayers();
	}

	public static void reloadWhitelist() {
		server.reloadWhitelist();
	}

	public static int broadcastMessage(String message) {
		return server.broadcastMessage(message);
	}

	

	public static void broadcast(net.md_5.bungee.api.chat.BaseComponent component) {
		server.broadcast(component);
	}

	public static void broadcast(net.md_5.bungee.api.chat.BaseComponent... components) {
		server.broadcast(components);
	}

	

	public static String getUpdateFolder() {
		return server.getUpdateFolder();
	}

	public static File getUpdateFolderFile() {
		return server.getUpdateFolderFile();
	}

	public static long getConnectionThrottle() {
		return server.getConnectionThrottle();
	}

	public static int getTicksPerAnimalSpawns() {
		return server.getTicksPerAnimalSpawns();
	}

	public static int getTicksPerMonsterSpawns() {
		return server.getTicksPerMonsterSpawns();
	}

	public static Player getPlayer(String name) {
		return server.getPlayer(name);
	}

	public static Player getPlayerExact(String name) {
		return server.getPlayerExact(name);
	}

	public static List<Player> matchPlayer(String name) {
		return server.matchPlayer(name);
	}

	public static Player getPlayer(UUID id) {
		return server.getPlayer(id);
	}

    public static Entity getEntity(UUID uuid) {
        return server.getEntity(uuid);
    }

	public static PluginManager getPluginManager() {
		return server.getPluginManager();
	}

	public static BukkitScheduler getScheduler() {
		return server.getScheduler();
	}

	public static ServicesManager getServicesManager() {
		return server.getServicesManager();
	}

	public static List<World> getWorlds() {
		return server.getWorlds();
	}

	public static World createWorld(WorldCreator creator) {
		return server.createWorld(creator);
	}

	public static boolean unloadWorld(String name, boolean save) {
		return server.unloadWorld(name, save);
	}

	public static boolean unloadWorld(World world, boolean save) {
		return server.unloadWorld(world, save);
	}

	public static World getWorld(String name) {
		return server.getWorld(name);
	}

	public static World getWorld(UUID uid) {
		return server.getWorld(uid);
	}

	@Deprecated
	public static MapView getMap(short id) {
		return server.getMap(id);
	}

	public static MapView createMap(World world) {
		return server.createMap(world);
	}

	public static void reload() {
		server.reload();
	}

	public static Logger getLogger() {
		return server.getLogger();
	}

	public static PluginCommand getPluginCommand(String name) {
		return server.getPluginCommand(name);
	}

	public static void savePlayers() {
		server.savePlayers();
	}

	public static boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException {
		return server.dispatchCommand(sender, commandLine);
	}

	public static void configureDbConfig(ServerConfig config) {
		server.configureDbConfig(config);
	}

	public static boolean addRecipe(Recipe recipe) {
		return server.addRecipe(recipe);
	}

	public static List<Recipe> getRecipesFor(ItemStack result) {
		return server.getRecipesFor(result);
	}

	public static Iterator<Recipe> recipeIterator() {
		return server.recipeIterator();
	}

	public static void clearRecipes() {
		server.clearRecipes();
	}

	public static void resetRecipes() {
		server.resetRecipes();
	}

	public static Map<String, String[]> getCommandAliases() {
		return server.getCommandAliases();
	}

	public static int getSpawnRadius() {
		return server.getSpawnRadius();
	}

	public static void setSpawnRadius(int value) {
		server.setSpawnRadius(value);
	}

	public static boolean getOnlineMode() {
		return server.getOnlineMode();
	}

	public static boolean getAllowFlight() {
		return server.getAllowFlight();
	}

	public static boolean isHardcore() {
		return server.isHardcore();
	}

	@Deprecated
	public static boolean useExactLoginLocation() {
		return server.useExactLoginLocation();
	}

	public static void shutdown() {
		server.shutdown();
	}

	public static int broadcast(String message, String permission) {
		return server.broadcast(message, permission);
	}

	@Deprecated
	public static OfflinePlayer getOfflinePlayer(String name) {
		return server.getOfflinePlayer(name);
	}

	public static OfflinePlayer getOfflinePlayer(UUID id) {
		return server.getOfflinePlayer(id);
	}

	public static Set<String> getIPBans() {
		return server.getIPBans();
	}

	public static void banIP(String address) {
		server.banIP(address);
	}

	public static void unbanIP(String address) {
		server.unbanIP(address);
	}

	public static Set<OfflinePlayer> getBannedPlayers() {
		return server.getBannedPlayers();
	}

	public static BanList getBanList(BanList.Type type) {
		return server.getBanList(type);
	}

	public static Set<OfflinePlayer> getOperators() {
		return server.getOperators();
	}

	public static GameMode getDefaultGameMode() {
		return server.getDefaultGameMode();
	}

	public static void setDefaultGameMode(GameMode mode) {
		server.setDefaultGameMode(mode);
	}

	public static ConsoleCommandSender getConsoleSender() {
		return server.getConsoleSender();
	}

	public static boolean versionCommandEnabled() {
		return server.versionCommandEnabled();
	}

	public static boolean reloadCommandEnabled() {
		return server.reloadCommandEnabled();
	}

	public static boolean pluginsCommandEnabled() {
		return server.pluginsCommandEnabled();
	}

	public static File getWorldContainer() {
		return server.getWorldContainer();
	}

	public static OfflinePlayer[] getOfflinePlayers() {
		return server.getOfflinePlayers();
	}

	public static Messenger getMessenger() {
		return server.getMessenger();
	}

	public static HelpMap getHelpMap() {
		return server.getHelpMap();
	}

	public static Inventory createInventory(InventoryHolder owner, InventoryType type) {
		return server.createInventory(owner, type);
	}

	public static Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
		return server.createInventory(owner, type, title);
	}

	public static Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException {
		return server.createInventory(owner, size);
	}

	public static Inventory createInventory(InventoryHolder owner, int size, String title)
			throws IllegalArgumentException {
		return server.createInventory(owner, size, title);
	}

	public static int getMonsterSpawnLimit() {
		return server.getMonsterSpawnLimit();
	}

	public static int getAnimalSpawnLimit() {
		return server.getAnimalSpawnLimit();
	}

	public static int getWaterAnimalSpawnLimit() {
		return server.getWaterAnimalSpawnLimit();
	}

	public static int getAmbientSpawnLimit() {
		return server.getAmbientSpawnLimit();
	}

	public static boolean isPrimaryThread() {
		return server.isPrimaryThread();
	}

	public static String getMotd() {
		return server.getMotd();
	}

	public static String getShutdownMessage() {
		return server.getShutdownMessage();
	}

	public static WarningState getWarningState() {
		return server.getWarningState();
	}

	public static ItemFactory getItemFactory() {
		return server.getItemFactory();
	}

	public static ScoreboardManager getScoreboardManager() {
		return server.getScoreboardManager();
	}

	public static CachedServerIcon getServerIcon() {
		return server.getServerIcon();
	}

	public static CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception {
		return server.loadServerIcon(file);
	}

	public static CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception {
		return server.loadServerIcon(image);
	}

	public static void setIdleTimeout(int threshold) {
		server.setIdleTimeout(threshold);
	}

	public static int getIdleTimeout() {
		return server.getIdleTimeout();
	}

	public static ChunkGenerator.ChunkData createChunkData(World world) {
		return server.createChunkData(world);
	}

	@Deprecated
	public static UnsafeValues getUnsafe() {
		return server.getUnsafe();
	}

	

	public static CommandMap getCommandMap() {
		return server.getCommandMap();
	}

	public static Server.Spigot spigot() {
		return server.spigot();
	}
}
