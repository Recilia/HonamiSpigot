package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockFormEvent extends BlockGrowEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();

	public BlockFormEvent(final Block block, final BlockState newState) {
		super(block, newState);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
