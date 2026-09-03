package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockGrowEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final BlockState newState;
	private boolean cancelled = false;

	public BlockGrowEvent(final Block block, final BlockState newState) {
		super(block);
		this.newState = newState;
	}

	public BlockState getNewState() {
		return newState;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
