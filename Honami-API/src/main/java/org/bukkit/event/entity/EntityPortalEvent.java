package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.TravelAgent;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class EntityPortalEvent extends EntityTeleportEvent {
	private static final HandlerList handlers = new HandlerList();
	protected boolean useTravelAgent = true;
	protected TravelAgent travelAgent;

	public EntityPortalEvent(final Entity entity, final Location from, final Location to, final TravelAgent pta) {
		super(entity, from, to);
		this.travelAgent = pta;
	}

	public void useTravelAgent(boolean useTravelAgent) {
		this.useTravelAgent = useTravelAgent;
	}

	public boolean useTravelAgent() {
		return useTravelAgent;
	}

	public TravelAgent getPortalTravelAgent() {
		return this.travelAgent;
	}

	public void setPortalTravelAgent(TravelAgent travelAgent) {
		this.travelAgent = travelAgent;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}