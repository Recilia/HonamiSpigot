package com.destroystokyo.paper.event.server;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ServerTickStartEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private final int tickNumber;

	public ServerTickStartEvent(int tickNumber) {
		this.tickNumber = tickNumber;
	}

	public int getTickNumber() {
		return tickNumber;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
