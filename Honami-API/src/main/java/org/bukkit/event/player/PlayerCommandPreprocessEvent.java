package org.bukkit.event.player;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerCommandPreprocessEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private String message;
	private String format = "<%1$s> %2$s";
	private final Set<Player> recipients;

	public PlayerCommandPreprocessEvent(final Player player, final String message) {
		super(player);
		this.recipients = new HashSet<Player>(player.getServer().getOnlinePlayers());
		this.message = message;
	}

	public PlayerCommandPreprocessEvent(final Player player, final String message, final Set<Player> recipients) {
		super(player);
		this.recipients = recipients;
		this.message = message;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String command) throws IllegalArgumentException {
		Validate.notNull(command, "Command cannot be null");
		Validate.notEmpty(command, "Command cannot be empty");
		this.message = command;
	}

	public void setPlayer(final Player player) throws IllegalArgumentException {
		Validate.notNull(player, "Player cannot be null");
		this.player = player;
	}

	@Deprecated
	public String getFormat() {
		return format;
	}

	@Deprecated
	public void setFormat(final String format) {
		
		try {
			String.format(format, player, message);
		} catch (RuntimeException ex) {
			ex.fillInStackTrace();
			throw ex;
		}

		this.format = format;
	}

	@Deprecated
	public Set<Player> getRecipients() {
		return recipients;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
