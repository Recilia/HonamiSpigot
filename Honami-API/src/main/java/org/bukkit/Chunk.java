package org.bukkit;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public interface Chunk {

	int getX();

	int getZ();

	World getWorld();

	Block getBlock(int x, int y, int z);

	ChunkSnapshot getChunkSnapshot();

	ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome, boolean includeBiomeTempRain);

	Entity[] getEntities();

	BlockState[] getTileEntities();

	boolean isLoaded();

	boolean load(boolean generate);

	boolean load();

	boolean unload(boolean save, boolean safe);

	boolean unload(boolean save);

	boolean unload();

	public List<Block> getBlocks(Material material);
}
