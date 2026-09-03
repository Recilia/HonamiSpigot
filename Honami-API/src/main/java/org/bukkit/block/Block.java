package org.bukkit.block;

import java.util.Collection;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;

public interface Block extends Metadatable {

	@Deprecated
	byte getData();

	Block getRelative(int modX, int modY, int modZ);

	Block getRelative(BlockFace face);

	Block getRelative(BlockFace face, int distance);

	Material getType();

	@Deprecated
	int getTypeId();

	byte getLightLevel();

	byte getLightFromSky();

	byte getLightFromBlocks();

	World getWorld();

	int getX();

	int getY();

	int getZ();

	Location getLocation();

	Location getLocation(Location loc);

	Chunk getChunk();

	@Deprecated
	void setData(byte data);

	@Deprecated
	void setData(byte data, boolean applyPhysics);

	void setType(Material type);

	void setType(Material type, boolean applyPhysics);

	void setType(Material type, boolean applyPhysics, boolean updateLight);

	@Deprecated
	boolean setTypeId(int type);

	@Deprecated
	boolean setTypeId(int type, boolean applyPhysics);

	@Deprecated
	boolean setTypeIdAndData(int type, byte data, boolean applyPhysics);

	BlockFace getFace(Block block);

	BlockState getState();

	Biome getBiome();

	void setBiome(Biome bio);

	boolean isBlockPowered();

	boolean isBlockIndirectlyPowered();

	boolean isBlockFacePowered(BlockFace face);

	boolean isBlockFaceIndirectlyPowered(BlockFace face);

	int getBlockPower(BlockFace face);

	int getBlockPower();

	boolean isEmpty();

	boolean isLiquid();

	double getTemperature();

	double getHumidity();

	PistonMoveReaction getPistonMoveReaction();

	boolean breakNaturally();

	boolean breakNaturally(ItemStack tool);

	Collection<ItemStack> getDrops();

	Collection<ItemStack> getDrops(ItemStack tool);

}
