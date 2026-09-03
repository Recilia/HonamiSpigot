package org.bukkit.entity;

import org.bukkit.Material;

public interface FallingBlock extends Entity {

	Material getMaterial();

	@Deprecated
	int getBlockId();

	@Deprecated
	byte getBlockData();

	boolean getDropItem();

	void setDropItem(boolean drop);

	boolean canHurtEntities();

	void setHurtEntities(boolean hurtEntities);

	org.bukkit.Location getSourceLoc(); 
}
