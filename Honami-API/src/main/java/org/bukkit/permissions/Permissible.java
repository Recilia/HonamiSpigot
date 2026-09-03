package org.bukkit.permissions;

import java.util.Set;

import org.bukkit.plugin.Plugin;

public interface Permissible extends ServerOperator {

	public boolean isPermissionSet(String name);

	public boolean isPermissionSet(Permission perm);

	public boolean hasPermission(String name);

	public boolean hasPermission(Permission perm);

	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value);

	public PermissionAttachment addAttachment(Plugin plugin);

	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks);

	public PermissionAttachment addAttachment(Plugin plugin, int ticks);

	public void removeAttachment(PermissionAttachment attachment);

	public void recalculatePermissions();

	public Set<PermissionAttachmentInfo> getEffectivePermissions();
}
