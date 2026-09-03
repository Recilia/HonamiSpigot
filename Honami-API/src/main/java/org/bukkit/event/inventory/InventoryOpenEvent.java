package org.bukkit.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;

public class InventoryOpenEvent extends InventoryEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;

	public InventoryOpenEvent(InventoryView transaction) {
		super(transaction);
		this.cancelled = false;
	}

	public final HumanEntity getPlayer() {
		return transaction.getPlayer();
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
