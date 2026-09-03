package org.bukkit;

import org.bukkit.block.Biome;

public interface ChunkSnapshot {

	int getX();

	int getZ();

	String getWorldName();

	@Deprecated
	int getBlockTypeId(int x, int y, int z);

	@Deprecated
	int getBlockData(int x, int y, int z);

	int getBlockSkyLight(int x, int y, int z);

	int getBlockEmittedLight(int x, int y, int z);

	int getHighestBlockYAt(int x, int z);

	Biome getBiome(int x, int z);

	double getRawBiomeTemperature(int x, int z);

	double getRawBiomeRainfall(int x, int z);

	long getCaptureFullTime();

	boolean isSectionEmpty(int sy);
}
