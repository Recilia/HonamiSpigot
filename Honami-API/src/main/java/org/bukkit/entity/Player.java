package org.bukkit.entity;

import java.net.InetSocketAddress;

import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.WeatherType;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.map.MapView;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scoreboard.Scoreboard;

import org.github.paperspigot.Title;

public interface Player extends HumanEntity, Conversable, CommandSender, OfflinePlayer, PluginMessageRecipient {

	public String getDisplayName();

	public void setDisplayName(String name);

	public String getPlayerListName();

	public void setPlayerListName(String name);

	public void setCompassTarget(Location loc);

	public Location getCompassTarget();

	public InetSocketAddress getAddress();

	public void sendRawMessage(String message);

	public void kickPlayer(String message);

	public void chat(String msg);

	public boolean performCommand(String command);

	public boolean isSneaking();

	public void setSneaking(boolean sneak);

	public boolean isSprinting();

	public void setSprinting(boolean sprinting);

	public void saveData();

	public void loadData();

	public void setSleepingIgnored(boolean isSleeping);

	public boolean isSleepingIgnored();

	@Deprecated
	public void playNote(Location loc, byte instrument, byte note);

	public void playNote(Location loc, Instrument instrument, Note note);

	public void playSound(Location location, Sound sound, float volume, float pitch);

	public void playSound(Location location, String sound, float volume, float pitch);

	@Deprecated
	public void playEffect(Location loc, Effect effect, int data);

	public <T> void playEffect(Location loc, Effect effect, T data);

	@Deprecated
	public void sendBlockChange(Location loc, Material material, byte data);

	@Deprecated
	public boolean sendChunkChange(Location loc, int sx, int sy, int sz, byte[] data);

	@Deprecated
	public void sendBlockChange(Location loc, int material, byte data);

	public void sendSignChange(Location loc, String[] lines) throws IllegalArgumentException;

	public void sendMap(MapView map);

	

	@Override
	public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component);

	@Override
	public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components);

	public void setPlayerListHeaderFooter(net.md_5.bungee.api.chat.BaseComponent[] header,
			net.md_5.bungee.api.chat.BaseComponent[] footer);

	public void setPlayerListHeaderFooter(net.md_5.bungee.api.chat.BaseComponent header,
			net.md_5.bungee.api.chat.BaseComponent footer);

	@Deprecated
	public void setTitleTimes(int fadeInTicks, int stayTicks, int fadeOutTicks);

	@Deprecated
	public void setSubtitle(net.md_5.bungee.api.chat.BaseComponent[] subtitle);

	@Deprecated
	public void setSubtitle(net.md_5.bungee.api.chat.BaseComponent subtitle);

	@Deprecated
	public void showTitle(net.md_5.bungee.api.chat.BaseComponent[] title);

	@Deprecated
	public void showTitle(net.md_5.bungee.api.chat.BaseComponent title);

	@Deprecated
	public void showTitle(net.md_5.bungee.api.chat.BaseComponent[] title,
			net.md_5.bungee.api.chat.BaseComponent[] subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks);

	@Deprecated
	public void showTitle(net.md_5.bungee.api.chat.BaseComponent title, net.md_5.bungee.api.chat.BaseComponent subtitle,
			int fadeInTicks, int stayTicks, int fadeOutTicks);

	void sendTitle(Title title);

	void updateTitle(Title title);

	public void hideTitle();

	

	
	public void updateInventory();

	public void awardAchievement(Achievement achievement);

	public void removeAchievement(Achievement achievement);

	public boolean hasAchievement(Achievement achievement);

	public void incrementStatistic(Statistic statistic) throws IllegalArgumentException;

	public void decrementStatistic(Statistic statistic) throws IllegalArgumentException;

	public void incrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException;

	public void decrementStatistic(Statistic statistic, int amount) throws IllegalArgumentException;

	public void setStatistic(Statistic statistic, int newValue) throws IllegalArgumentException;

	public int getStatistic(Statistic statistic) throws IllegalArgumentException;

	public void incrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

	public void decrementStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

	public int getStatistic(Statistic statistic, Material material) throws IllegalArgumentException;

	public void incrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException;

	public void decrementStatistic(Statistic statistic, Material material, int amount) throws IllegalArgumentException;

	public void setStatistic(Statistic statistic, Material material, int newValue) throws IllegalArgumentException;

	public void incrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException;

	public void decrementStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException;

	public int getStatistic(Statistic statistic, EntityType entityType) throws IllegalArgumentException;

	public void incrementStatistic(Statistic statistic, EntityType entityType, int amount)
			throws IllegalArgumentException;

	public void decrementStatistic(Statistic statistic, EntityType entityType, int amount);

	public void setStatistic(Statistic statistic, EntityType entityType, int newValue);

	public void setPlayerTime(long time, boolean relative);

	public long getPlayerTime();

	public long getPlayerTimeOffset();

	public boolean isPlayerTimeRelative();

	public void resetPlayerTime();

	public void setPlayerWeather(WeatherType type);

	public WeatherType getPlayerWeather();

	public void resetPlayerWeather();

	public void giveExp(int amount);

	public void giveExpLevels(int amount);

	public float getExp();

	public void setExp(float exp);

	public int getLevel();

	public void setLevel(int level);

	public int getTotalExperience();

	public void setTotalExperience(int exp);

	public float getExhaustion();

	public void setExhaustion(float value);

	public float getSaturation();

	public void setSaturation(float value);

	public int getFoodLevel();

	public void setFoodLevel(int value);

	public Location getBedSpawnLocation();

	public void setBedSpawnLocation(Location location);

	public void setBedSpawnLocation(Location location, boolean force);

	public boolean getAllowFlight();

	public void setAllowFlight(boolean flight);

	public void hidePlayer(Player player);

	public void hidePlayer(Player player, boolean onTab);

	public void showPlayer(Player player);

	public boolean canSee(Player player);

	public boolean canSee(Entity entity);

	@Deprecated
	public boolean isOnGround();

	public boolean isFlying();

	public void setFlying(boolean value);

	public void setFlySpeed(float value) throws IllegalArgumentException;

	public void setWalkSpeed(float value) throws IllegalArgumentException;

	public float getFlySpeed();

	public float getWalkSpeed();

	@Deprecated
	public void setTexturePack(String url);

	@Deprecated 
	public void setResourcePack(String url);

	public Scoreboard getScoreboard();

	public void setScoreboard(Scoreboard scoreboard) throws IllegalArgumentException, IllegalStateException;

	public boolean isHealthScaled();

	public void setHealthScaled(boolean scale);

	public void setHealthScale(double scale) throws IllegalArgumentException;

	public double getHealthScale();

	public Entity getSpectatorTarget();

	public void setSpectatorTarget(Entity entity);

	@Deprecated
	public void sendTitle(String title, String subtitle);

	
	public void resetTitle();

	

	void setResourcePack(String url, String hash);

	org.bukkit.event.player.PlayerResourcePackStatusEvent.Status getResourcePackStatus();

	String getResourcePackHash();

	boolean hasResourcePack();

	
	public class Spigot extends Entity.Spigot {

		public InetSocketAddress getRawAddress() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY,
				float offsetZ, float speed, int particleCount, int radius) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public boolean getCollidesWithEntities() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void setCollidesWithEntities(boolean collides) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void respawn() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public String getLocale() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public java.util.Set<Player> getHiddenPlayers() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void sendMessage(net.md_5.bungee.api.chat.BaseComponent component) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void sendMessage(net.md_5.bungee.api.chat.BaseComponent... components) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public boolean getAffectsSpawning() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void setAffectsSpawning(boolean affects) {
			throw new UnsupportedOperationException("Not supported yet");
		}

		public int getViewDistance() {
			throw new UnsupportedOperationException("Not supported yet");
		}

		public void setViewDistance(int viewDistance) {
			throw new UnsupportedOperationException("Not supported yet");
		}

		public int getPing() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	Spigot spigot();

	class NachoPlayer {

		public void sendActionBar(String message) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void jump() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	NachoPlayer nacho();

	class Unsafe {

		public void sendPacket(Object packet) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	Unsafe unsafe();

	double getBlockReach();

	

	int getPing();
	
}