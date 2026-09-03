package org.bukkit.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

public class FurnaceSmeltEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final ItemStack source;
	private ItemStack result;
	private boolean cancelled;

	public FurnaceSmeltEvent(final Block furnace, final ItemStack source, final ItemStack result) {
		super(furnace);
		this.source = source;
		this.result = result;
		this.cancelled = false;
	}

	@Deprecated
	public Block getFurnace() {
		return getBlock();
	}

	public ItemStack getSource() {
		return source;
	}

	public ItemStack getResult() {
		return result;
	}

	public void setResult(ItemStack result) {
		this.result = result;
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
