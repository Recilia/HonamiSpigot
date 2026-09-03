package org.bukkit.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

public abstract class ChunkGenerator {

	public interface BiomeGrid {

		Biome getBiome(int x, int z);

		void setBiome(int x, int z, Biome bio);
	}

	@Deprecated

	public byte[] generate(World world, Random random, int x, int z) {
		throw new UnsupportedOperationException(
				"Custom generator is missing required methods: generate(), generateBlockSections() and generateExtBlockSections()");
	}

	@Deprecated
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
		return null; 
	}

	@Deprecated
	public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
		return null; 
	}

	public ChunkData generateChunkData(World world, Random random, int x, int z, BiomeGrid biome) {
		return null; 
	}

	protected final ChunkData createChunkData(World world) {
		return Bukkit.getServer().createChunkData(world);
	}

	public boolean canSpawn(World world, int x, int z) {
		Block highest = world.getBlockAt(x, world.getHighestBlockYAt(x, z), z);

		switch (world.getEnvironment()) {
		case NETHER:
			return true;
		case THE_END:
			return highest.getType() != Material.AIR && highest.getType() != Material.WATER
					&& highest.getType() != Material.LAVA;
		case NORMAL:
		default:
			return highest.getType() == Material.SAND || highest.getType() == Material.GRAVEL;
		}
	}

	public List<BlockPopulator> getDefaultPopulators(World world) {
		return new ArrayList<BlockPopulator>();
	}

	public Location getFixedSpawnLocation(World world, Random random) {
		return null;
	}

	public static interface ChunkData {

		public int getMaxHeight();

		public void setBlock(int x, int y, int z, Material material);

		public void setBlock(int x, int y, int z, MaterialData material);

		public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material);

		public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material);

		public Material getType(int x, int y, int z);

		public MaterialData getTypeAndData(int x, int y, int z);

		@Deprecated
		public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId);

		@Deprecated
		public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId, int data);

		@Deprecated
		public void setBlock(int x, int y, int z, int blockId);

		@Deprecated
		public void setBlock(int x, int y, int z, int blockId, byte data);

		@Deprecated
		public int getTypeId(int x, int y, int z);

		@Deprecated
		public byte getData(int x, int y, int z);
	}
}
