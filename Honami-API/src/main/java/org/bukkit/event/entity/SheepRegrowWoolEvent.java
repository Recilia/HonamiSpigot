package org.bukkit.event.entity;

import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SheepRegrowWoolEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;

	public SheepRegrowWoolEvent(final Sheep sheep) {
		super(sheep);
		this.cancel = false;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public Sheep getEntity() {
		return (Sheep) entity;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
