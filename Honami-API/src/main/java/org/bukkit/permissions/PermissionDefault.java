package org.bukkit.permissions;

import java.util.HashMap;
import java.util.Map;

public enum PermissionDefault {
	TRUE("true"), FALSE("false"), OP("op", "isop", "operator", "isoperator", "admin", "isadmin"),
	NOT_OP("!op", "notop", "!operator", "notoperator", "!admin", "notadmin");

	private final String[] names;
	private final static Map<String, PermissionDefault> lookup = new HashMap<String, PermissionDefault>();

	private PermissionDefault(String... names) {
		this.names = names;
	}

	public boolean getValue(boolean op) {
		switch (this) {
		case TRUE:
			return true;
		case FALSE:
			return false;
		case OP:
			return op;
		case NOT_OP:
			return !op;
		default:
			return false;
		}
	}

	public static PermissionDefault getByName(String name) {
		return lookup.get(name.toLowerCase().replaceAll("[^a-z!]", ""));
	}

	@Override
	public String toString() {
		return names[0];
	}

	static {
		for (PermissionDefault value : values()) {
			for (String name : value.names) {
				lookup.put(name, value);
			}
		}
	}
}
