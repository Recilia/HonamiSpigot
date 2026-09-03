package org.bukkit.event.player;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerPickupItemEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Item item;
	private boolean cancel = false;
	private final int remaining;

	public PlayerPickupItemEvent(final Player player, final Item item, final int remaining) {
		super(player);
		this.item = item;
		this.remaining = remaining;
	}

	public Item getItem() {
		return item;
	}

	public int getRemaining() {
		return remaining;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
