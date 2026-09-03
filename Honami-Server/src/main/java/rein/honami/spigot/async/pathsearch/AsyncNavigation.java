package rein.honami.spigot.async.pathsearch;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.EntityType;

import com.google.common.collect.Lists;
import rein.honami.spigot.async.pathsearch.cache.SearchCacheEntry;
import rein.honami.spigot.async.pathsearch.cache.SearchCacheEntryEntity;
import rein.honami.spigot.async.pathsearch.cache.SearchCacheEntryPosition;
import rein.honami.spigot.config.HonamiConfig;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityInsentient;
import net.minecraft.server.Navigation;
import net.minecraft.server.PathEntity;
import net.minecraft.server.World;

public class AsyncNavigation extends Navigation {

	private final List<SearchCacheEntryEntity> searchCache = Lists.newCopyOnWriteArrayList();
	private final List<SearchCacheEntryPosition> positionSearchCache = Lists.newCopyOnWriteArrayList();
	
	public final AtomicBoolean isSearching = new AtomicBoolean(false);
	
	private int ticksSinceCleanup = 0;
	
	private static final List<EntityType> offloadedEntities = Lists.newArrayList();
	private static int minimumDistanceForOffloadingSquared = 0;

	public AsyncNavigation(EntityInsentient var1, World var2) {
		super(var1, var2);
	}
	
	public static void addOffloadedEntities(List<EntityType> entities) {
		offloadedEntities.addAll(entities);
	}
	
	private void issueSearch(Entity targetEntity) {
		SearchHandler.getInstance().issueSearch(targetEntity, this);
	}
	
	private void issueSearch(int x, int y, int z) {
		SearchHandler.getInstance().issueSearch(x, y, z, this);
	}
	
	@Override
	public PathEntity a(Entity targetEntity) {
		
		boolean isTooClose = this.b.h(targetEntity) < minimumDistanceForOffloadingSquared;
		boolean alreadySearching = this.isSearching.get();
		
		if ((!offLoadedSearches(this.getEntity().getBukkitEntity().getType()) || isTooClose) && !alreadySearching) {
			return super.a(targetEntity);
		}
				
		PathEntity finalPath = null;
		
		for (SearchCacheEntryEntity cacheEntry : this.searchCache) {
			if (cacheEntry.getTargetingEntity() == this.getEntity()) {
				finalPath = cacheEntry.getPath();
				
				if (HonamiConfig.ensurePathSearchAccuracy) {

					if (!cacheEntry.isAccurate()) {
						return super.a(targetEntity);
					}
				}
				
				break;
			}
		}
		
		if (finalPath == null && !this.isSearching.get()) {
			this.issueSearch(targetEntity);
		}
		
		return finalPath;
	}
	
	@Override
	public PathEntity a(int x, int y, int z) {
		
		boolean isTooClose = this.b.distanceSquared(x, y, z) < minimumDistanceForOffloadingSquared;
		boolean alreadySearching = this.isSearching.get();
		
		if ((!offLoadedSearches(this.getEntity().getBukkitEntity().getType()) || isTooClose) && !alreadySearching) {
			return super.a(new BlockPosition(x, y, z));
		}
				
		PathEntity finalPath = null;
		
		for (SearchCacheEntryPosition cacheEntry : this.positionSearchCache) {
			if (cacheEntry.getTargetingEntity() == this.getEntity()) {
				finalPath = cacheEntry.getPath();
				
				if (HonamiConfig.ensurePathSearchAccuracy) {

					if (!cacheEntry.isAccurate()) {
						return super.a(new BlockPosition(x, y, z));
					}
				}
				
				break;
			}
		}
		
		if (finalPath == null && !this.isSearching.get()) {
			this.issueSearch(x, y, z);
		}
		
		return finalPath;
	}
	
	@Override
	public PathEntity a(BlockPosition blockposition) {
		return a(blockposition.getX(), blockposition.getY(), blockposition.getZ());
	}
	
	public void addEntry(SearchCacheEntry cacheEntry) {
		if (cacheEntry instanceof SearchCacheEntryEntity) {
			this.searchCache.add((SearchCacheEntryEntity) cacheEntry);
		} else {
			this.positionSearchCache.add((SearchCacheEntryPosition) cacheEntry);
		}
	}
	
	@Override
	public void cleanUpExpiredSearches() {
		this.ticksSinceCleanup++;
		if (this.ticksSinceCleanup == 150) {
			this.ticksSinceCleanup = 0;
			
			this.searchCache.clear();
			this.positionSearchCache.clear();
		}
	}

	private static boolean offLoadedSearches(EntityType type) {
		if (HonamiConfig.asyncPathSearches) {
			return offloadedEntities.contains(type);
		} else {
			return false;
		}
	}

	public static void setMinimumDistanceForOffloading(int distanceToAsync) {
		minimumDistanceForOffloadingSquared = distanceToAsync;
	}	
}
