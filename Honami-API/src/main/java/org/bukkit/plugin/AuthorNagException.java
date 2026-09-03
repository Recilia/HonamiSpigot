package org.bukkit.plugin;

@SuppressWarnings("serial")
public class AuthorNagException extends RuntimeException {
	private final String message;

	public AuthorNagException(final String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
