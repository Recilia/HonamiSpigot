package org.bukkit.event.server;

import org.bukkit.event.HandlerList;
import org.bukkit.map.MapView;

public class MapInitializeEvent extends ServerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final MapView mapView;

	public MapInitializeEvent(final MapView mapView) {
		this.mapView = mapView;
	}

	public MapView getMap() {
		return mapView;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
