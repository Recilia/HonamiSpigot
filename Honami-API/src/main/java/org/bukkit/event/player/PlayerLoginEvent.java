package org.bukkit.event.player;

import java.net.InetAddress;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerLoginEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final InetAddress address;
	private final String hostname;
	private Result result = Result.ALLOWED;
	private String message = "";
	private final InetAddress realAddress; 

	@Deprecated
	public PlayerLoginEvent(final Player player) {
		this(player, "", null);
	}

	@Deprecated
	public PlayerLoginEvent(final Player player, final String hostname) {
		this(player, hostname, null);
	}

	public PlayerLoginEvent(final Player player, final String hostname, final InetAddress address,
			final InetAddress realAddress) { 
		super(player);
		this.hostname = hostname;
		this.address = address;
		
		this.realAddress = realAddress;
	}

	public PlayerLoginEvent(final Player player, final String hostname, final InetAddress address) {
		this(player, hostname, address, address);
		
	}

	@Deprecated
	public PlayerLoginEvent(final Player player, final Result result, final String message) {
		this(player, "", null, result, message, null); 
	}

	public PlayerLoginEvent(final Player player, String hostname, final InetAddress address, final Result result,
			final String message, final InetAddress realAddress) { 
		this(player, hostname, address, realAddress); 
		this.result = result;
		this.message = message;
	}

	

	public InetAddress getRealAddress() {
		return realAddress;
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

	public String getHostname() {
		return hostname;
	}

	public void allow() {
		result = Result.ALLOWED;
		message = "";
	}

	public void disallow(final Result result, final String message) {
		this.result = result;
		this.message = message;
	}

	public InetAddress getAddress() {
		return address;
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

		KICK_OTHER
	}
}
