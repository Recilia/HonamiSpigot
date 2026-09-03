package org.bukkit.event.vehicle;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class VehicleDestroyEvent extends VehicleEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Entity attacker;
	private boolean cancelled;

	public VehicleDestroyEvent(final Vehicle vehicle, final Entity attacker) {
		super(vehicle);
		this.attacker = attacker;
	}

	public Entity getAttacker() {
		return attacker;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
