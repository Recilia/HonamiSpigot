package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class EntityPortalExitEvent extends EntityTeleportEvent {
	private static final HandlerList handlers = new HandlerList();
	private Vector before;
	private Vector after;

	public EntityPortalExitEvent(final Entity entity, final Location from, final Location to, final Vector before,
			final Vector after) {
		super(entity, from, to);
		this.before = before;
		this.after = after;
	}

	public Vector getBefore() {
		return this.before.clone();
	}

	public Vector getAfter() {
		return this.after.clone();
	}

	public void setAfter(Vector after) {
		this.after = after.clone();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}