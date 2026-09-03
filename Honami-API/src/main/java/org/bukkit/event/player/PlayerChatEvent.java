package org.bukkit.event.player;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

@Deprecated
@Warning(reason = "Listening to this event forces chat to wait for the main thread, delaying chat messages.")
public class PlayerChatEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private String message;
	private String format;
	private final Set<Player> recipients;

	public PlayerChatEvent(final Player player, final String message) {
		super(player);
		this.message = message;
		this.format = "<%1$s> %2$s";
		this.recipients = new HashSet<Player>(player.getServer().getOnlinePlayers());
	}

	public PlayerChatEvent(final Player player, final String message, final String format,
			final Set<Player> recipients) {
		super(player);
		this.message = message;
		this.format = format;
		this.recipients = recipients;
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

	public void setMessage(String message) {
		this.message = message;
	}

	public void setPlayer(final Player player) {
		Validate.notNull(player, "Player cannot be null");
		this.player = player;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(final String format) {
		
		try {
			String.format(format, player, message);
		} catch (RuntimeException ex) {
			ex.fillInStackTrace();
			throw ex;
		}

		this.format = format;
	}

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
