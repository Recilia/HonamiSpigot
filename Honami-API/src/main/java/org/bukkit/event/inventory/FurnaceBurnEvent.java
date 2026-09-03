package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceBurnEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final ItemStack fuel;
	private int burnTime;
	private boolean cancelled;
	private boolean burning;

	public FurnaceBurnEvent(final Block furnace, final ItemStack fuel, final int burnTime) {
		super(furnace);
		this.fuel = fuel;
		this.burnTime = burnTime;
		this.cancelled = false;
		this.burning = true;
	}

	@Deprecated
	public Block getFurnace() {
		return getBlock();
	}

	public ItemStack getFuel() {
		return fuel;
	}

	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

	public boolean isBurning() {
		return this.burning;
	}

	public void setBurning(boolean burning) {
		this.burning = burning;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
