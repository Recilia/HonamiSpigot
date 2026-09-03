package org.bukkit.plugin;

@SuppressWarnings("serial")
public class IllegalPluginAccessException extends RuntimeException {

	public IllegalPluginAccessException() {
	}

	public IllegalPluginAccessException(String msg) {
		super(msg);
	}
}
