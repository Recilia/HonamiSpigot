package rein.honami.spigot.taco.event.entity;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class ArrowCollideEvent extends EntityEvent implements Cancellable {
	private final Entity collidedWith;

	public Entity getCollidedWith() {
		return collidedWith;
	}

	public ArrowCollideEvent(Arrow what, Entity collidedWith) {
		super(what);
		this.collidedWith = collidedWith;
	}

	public Arrow getEntity() {
		return (Arrow) super.getEntity();
	}

	private static final HandlerList handlerList = new HandlerList();

	public static HandlerList getHandlerList() {
		return handlerList;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	private boolean cancelled = false;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
