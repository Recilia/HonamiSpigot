package org.bukkit.metadata;

import java.util.List;

import org.bukkit.plugin.Plugin;

public interface MetadataStore<T> {

	public void setMetadata(T subject, String metadataKey, MetadataValue newMetadataValue);

	public List<MetadataValue> getMetadata(T subject, String metadataKey);

	public boolean hasMetadata(T subject, String metadataKey);

	public void removeMetadata(T subject, String metadataKey, Plugin owningPlugin);

	public void invalidateAll(Plugin owningPlugin);
}
