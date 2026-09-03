package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Explosive;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class ExplosionPrimeEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private float radius;
	private boolean fire;

	public ExplosionPrimeEvent(final Entity what, final float radius, final boolean fire) {
		super(what);
		this.cancel = false;
		this.radius = radius;
		this.fire = fire;
	}

	public ExplosionPrimeEvent(final Explosive explosive) {
		this(explosive, explosive.getYield(), explosive.isIncendiary());
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public boolean getFire() {
		return fire;
	}

	public void setFire(boolean fire) {
		this.fire = fire;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
