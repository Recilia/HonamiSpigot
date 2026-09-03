package org.bukkit.event.player;

import java.net.InetAddress;
import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AsyncPlayerPreLoginEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private Result result;
	private String message;
	private final String name;
	private final InetAddress ipAddress;
	private final UUID uniqueId;

	@Deprecated
	public AsyncPlayerPreLoginEvent(final String name, final InetAddress ipAddress) {
		this(name, ipAddress, null);
	}

	public AsyncPlayerPreLoginEvent(final String name, final InetAddress ipAddress, final UUID uniqueId) {
		super(true);
		this.result = Result.ALLOWED;
		this.message = "";
		this.name = name;
		this.ipAddress = ipAddress;
		this.uniqueId = uniqueId;
	}

	public Result getLoginResult() {
		return result;
	}

	@Deprecated
	public PlayerPreLoginEvent.Result getResult() {
		return result == null ? null : result.old();
	}

	public void setLoginResult(final Result result) {
		this.result = result;
	}

	@Deprecated
	public void setResult(final PlayerPreLoginEvent.Result result) {
		this.result = result == null ? null : Result.valueOf(result.name());
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

	@Deprecated
	public void disallow(final PlayerPreLoginEvent.Result result, final String message) {
		this.result = result == null ? null : Result.valueOf(result.name());
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public InetAddress getAddress() {
		return ipAddress;
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum Result {

		ALLOWED,

		KICK_FULL,

		KICK_BANNED,

		KICK_WHITELIST,

		KICK_OTHER;

		@Deprecated
		private PlayerPreLoginEvent.Result old() {
			return PlayerPreLoginEvent.Result.valueOf(name());
		}
	}
}
