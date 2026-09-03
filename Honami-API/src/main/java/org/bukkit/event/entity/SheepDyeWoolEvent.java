package org.bukkit.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SheepDyeWoolEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private DyeColor color;

	public SheepDyeWoolEvent(final Sheep sheep, final DyeColor color) {
		super(sheep);
		this.cancel = false;
		this.color = color;
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

	public DyeColor getColor() {
		return color;
	}

	public void setColor(DyeColor color) {
		this.color = color;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
