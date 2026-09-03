package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public class BlockRedstoneEvent extends BlockEvent {
	private static final HandlerList handlers = new HandlerList();
	private final int oldCurrent;
	private int newCurrent;

	public BlockRedstoneEvent(final Block block, final int oldCurrent, final int newCurrent) {
		super(block);
		this.oldCurrent = oldCurrent;
		this.newCurrent = newCurrent;
	}

	public int getOldCurrent() {
		return oldCurrent;
	}

	public int getNewCurrent() {
		return newCurrent;
	}

	public void setNewCurrent(int newCurrent) {
		this.newCurrent = newCurrent;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
