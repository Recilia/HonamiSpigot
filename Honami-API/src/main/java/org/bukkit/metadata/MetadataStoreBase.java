package org.bukkit.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

public abstract class MetadataStoreBase<T> {
	private Map<String, Map<Plugin, MetadataValue>> metadataMap = new HashMap<String, Map<Plugin, MetadataValue>>();

	public synchronized void setMetadata(T subject, String metadataKey, MetadataValue newMetadataValue) {
		Validate.notNull(newMetadataValue, "Value cannot be null");
		Plugin owningPlugin = newMetadataValue.getOwningPlugin();
		Validate.notNull(owningPlugin, "Plugin cannot be null");
		String key = disambiguate(subject, metadataKey);
		Map<Plugin, MetadataValue> entry = metadataMap.get(key);
		if (entry == null) {
			entry = new WeakHashMap<Plugin, MetadataValue>(1);
			metadataMap.put(key, entry);
		}
		entry.put(owningPlugin, newMetadataValue);
	}

	public synchronized List<MetadataValue> getMetadata(T subject, String metadataKey) {
		String key = disambiguate(subject, metadataKey);
		if (metadataMap.containsKey(key)) {
			Collection<MetadataValue> values = metadataMap.get(key).values();
			return Collections.unmodifiableList(new ArrayList<MetadataValue>(values));
		} else {
			return Collections.emptyList();
		}
	}

	public synchronized boolean hasMetadata(T subject, String metadataKey) {
		String key = disambiguate(subject, metadataKey);
		return metadataMap.containsKey(key);
	}

	public synchronized void removeMetadata(T subject, String metadataKey, Plugin owningPlugin) {
		Validate.notNull(owningPlugin, "Plugin cannot be null");
		String key = disambiguate(subject, metadataKey);
		Map<Plugin, MetadataValue> entry = metadataMap.get(key);
		if (entry == null) {
			return;
		}

		entry.remove(owningPlugin);
		if (entry.isEmpty()) {
			metadataMap.remove(key);
		}
	}

	public synchronized void invalidateAll(Plugin owningPlugin) {
		Validate.notNull(owningPlugin, "Plugin cannot be null");
		for (Map<Plugin, MetadataValue> values : metadataMap.values()) {
			if (values.containsKey(owningPlugin)) {
				values.get(owningPlugin).invalidate();
			}
		}
	}

	protected abstract String disambiguate(T subject, String metadataKey);
}
