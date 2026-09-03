package org.bukkit.event.painting;

import org.bukkit.Warning;
import org.bukkit.entity.Painting;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Deprecated
@Warning(reason = "This event has been replaced by HangingBreakEvent")
public class PaintingBreakEvent extends PaintingEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private final RemoveCause cause;

	public PaintingBreakEvent(final Painting painting, final RemoveCause cause) {
		super(painting);
		this.cause = cause;
	}

	public RemoveCause getCause() {
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

		FIRE,

		OBSTRUCTION,

		WATER,

		PHYSICS,
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
