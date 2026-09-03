package org.bukkit.event.vehicle;

import org.bukkit.Location;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

public class VehicleMoveEvent extends VehicleEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Location from;
	private final Location to;

	public VehicleMoveEvent(final Vehicle vehicle, final Location from, final Location to) {
		super(vehicle);

		this.from = from;
		this.to = to;
	}

	public Location getFrom() {
		return from;
	}

	public Location getTo() {
		return to;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
