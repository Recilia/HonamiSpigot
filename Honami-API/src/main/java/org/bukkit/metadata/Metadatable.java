package org.bukkit.metadata;

import java.util.List;

import org.bukkit.plugin.Plugin;

public interface Metadatable {

	public void setMetadata(String metadataKey, MetadataValue newMetadataValue);

	public List<MetadataValue> getMetadata(String metadataKey);

	public boolean hasMetadata(String metadataKey);

	public void removeMetadata(String metadataKey, Plugin owningPlugin);
}
