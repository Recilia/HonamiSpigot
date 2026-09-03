package org.bukkit.permissions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Permission {
	public static final PermissionDefault DEFAULT_PERMISSION = PermissionDefault.OP;

	private final String name;
	private final Map<String, Boolean> children = new LinkedHashMap<String, Boolean>();
	private PermissionDefault defaultValue = DEFAULT_PERMISSION;
	private String description;

	public Permission(String name) {
		this(name, null, null, null);
	}

	public Permission(String name, String description) {
		this(name, description, null, null);
	}

	public Permission(String name, PermissionDefault defaultValue) {
		this(name, null, defaultValue, null);
	}

	public Permission(String name, String description, PermissionDefault defaultValue) {
		this(name, description, defaultValue, null);
	}

	public Permission(String name, Map<String, Boolean> children) {
		this(name, null, null, children);
	}

	public Permission(String name, String description, Map<String, Boolean> children) {
		this(name, description, null, children);
	}

	public Permission(String name, PermissionDefault defaultValue, Map<String, Boolean> children) {
		this(name, null, defaultValue, children);
	}

	public Permission(String name, String description, PermissionDefault defaultValue, Map<String, Boolean> children) {
		Validate.notNull(name, "Name cannot be null");
		this.name = name;
		this.description = (description == null) ? "" : description;

		if (defaultValue != null) {
			this.defaultValue = defaultValue;
		}

		if (children != null) {
			this.children.putAll(children);
		}

		recalculatePermissibles();
	}

	public String getName() {
		return name;
	}

	public Map<String, Boolean> getChildren() {
		return children;
	}

	public PermissionDefault getDefault() {
		return defaultValue;
	}

	public void setDefault(PermissionDefault value) {
		if (defaultValue == null) {
			throw new IllegalArgumentException("Default value cannot be null");
		}

		defaultValue = value;
		recalculatePermissibles();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String value) {
		if (value == null) {
			description = "";
		} else {
			description = value;
		}
	}

	public Set<Permissible> getPermissibles() {
		return Bukkit.getServer().getPluginManager().getPermissionSubscriptions(name);
	}

	public void recalculatePermissibles() {
		Set<Permissible> perms = getPermissibles();

		Bukkit.getServer().getPluginManager().recalculatePermissionDefaults(this);

		for (Permissible p : perms) {
			p.recalculatePermissions();
		}
	}

	public Permission addParent(String name, boolean value) {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		String lname = name.toLowerCase();

		Permission perm = pm.getPermission(lname);

		if (perm == null) {
			perm = new Permission(lname);
			pm.addPermission(perm);
		}

		addParent(perm, value);

		return perm;
	}

	public void addParent(Permission perm, boolean value) {
		perm.getChildren().put(getName(), value);
		perm.recalculatePermissibles();
	}

	public static List<Permission> loadPermissions(Map<?, ?> data, String error, PermissionDefault def) {
		List<Permission> result = new ArrayList<Permission>();

		for (Map.Entry<?, ?> entry : data.entrySet()) {
			try {
				result.add(Permission.loadPermission(entry.getKey().toString(), (Map<?, ?>) entry.getValue(), def,
						result));
			} catch (Throwable ex) {
				Bukkit.getServer().getLogger().log(Level.SEVERE, String.format(error, entry.getKey()), ex);
			}
		}

		return result;
	}

	public static Permission loadPermission(String name, Map<String, Object> data) {
		return loadPermission(name, data, DEFAULT_PERMISSION, null);
	}

	public static Permission loadPermission(String name, Map<?, ?> data, PermissionDefault def,
			List<Permission> output) {
		Validate.notNull(name, "Name cannot be null");
		Validate.notNull(data, "Data cannot be null");

		String desc = null;
		Map<String, Boolean> children = null;

		if (data.get("default") != null) {
			PermissionDefault value = PermissionDefault.getByName(data.get("default").toString());
			if (value != null) {
				def = value;
			} else {
				throw new IllegalArgumentException("'default' key contained unknown value");
			}
		}

		if (data.get("children") != null) {
			Object childrenNode = data.get("children");
			if (childrenNode instanceof Iterable) {
				children = new LinkedHashMap<String, Boolean>();
				for (Object child : (Iterable<?>) childrenNode) {
					if (child != null) {
						children.put(child.toString(), Boolean.TRUE);
					}
				}
			} else if (childrenNode instanceof Map) {
				children = extractChildren((Map<?, ?>) childrenNode, name, def, output);
			} else {
				throw new IllegalArgumentException("'children' key is of wrong type");
			}
		}

		if (data.get("description") != null) {
			desc = data.get("description").toString();
		}

		return new Permission(name, desc, def, children);
	}

	private static Map<String, Boolean> extractChildren(Map<?, ?> input, String name, PermissionDefault def,
			List<Permission> output) {
		Map<String, Boolean> children = new LinkedHashMap<String, Boolean>();

		for (Map.Entry<?, ?> entry : input.entrySet()) {
			if ((entry.getValue() instanceof Boolean)) {
				children.put(entry.getKey().toString(), (Boolean) entry.getValue());
			} else if ((entry.getValue() instanceof Map)) {
				try {
					Permission perm = loadPermission(entry.getKey().toString(), (Map<?, ?>) entry.getValue(), def,
							output);
					children.put(perm.getName(), Boolean.TRUE);

					if (output != null) {
						output.add(perm);
					}
				} catch (Throwable ex) {
					throw new IllegalArgumentException(
							"Permission node '" + entry.getKey().toString() + "' in child of " + name + " is invalid",
							ex);
				}
			} else {
				throw new IllegalArgumentException("Child '" + entry.getKey().toString() + "' contains invalid value");
			}
		}

		return children;
	}
}
