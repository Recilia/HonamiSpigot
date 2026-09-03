package org.bukkit.block;

import org.bukkit.SkullType;

public interface Skull extends BlockState {

	public boolean hasOwner();

	public String getOwner();

	public boolean setOwner(String name);

	public BlockFace getRotation();

	public void setRotation(BlockFace rotation);

	public SkullType getSkullType();

	public void setSkullType(SkullType skullType);
}
