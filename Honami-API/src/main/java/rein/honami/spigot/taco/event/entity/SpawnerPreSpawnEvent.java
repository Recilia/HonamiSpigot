package rein.honami.spigot.taco.event.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SpawnerPreSpawnEvent extends Event implements Cancellable {

	private final Location location;
	private final EntityType spawnedType;

	public SpawnerPreSpawnEvent(Location location, EntityType spawnedType) {
		this.location = checkNotNull(location, "Null location").clone(); 
		this.spawnedType = checkNotNull(spawnedType, "Null spawned type");
	}

	public Location getLocation() {
		return location.clone(); 
	}

	public EntityType getSpawnedType() {
		return spawnedType;
	}

	private boolean cancelled;

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	private static final HandlerList handlerList = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

	public static HandlerList getHandlerList() {
		return handlerList;
	}
}
