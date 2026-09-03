package org.bukkit.event.entity;

import org.bukkit.entity.Firework;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class FireworkExplodeEvent extends EntityEvent implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;

	public FireworkExplodeEvent(final Firework what) {
		super(what);
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public Firework getEntity() {
		return (Firework) super.getEntity();
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
