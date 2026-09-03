package com.destroystokyo.paper.event.player;

import java.util.UUID;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerHandshakeEvent extends Event implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();
	private final String originalHandshake;
	private boolean cancelled;
	private String serverHostname;
	private String socketAddressHostname;
	private UUID uniqueId;
	private String propertiesJson;
	private boolean failed;
	private String failMessage = "If you wish to use IP forwarding, please enable it in your BungeeCord config as well!";

	public PlayerHandshakeEvent(String originalHandshake, boolean cancelled) {
		this.originalHandshake = originalHandshake;
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getOriginalHandshake() {
		return this.originalHandshake;
	}

	public String getServerHostname() {
		return this.serverHostname;
	}

	public void setServerHostname(String serverHostname) {
		this.serverHostname = serverHostname;
	}

	public String getSocketAddressHostname() {
		return this.socketAddressHostname;
	}

	public void setSocketAddressHostname(String socketAddressHostname) {
		this.socketAddressHostname = socketAddressHostname;
	}

	public UUID getUniqueId() {
		return this.uniqueId;
	}

	public void setUniqueId(UUID uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getPropertiesJson() {
		return this.propertiesJson;
	}

	public boolean isFailed() {
		return this.failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public void setPropertiesJson(String propertiesJson) {
		this.propertiesJson = propertiesJson;
	}

	public String getFailMessage() {
		return this.failMessage;
	}

	public void setFailMessage(String failMessage) {
		Validate.notEmpty(failMessage, "fail message cannot be null or empty");
		this.failMessage = failMessage;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}
