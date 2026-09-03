package org.bukkit.material;

import org.bukkit.block.BlockFace;

public interface Directional {

	public void setFacingDirection(BlockFace face);

	public BlockFace getFacing();
}
