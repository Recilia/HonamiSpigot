package org.bukkit.event.block;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

public class BlockPistonRetractEvent extends BlockPistonEvent {
	private static final HandlerList handlers = new HandlerList();
	private List<Block> blocks;

	public BlockPistonRetractEvent(final Block block, final List<Block> blocks, final BlockFace direction) {
		super(block, direction);

		this.blocks = blocks;
	}

	@Deprecated
	public Location getRetractLocation() {
		return getBlock().getRelative(getDirection(), 2).getLocation();
	}

	public List<Block> getBlocks() {
		return blocks;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
