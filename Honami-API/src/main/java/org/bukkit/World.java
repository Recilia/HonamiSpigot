package org.bukkit;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.util.Vector;

public interface World extends PluginMessageRecipient, Metadatable {

	public Block getBlockAt(int x, int y, int z);

	public Block getBlockAt(Location location);

	@Deprecated
	public int getBlockTypeIdAt(int x, int y, int z);

	@Deprecated
	public int getBlockTypeIdAt(Location location);

	public int getHighestBlockYAt(int x, int z);

	public int getHighestBlockYAt(Location location);

	public Block getHighestBlockAt(int x, int z);

	public Block getHighestBlockAt(Location location);

	public Chunk getChunkAt(int x, int z);

	public Chunk getChunkAt(Location location);

	public Chunk getChunkAt(Block block);

	public static interface ChunkLoadCallback {
		public void onLoad(Chunk chunk);
	}

	public void getChunkAtAsync(int x, int z, ChunkLoadCallback cb);

	public void getChunkAtAsync(Location location, ChunkLoadCallback cb);

	public void getChunkAtAsync(Block block, ChunkLoadCallback cb);

	

	public boolean isChunkLoaded(Chunk chunk);

	public Chunk[] getLoadedChunks();

	public void loadChunk(Chunk chunk);

	public boolean isChunkLoaded(int x, int z);

	public boolean isChunkInUse(int x, int z);

	public void loadChunk(int x, int z);

	public boolean loadChunk(int x, int z, boolean generate);

	public boolean unloadChunk(Chunk chunk);

	public boolean unloadChunk(int x, int z);

	public boolean unloadChunk(int x, int z, boolean save);

	public boolean unloadChunk(int x, int z, boolean save, boolean safe);

	public boolean unloadChunkRequest(int x, int z);

	public boolean unloadChunkRequest(int x, int z, boolean safe);

	public boolean regenerateChunk(int x, int z);

	@Deprecated
	public boolean refreshChunk(int x, int z);

	public Item dropItem(Location location, ItemStack item);

	public Item dropItemNaturally(Location location, ItemStack item);

	public Arrow spawnArrow(Location location, Vector direction, float speed, float spread);

	public boolean generateTree(Location location, TreeType type);

	public boolean generateTree(Location loc, TreeType type, BlockChangeDelegate delegate);

	public Entity spawnEntity(Location loc, EntityType type);

	@Deprecated
	public LivingEntity spawnCreature(Location loc, EntityType type);

	@Deprecated
	public LivingEntity spawnCreature(Location loc, CreatureType type);

	public LightningStrike strikeLightning(Location loc);

	public LightningStrike strikeLightningEffect(Location loc);

	public List<Entity> getEntities();

	public List<LivingEntity> getLivingEntities();

	@Deprecated
	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T>... classes);

	public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> cls);

	public Collection<Entity> getEntitiesByClasses(Class<?>... classes);

	public List<Player> getPlayers();

	public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z);

	public String getName();

	public UUID getUID();

	public Location getSpawnLocation();

	public boolean setSpawnLocation(int x, int y, int z);

	public long getTime();

	public void setTime(long time);

	public long getFullTime();

	public void setFullTime(long time);

	public boolean hasStorm();

	public void setStorm(boolean hasStorm);

	public int getWeatherDuration();

	public void setWeatherDuration(int duration);

	public boolean isThundering();

	public void setThundering(boolean thundering);

	public int getThunderDuration();

	public void setThunderDuration(int duration);

	public boolean createExplosion(double x, double y, double z, float power);

	public boolean createExplosion(double x, double y, double z, float power, boolean setFire);

	public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks);

	public boolean createExplosion(Location loc, float power);

	public boolean createExplosion(Location loc, float power, boolean setFire);

	public Environment getEnvironment();

	public long getSeed();

	public boolean getPVP();

	public void setPVP(boolean pvp);

	public ChunkGenerator getGenerator();

	public void save();

	public List<BlockPopulator> getPopulators();

	public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException;

	@Deprecated
	public FallingBlock spawnFallingBlock(Location location, Material material, byte data)
			throws IllegalArgumentException;

	@Deprecated
	public FallingBlock spawnFallingBlock(Location location, int blockId, byte blockData)
			throws IllegalArgumentException;

	public void playEffect(Location location, Effect effect, int data);

	public void playEffect(Location location, Effect effect, int data, int radius);

	public <T> void playEffect(Location location, Effect effect, T data);

	public <T> void playEffect(Location location, Effect effect, T data, int radius);

	public ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain);

	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals);

	public boolean getAllowAnimals();

	public boolean getAllowMonsters();

	Biome getBiome(int x, int z);

	void setBiome(int x, int z, Biome bio);

	public double getTemperature(int x, int z);

	public double getHumidity(int x, int z);

	public int getMaxHeight();

	public int getSeaLevel();

	public boolean getKeepSpawnInMemory();

	public void setKeepSpawnInMemory(boolean keepLoaded);

	public boolean isAutoSave();

	public void setAutoSave(boolean value);

	public void setDifficulty(Difficulty difficulty);

	public Difficulty getDifficulty();

	public File getWorldFolder();

	public WorldType getWorldType();

	public boolean canGenerateStructures();

	public long getTicksPerAnimalSpawns();

	public void setTicksPerAnimalSpawns(int ticksPerAnimalSpawns);

	public long getTicksPerMonsterSpawns();

	public void setTicksPerMonsterSpawns(int ticksPerMonsterSpawns);

	int getMonsterSpawnLimit();

	void setMonsterSpawnLimit(int limit);

	int getAnimalSpawnLimit();

	void setAnimalSpawnLimit(int limit);

	int getWaterAnimalSpawnLimit();

	void setWaterAnimalSpawnLimit(int limit);

	int getAmbientSpawnLimit();

	void setAmbientSpawnLimit(int limit);

	void playSound(Location location, Sound sound, float volume, float pitch);

	public String[] getGameRules();

	public String getGameRuleValue(String rule);

	public boolean setGameRuleValue(String rule, String value);

	public boolean isGameRule(String rule);

	public class Spigot {

		public void playEffect(Location location, Effect effect) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void playEffect(Location location, Effect effect, int id, int data, float offsetX, float offsetY,
				float offsetZ, float speed, int particleCount, int radius) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public LightningStrike strikeLightning(Location loc, boolean isSilent) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public LightningStrike strikeLightningEffect(Location loc, boolean isSilent) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	Spigot spigot();

	

	public WorldBorder getWorldBorder();

	public enum Environment {

		NORMAL(0),

		NETHER(-1),

		THE_END(1);

		private final int id;
		private static final Map<Integer, Environment> lookup = new HashMap<Integer, Environment>();

		private Environment(int id) {
			this.id = id;
		}

		@Deprecated
		public int getId() {
			return id;
		}

		@Deprecated
		public static Environment getEnvironment(int id) {
			return lookup.get(id);
		}

		static {
			for (Environment env : values()) {
				lookup.put(env.getId(), env);
			}
		}
	}
}
