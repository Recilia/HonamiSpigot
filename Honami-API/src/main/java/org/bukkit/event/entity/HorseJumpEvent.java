package org.bukkit.event.entity;

import org.bukkit.entity.Horse;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class HorseJumpEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private float power;

	public HorseJumpEvent(final Horse horse, final float power) {
		super(horse);
		this.power = power;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public Horse getEntity() {
		return (Horse) entity;
	}

	public float getPower() {
		return power;
	}

	public void setPower(float power) {
		this.power = power;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
