package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityTeleportEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private Location from;
	private Location to;

	public EntityTeleportEvent(Entity what, Location from, Location to) {
		super(what);
		this.from = from;
		this.to = to;
		this.cancel = false;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		this.to = to;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}