package org.bukkit.plugin;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public interface PluginLoader {

	public Plugin loadPlugin(File file) throws InvalidPluginException, UnknownDependencyException;

	public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException;

	public Pattern[] getPluginFileFilters();

	public Map<Class<? extends Event>, Set<RegisteredListener>> createRegisteredListeners(Listener listener,
			Plugin plugin);

	public void enablePlugin(Plugin plugin);

	public void disablePlugin(Plugin plugin);
}
