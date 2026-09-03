package org.bukkit.event.server;

import org.bukkit.plugin.Plugin;

public abstract class PluginEvent extends ServerEvent {
	private final Plugin plugin;

	public PluginEvent(final Plugin plugin) {
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return plugin;
	}
}
