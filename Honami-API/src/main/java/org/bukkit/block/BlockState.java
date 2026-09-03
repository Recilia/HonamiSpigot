package org.bukkit.block;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.Metadatable;

public interface BlockState extends Metadatable {

	Block getBlock();

	MaterialData getData();

	Material getType();

	@Deprecated
	int getTypeId();

	byte getLightLevel();

	World getWorld();

	int getX();

	int getY();

	int getZ();

	Location getLocation();

	Location getLocation(Location loc);

	Chunk getChunk();

	void setData(MaterialData data);

	void setType(Material type);

	@Deprecated
	boolean setTypeId(int type);

	boolean update();

	boolean update(boolean force);

	boolean update(boolean force, boolean applyPhysics);

	@Deprecated
	public byte getRawData();

	@Deprecated
	public void setRawData(byte data);

	boolean isPlaced();
}
