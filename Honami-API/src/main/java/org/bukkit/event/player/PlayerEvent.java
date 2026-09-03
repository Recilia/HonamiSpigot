package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public abstract class PlayerEvent extends Event {
	protected Player player;

	public PlayerEvent(final Player who) {
		player = who;
	}

	public PlayerEvent(final Player who, boolean async) {
		super(async);
		player = who;

	}

	public final Player getPlayer() {
		return player;
	}
}
