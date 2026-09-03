package org.bukkit.event.vehicle;

import org.bukkit.block.Block;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.HandlerList;

public class VehicleBlockCollisionEvent extends VehicleCollisionEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Block block;

	public VehicleBlockCollisionEvent(final Vehicle vehicle, final Block block) {
		super(vehicle);
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
