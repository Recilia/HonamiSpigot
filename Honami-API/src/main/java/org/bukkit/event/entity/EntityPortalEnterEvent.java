package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class EntityPortalEnterEvent extends EntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Location location;

	public EntityPortalEnterEvent(final Entity entity, final Location location) {
		super(entity);
		this.location = location;
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
