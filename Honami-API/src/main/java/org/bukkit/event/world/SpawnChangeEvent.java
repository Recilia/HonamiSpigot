package org.bukkit.event.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.HandlerList;

public class SpawnChangeEvent extends WorldEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Location previousLocation;

	public SpawnChangeEvent(final World world, final Location previousLocation) {
		super(world);
		this.previousLocation = previousLocation;
	}

	public Location getPreviousLocation() {
		return previousLocation;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
