package org.bukkit.entity;

import org.bukkit.block.BlockFace;
import org.bukkit.material.Attachable;

public interface Hanging extends Entity, Attachable {

	public boolean setFacingDirection(BlockFace face, boolean force);
}
