package org.bukkit.event.weather;

import org.bukkit.World;
import org.bukkit.event.Event;

public abstract class WeatherEvent extends Event {
	protected World world;

	public WeatherEvent(final World where) {
		world = where;
	}

	public final World getWorld() {
		return world;
	}
}
