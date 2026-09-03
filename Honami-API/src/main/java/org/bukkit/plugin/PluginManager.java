package org.bukkit.plugin;

import java.io.File;
import java.util.Set;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

public interface PluginManager {

	public void registerInterface(Class<? extends PluginLoader> loader) throws IllegalArgumentException;

	public Plugin getPlugin(String name);

	public Plugin[] getPlugins();

	public boolean isPluginEnabled(String name);

	public boolean isPluginEnabled(Plugin plugin);

	public Plugin loadPlugin(File file)
			throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException;

	public Plugin[] loadPlugins(File directory);

	public void disablePlugins();

	public void clearPlugins();

	public void callEvent(Event event) throws IllegalStateException;

	public void registerEvents(Listener listener, Plugin plugin);

	public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority,
			EventExecutor executor, Plugin plugin);

	public void registerEvent(Class<? extends Event> event, Listener listener, EventPriority priority,
			EventExecutor executor, Plugin plugin, boolean ignoreCancelled);

	public void enablePlugin(Plugin plugin);

	public void disablePlugin(Plugin plugin);

	public Permission getPermission(String name);

	public void addPermission(Permission perm);

	public void removePermission(Permission perm);

	public void removePermission(String name);

	public Set<Permission> getDefaultPermissions(boolean op);

	public void recalculatePermissionDefaults(Permission perm);

	public void subscribeToPermission(String permission, Permissible permissible);

	public void unsubscribeFromPermission(String permission, Permissible permissible);

	public Set<Permissible> getPermissionSubscriptions(String permission);

	public void subscribeToDefaultPerms(boolean op, Permissible permissible);

	public void unsubscribeFromDefaultPerms(boolean op, Permissible permissible);

	public Set<Permissible> getDefaultPermSubscriptions(boolean op);

	public Set<Permission> getPermissions();

	public boolean useTimings();
}
