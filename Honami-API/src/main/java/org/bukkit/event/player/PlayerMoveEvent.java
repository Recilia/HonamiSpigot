package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

import com.google.common.base.Preconditions;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Location from;
	private Location to;

	public PlayerMoveEvent(final Player player, final Location from, final Location to) {
		super(player);
		this.from = from;
		this.to = to;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public Location getFrom() {
		return from;
	}

	public void setFrom(Location from) {
		validateLocation(from);
		this.from = from;
	}

	public Location getTo() {
		return to;
	}

	public void setTo(Location to) {
		validateLocation(to);
		this.to = to;
	}

	private void validateLocation(Location loc) {
		Preconditions.checkArgument(loc != null, "Cannot use null location!");
		Preconditions.checkArgument(loc.getWorld() != null, "Cannot use null location with null world!");
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
