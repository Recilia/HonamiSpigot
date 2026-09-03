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
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;

import com.avaje.ebean.config.ServerConfig;
import com.google.common.collect.ImmutableList;

public interface Server extends PluginMessageRecipient {

	public static final String BROADCAST_CHANNEL_ADMINISTRATIVE = "bukkit.broadcast.admin";

	public static final String BROADCAST_CHANNEL_USERS = "bukkit.broadcast.user";

	public String getName();

	public String getVersion();

	public String getBukkitVersion();

	@Deprecated
	public Player[] _INVALID_getOnlinePlayers();

	public Collection<? extends Player> getOnlinePlayers();

	public int getMaxPlayers();

	public void setMaxPlayers(int maxPlayers); 

	

	public int getPort();

	public int getViewDistance();

	public String getIp();

	public String getServerName();

	public String getServerId();

	public String getWorldType();

	public boolean getGenerateStructures();

	public boolean getAllowEnd();

	public boolean getAllowNether();

	public boolean hasWhitelist();

	public void setWhitelist(boolean value);

	public Set<OfflinePlayer> getWhitelistedPlayers();

	public void reloadWhitelist();

	public int broadcastMessage(String message);

	

	public void broadcast(net.md_5.bungee.api.chat.BaseComponent component);

	public void broadcast(net.md_5.bungee.api.chat.BaseComponent... components);

	

	public String getUpdateFolder();

	public File getUpdateFolderFile();

	public long getConnectionThrottle();

	public int getTicksPerAnimalSpawns();

	public int getTicksPerMonsterSpawns();

	public Player getPlayer(String name);

	public Player getPlayerExact(String name);

	public List<Player> matchPlayer(String name);

	public Player getPlayer(UUID id);

	public Entity getEntity(UUID uuid);

	public PluginManager getPluginManager();

	public BukkitScheduler getScheduler();

	public ServicesManager getServicesManager();

	public List<World> getWorlds();

	public World createWorld(WorldCreator creator);

	public boolean unloadWorld(String name, boolean save);

	public boolean unloadWorld(World world, boolean save);

	public World getWorld(String name);

	public World getWorld(UUID uid);

	@Deprecated
	public MapView getMap(short id);

	public MapView createMap(World world);

	public void reload();

	public Logger getLogger();

	public PluginCommand getPluginCommand(String name);

	public void savePlayers();

	public boolean dispatchCommand(CommandSender sender, String commandLine) throws CommandException;

	public void configureDbConfig(ServerConfig config);

	public boolean addRecipe(Recipe recipe);

	public List<Recipe> getRecipesFor(ItemStack result);

	public Iterator<Recipe> recipeIterator();

	public void clearRecipes();

	public void resetRecipes();

	public Map<String, String[]> getCommandAliases();

	public int getSpawnRadius();

	public void setSpawnRadius(int value);

	public boolean getOnlineMode();

	public boolean getAllowFlight();

	public boolean isHardcore();

	@Deprecated
	public boolean useExactLoginLocation();

	public void shutdown();

	public int broadcast(String message, String permission);

	@Deprecated
	public OfflinePlayer getOfflinePlayer(String name);

	public OfflinePlayer getOfflinePlayer(UUID id);

	public Set<String> getIPBans();

	public void banIP(String address);

	public void unbanIP(String address);

	public Set<OfflinePlayer> getBannedPlayers();

	public BanList getBanList(BanList.Type type);

	public Set<OfflinePlayer> getOperators();

	public GameMode getDefaultGameMode();

	public void setDefaultGameMode(GameMode mode);

	public ConsoleCommandSender getConsoleSender();

	default boolean versionCommandEnabled() {
		return true;
	}

	default boolean reloadCommandEnabled() {
		return true;
	}

	default boolean pluginsCommandEnabled() {
		return true;
	}

	public File getWorldContainer();

	public OfflinePlayer[] getOfflinePlayers();

	public Messenger getMessenger();

	public HelpMap getHelpMap();

	Inventory createInventory(InventoryHolder owner, InventoryType type);

	Inventory createInventory(InventoryHolder owner, InventoryType type, String title);

	Inventory createInventory(InventoryHolder owner, int size) throws IllegalArgumentException;

	Inventory createInventory(InventoryHolder owner, int size, String title) throws IllegalArgumentException;

	int getMonsterSpawnLimit();

	int getAnimalSpawnLimit();

	int getWaterAnimalSpawnLimit();

	int getAmbientSpawnLimit();

	boolean isPrimaryThread();

	String getMotd();

	String getShutdownMessage();

	public WarningState getWarningState();

	ItemFactory getItemFactory();

	ScoreboardManager getScoreboardManager();

	CachedServerIcon getServerIcon();

	CachedServerIcon loadServerIcon(File file) throws IllegalArgumentException, Exception;

	CachedServerIcon loadServerIcon(BufferedImage image) throws IllegalArgumentException, Exception;

	public void setIdleTimeout(int threshold);

	public int getIdleTimeout();

	public ChunkGenerator.ChunkData createChunkData(World world);

	@Deprecated
	UnsafeValues getUnsafe();

	

	CommandMap getCommandMap();

	public class Spigot {
		@Deprecated
		public org.bukkit.configuration.file.YamlConfiguration getConfig() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public org.bukkit.configuration.file.YamlConfiguration getBukkitConfig() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public org.bukkit.configuration.file.YamlConfiguration getSpigotConfig() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public org.bukkit.configuration.file.YamlConfiguration getPaperSpigotConfig() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public org.bukkit.configuration.file.YamlConfiguration getHonamiConfig() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		

		public void broadcast(net.md_5.bungee.api.chat.BaseComponent component) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void broadcast(net.md_5.bungee.api.chat.BaseComponent... components) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void restart() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public double[] getTPS() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
		
	}

	Spigot spigot();
}
