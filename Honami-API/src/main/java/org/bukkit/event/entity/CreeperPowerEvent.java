package org.bukkit.event.entity;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class CreeperPowerEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean canceled;
	private final PowerCause cause;
	private LightningStrike bolt;

	public CreeperPowerEvent(final Creeper creeper, final LightningStrike bolt, final PowerCause cause) {
		this(creeper, cause);
		this.bolt = bolt;
	}

	public CreeperPowerEvent(final Creeper creeper, final PowerCause cause) {
		super(creeper);
		this.cause = cause;
	}

	public boolean isCancelled() {
		return canceled;
	}

	public void setCancelled(boolean cancel) {
		canceled = cancel;
	}

	@Override
	public Creeper getEntity() {
		return (Creeper) entity;
	}

	public LightningStrike getLightning() {
		return bolt;
	}

	public PowerCause getCause() {
		return cause;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum PowerCause {

		LIGHTNING,

		SET_ON,

		SET_OFF
	}
}
