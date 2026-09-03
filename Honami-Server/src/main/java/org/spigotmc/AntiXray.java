package org.spigotmc;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.craftbukkit.util.CraftMagicNumbers;

import co.aikar.timings.SpigotTimings;
import it.unimi.dsi.fastutil.bytes.ByteOpenHashSet;
import it.unimi.dsi.fastutil.bytes.ByteSet;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.Blocks;

import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.World;
import rein.honami.spigot.taco.utils.BlockHelper;

public class AntiXray {

	private final boolean[] obfuscateBlocks = new boolean[Short.MAX_VALUE];
	
	private final byte[] replacementOres;
	
	public boolean queueUpdates = true;
	public final Set<BlockPosition> pendingUpdates = new HashSet<BlockPosition>();

	public AntiXray(SpigotWorldConfig config) {
		
		for (int id : (config.engineMode == 1) ? config.hiddenBlocks : config.replaceBlocks) {
			obfuscateBlocks[id] = true;
		}

		ByteSet blocks = new ByteOpenHashSet(); 
		for (Integer i : config.hiddenBlocks) {
			Block block = Block.getById(i);
			
			if (block != null && !block.isTileEntity()) {
				
				blocks.add((byte) (int) i);
			}
		}
		
		replacementOres = blocks.toByteArray(); 
	}

	public void flushUpdates(World world) {
		if (world.spigotConfig.antiXray && !pendingUpdates.isEmpty()) {
			queueUpdates = false;

			for (BlockPosition position : pendingUpdates) {
				updateNearbyBlocks(world, position);
			}

			pendingUpdates.clear();
			queueUpdates = true;
		}
	}

	public void updateNearbyBlocks(World world, BlockPosition position) {
		if (world.spigotConfig.antiXray) {
			
			if (queueUpdates) {
				pendingUpdates.add(position);
				return;
			}
			
			SpigotTimings.antiXrayUpdateTimer.startTiming();
			updateNearbyBlocks(world, position, 2, false); 
															
			SpigotTimings.antiXrayUpdateTimer.stopTiming();
		}
	}

	public void obfuscateSync(int chunkX, int chunkY, int bitmask, byte[] buffer, World world) {
		if (world.spigotConfig.antiXray) {
			SpigotTimings.antiXrayObfuscateTimer.startTiming();
			obfuscate(chunkX, chunkY, bitmask, buffer, world);
			SpigotTimings.antiXrayObfuscateTimer.stopTiming();
		}
	}

	public void obfuscate(int chunkX, int chunkY, int bitmask, byte[] buffer, World world) {
		
		if (world.spigotConfig.antiXray) {
			
			int initialRadius = 1;
			
			int index = 0;
			
			int randomOre = 0;

			int startX = chunkX << 4;
			int startZ = chunkY << 4;

			byte replaceWithTypeId;
			switch (world.getWorld().getEnvironment()) {
			case NETHER:
				replaceWithTypeId = (byte) CraftMagicNumbers.getId(Blocks.NETHERRACK);
				break;
			case THE_END:
				replaceWithTypeId = (byte) CraftMagicNumbers.getId(Blocks.END_STONE);
				break;
			default:
				replaceWithTypeId = (byte) CraftMagicNumbers.getId(Blocks.STONE);
				break;
			}

			BlockPosition.MutableBlockPosition pos = new BlockPosition.MutableBlockPosition(); 

			
			for (int i = 0; i < 16; i++) {
				
				if ((bitmask & 1 << i) != 0) {
					
					for (int y = 0; y < 16; y++) {
						for (int z = 0; z < 16; z++) {
							for (int x = 0; x < 16; x++) {

								if (index >= buffer.length) {
									index++;
									continue;
								}

								int blockId = (buffer[index << 1] & 0xFF) | ((buffer[(index << 1) + 1] & 0xFF) << 8);
								blockId >>>= 4;
								
								if (obfuscateBlocks[blockId]) {

									pos.setValues(startX + x, (i << 4) + y, startZ + z);
									if (!isLoaded(world,
											 pos,
											initialRadius)) {
										
										index++;
										continue;
									}

									if (!hasTransparentBlockAdjacent(world,
											 pos,
											initialRadius)) 
									{
										int newId = blockId;
										switch (world.spigotConfig.engineMode) {
										case 1:
											
											newId = replaceWithTypeId & 0xFF;
											break;
										case 2:
											
											if (randomOre >= replacementOres.length) {
												randomOre = 0;
											}
											newId = replacementOres[randomOre++] & 0xFF;
											break;
										}
										newId <<= 4;
										buffer[index << 1] = (byte) (newId & 0xFF);
										buffer[(index << 1) + 1] = (byte) ((newId >> 8) & 0xFF);
									}
								}

								index++;
							}
						}
					}
				}
			}
		}
	}

	private void updateNearbyBlocks(World world, final BlockPosition startPos, int radius, boolean updateSelf) {
		int startX = startPos.getX() - radius;
		int endX = startPos.getX() + radius;
		int startY = Math.max(0, startPos.getY() - radius);
		int endY = Math.min(255, startPos.getY() + radius);
		int startZ = startPos.getZ() - radius;
		int endZ = startPos.getZ() + radius;
		BlockPosition.MutableBlockPosition adjacent = new BlockPosition.MutableBlockPosition();
		for (int x = startX; x <= endX; x++) {
			for (int y = startY; y <= endY; y++) {
				for (int z = startZ; z <= endZ; z++) {
					adjacent.setValues(x, y, z);
					if (!updateSelf && x == startPos.getX() & y == startPos.getY() & z == startPos.getZ()) {
						continue;
					}
					if (world.isLoaded(adjacent)) {
						updateBlock(world, adjacent);
					}
				}
			}
		}
	}

	private void updateBlock(World world, BlockPosition position) {
		
		if (true) 
		{
			
			Block block = getType(world, position); 

			if (obfuscateBlocks[Block.getId(block)]) 
			{
				
				world.notify(position);
			}

			

			
		}
	}

	private static boolean isLoaded(World world, BlockPosition position, int radius) {
		
		return BlockHelper.isAllAdjacentBlocksLoaded(world, position, radius);
		
	}

	private static boolean hasTransparentBlockAdjacent(World w, BlockPosition startPos, int radius) {
		
		return !BlockHelper.isAllAdjacentBlocksFillPredicate(w, startPos, radius, (world, position) -> {
			Block block = getType(world, position);
			return isSolidBlock(block);
		}); 
		
	}

	private static boolean isSolidBlock(Block block) {

		

		return block.isOccluding() && block != Blocks.MOB_SPAWNER && block != Blocks.BARRIER;
	}

	public static Block getType(World world, BlockPosition pos) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		Chunk chunk = world.getChunkIfLoaded(x >> 4, z >> 4);
		if (chunk == null) {
			return Blocks.AIR;
		}
		int sectionId = y >> 4;
		if (sectionId < 0 || sectionId > 15) {
			return Blocks.AIR;
		}
		ChunkSection section = chunk.getSections()[sectionId];
		if (section == null) {
			return Blocks.AIR; 
		}
		x &= 0xF;
		y &= 0xF;
		z &= 0xF;
		int combinedId = section.getIdArray()[(y << 8) | (z << 4) | x];
		int blockId = combinedId >> 4;
		return BlockHelper.getBlock(blockId);
	}
	
}
