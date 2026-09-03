
package org.bukkit.inventory.meta;

import org.bukkit.block.BlockState;

public interface BlockStateMeta extends ItemMeta {

	boolean hasBlockState();

	BlockState getBlockState();

	void setBlockState(BlockState blockState);
}
