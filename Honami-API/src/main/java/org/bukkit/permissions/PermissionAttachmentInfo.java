package org.bukkit.permissions;

public class PermissionAttachmentInfo {
	private final Permissible permissible;
	private final String permission;
	private final PermissionAttachment attachment;
	private final boolean value;

	public PermissionAttachmentInfo(Permissible permissible, String permission, PermissionAttachment attachment,
			boolean value) {
		if (permissible == null) {
			throw new IllegalArgumentException("Permissible may not be null");
		} else if (permission == null) {
			throw new IllegalArgumentException("Permissions may not be null");
		}

		this.permissible = permissible;
		this.permission = permission;
		this.attachment = attachment;
		this.value = value;
	}

	public Permissible getPermissible() {
		return permissible;
	}

	public String getPermission() {
		return permission;
	}

	public PermissionAttachment getAttachment() {
		return attachment;
	}

	public boolean getValue() {
		return value;
	}
}
