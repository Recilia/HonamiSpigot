package org.bukkit.plugin;

public class InvalidPluginException extends Exception {
	private static final long serialVersionUID = -8242141640709409544L;

	public InvalidPluginException(final Throwable cause) {
		super(cause);
	}

	public InvalidPluginException() {

	}

	public InvalidPluginException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public InvalidPluginException(final String message) {
		super(message);
	}
}
