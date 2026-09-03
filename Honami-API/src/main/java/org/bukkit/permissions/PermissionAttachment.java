package org.bukkit.permissions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

public class PermissionAttachment {
	private PermissionRemovedExecutor removed;
	private final Map<String, Boolean> permissions = new LinkedHashMap<String, Boolean>();
	private final Permissible permissible;
	private final Plugin plugin;

	public PermissionAttachment(Plugin plugin, Permissible Permissible) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		} else if (!plugin.isEnabled()) {
			throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
		}

		this.permissible = Permissible;
		this.plugin = plugin;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public void setRemovalCallback(PermissionRemovedExecutor ex) {
		removed = ex;
	}

	public PermissionRemovedExecutor getRemovalCallback() {
		return removed;
	}

	public Permissible getPermissible() {
		return permissible;
	}

	public Map<String, Boolean> getPermissions() {
		return new LinkedHashMap<String, Boolean>(permissions);
	}

	public void setPermission(String name, boolean value) {
		permissions.put(name.toLowerCase(), value);
		permissible.recalculatePermissions();
	}

	public void setPermission(Permission perm, boolean value) {
		setPermission(perm.getName(), value);
	}

	public void unsetPermission(String name) {
		permissions.remove(name.toLowerCase());
		permissible.recalculatePermissions();
	}

	public void unsetPermission(Permission perm) {
		unsetPermission(perm.getName());
	}

	public boolean remove() {
		try {
			permissible.removeAttachment(this);
			return true;
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}
}
