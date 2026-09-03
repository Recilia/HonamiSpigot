package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerAnimationEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final PlayerAnimationType animationType;
	private boolean isCancelled = false;

	public PlayerAnimationEvent(final Player player) {
		super(player);

		animationType = PlayerAnimationType.ARM_SWING;
	}

	public PlayerAnimationType getAnimationType() {
		return animationType;
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
