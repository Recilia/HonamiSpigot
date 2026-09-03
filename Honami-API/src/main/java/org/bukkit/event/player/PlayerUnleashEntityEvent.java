package org.bukkit.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.entity.EntityUnleashEvent;

public class PlayerUnleashEntityEvent extends EntityUnleashEvent implements Cancellable {
	private final Player player;
	private boolean cancelled = false;

	public PlayerUnleashEntityEvent(Entity entity, Player player) {
		super(entity, UnleashReason.PLAYER_UNLEASH);
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
