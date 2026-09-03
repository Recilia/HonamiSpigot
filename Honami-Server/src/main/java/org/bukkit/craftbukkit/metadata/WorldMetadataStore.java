package org.bukkit.craftbukkit.metadata;

import org.bukkit.World;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;

public class WorldMetadataStore extends MetadataStoreBase<World> implements MetadataStore<World> {

	@Override
	protected String disambiguate(World world, String metadataKey) {
		return world.getUID().toString() + ":" + metadataKey;
	}
}
