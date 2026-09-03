package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ItemDespawnEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean canceled;
	private final Location location;

	public ItemDespawnEvent(final Item despawnee, final Location loc) {
		super(despawnee);
		location = loc;
	}

	public boolean isCancelled() {
		return canceled;
	}

	public void setCancelled(boolean cancel) {
		canceled = cancel;
	}

	@Override
	public Item getEntity() {
		return (Item) entity;
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
