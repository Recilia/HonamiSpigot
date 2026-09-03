package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerFishEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Entity entity;
	private boolean cancel = false;
	private int exp;
	private final State state;
	private final Fish hookEntity;

	@Deprecated
	public PlayerFishEvent(final Player player, final Entity entity, final State state) {
		this(player, entity, null, state);
	}

	public PlayerFishEvent(final Player player, final Entity entity, final Fish hookEntity, final State state) {
		super(player);
		this.entity = entity;
		this.hookEntity = hookEntity;
		this.state = state;
	}

	public Entity getCaught() {
		return entity;
	}

	public Fish getHook() {
		return hookEntity;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public int getExpToDrop() {
		return exp;
	}

	public void setExpToDrop(int amount) {
		exp = amount;
	}

	public State getState() {
		return state;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum State {

		FISHING,

		CAUGHT_FISH,

		CAUGHT_ENTITY,

		IN_GROUND,

		FAILED_ATTEMPT,
	}
}
