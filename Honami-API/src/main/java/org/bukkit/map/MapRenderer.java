package org.bukkit.map;

import org.bukkit.entity.Player;

public abstract class MapRenderer {

	private boolean contextual;

	public MapRenderer() {
		this(false);
	}

	public MapRenderer(boolean contextual) {
		this.contextual = contextual;
	}

	final public boolean isContextual() {
		return contextual;
	}

	public void initialize(MapView map) {
	}

	abstract public void render(MapView map, MapCanvas canvas, Player player);

}
