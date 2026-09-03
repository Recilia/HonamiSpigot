package rein.honami.spigot.async.pathsearch.cache;

import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.PathEntity;

public class SearchCacheEntryEntity extends SearchCacheEntry {
	
	private final Entity target;
	
	public SearchCacheEntryEntity(Entity target, EntityInsentient targetingEntity, PathEntity path) {
		super(targetingEntity, path);
		this.target = target;
	}

	public Entity getTarget() {
		return target;
	}
}
