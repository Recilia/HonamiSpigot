package org.bukkit.event.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerTeleportEvent extends PlayerMoveEvent {
	private static final HandlerList handlers = new HandlerList();
	private TeleportCause cause = TeleportCause.UNKNOWN;

	public PlayerTeleportEvent(final Player player, final Location from, final Location to) {
		super(player, from, to);
	}

	public PlayerTeleportEvent(final Player player, final Location from, final Location to, final TeleportCause cause) {
		this(player, from, to);

		this.cause = cause;
	}

	public TeleportCause getCause() {
		return cause;
	}

	public enum TeleportCause {

		ENDER_PEARL,

		COMMAND,

		PLUGIN,

		NETHER_PORTAL,

		END_PORTAL,

		SPECTATE,

		UNKNOWN;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
