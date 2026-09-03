package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockDispenseEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private ItemStack item;
	private Vector velocity;

	public BlockDispenseEvent(final Block block, final ItemStack dispensed, final Vector velocity) {
		super(block);
		this.item = dispensed;
		this.velocity = velocity;
	}

	public ItemStack getItem() {
		return item.clone();
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public Vector getVelocity() {
		return velocity.clone();
	}

	public void setVelocity(Vector vel) {
		velocity = vel;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
