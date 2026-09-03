package org.bukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class HangingBreakEvent extends HangingEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private final HangingBreakEvent.RemoveCause cause;

	public HangingBreakEvent(final Hanging hanging, final HangingBreakEvent.RemoveCause cause) {
		super(hanging);
		this.cause = cause;
	}

	public HangingBreakEvent.RemoveCause getCause() {
		return cause;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	public enum RemoveCause {

		ENTITY,

		EXPLOSION,

		OBSTRUCTION,

		PHYSICS,

		DEFAULT,
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
