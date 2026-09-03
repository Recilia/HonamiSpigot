package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerQuitEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private String quitMessage;

	public PlayerQuitEvent(final Player who, final String quitMessage) {
		super(who);
		this.quitMessage = quitMessage;
	}

	public String getQuitMessage() {
		return quitMessage;
	}

	public void setQuitMessage(String quitMessage) {
		this.quitMessage = quitMessage;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
