package org.bukkit.event.player;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerChatTabCompleteEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final String message;
	private final String lastToken;
	private final Collection<String> completions;

	public PlayerChatTabCompleteEvent(final Player who, final String message, final Collection<String> completions) {
		super(who);
		Validate.notNull(message, "Message cannot be null");
		Validate.notNull(completions, "Completions cannot be null");
		this.message = message;
		int i = message.lastIndexOf(' ');
		if (i < 0) {
			this.lastToken = message;
		} else {
			this.lastToken = message.substring(i + 1);
		}
		this.completions = completions;
	}

	public String getChatMessage() {
		return message;
	}

	public String getLastToken() {
		return lastToken;
	}

	public Collection<String> getTabCompletions() {
		return completions;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
