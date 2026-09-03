package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.Warning;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Deprecated
@Warning(reason = "This event causes a login thread to synchronize with the main thread")
public class PlayerPreLoginEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Result result;
	private String message;
	private final String name;
	private final InetAddress ipAddress;
	private final UUID uniqueId;

	@Deprecated
	public PlayerPreLoginEvent(final String name, final InetAddress ipAddress) {
		this(name, ipAddress, null);
	}

	public PlayerPreLoginEvent(final String name, final InetAddress ipAddress, final UUID uniqueId) {
		this.result = Result.ALLOWED;
		this.message = "";
		this.name = name;
		this.ipAddress = ipAddress;
		this.uniqueId = uniqueId;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(final Result result) {
		this.result = result;
	}

	public String getKickMessage() {
		return message;
	}

	public void setKickMessage(final String message) {
		this.message = message;
	}

	public void allow() {
		result = Result.ALLOWED;
		message = "";
	}

	public void disallow(final Result result, final String message) {
		this.result = result;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public InetAddress getAddress() {
		return ipAddress;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum Result {

		ALLOWED,

		KICK_FULL,

		KICK_BANNED,

		KICK_WHITELIST,

		KICK_OTHER
	}
}
