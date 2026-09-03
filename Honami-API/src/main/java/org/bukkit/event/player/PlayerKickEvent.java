package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private String leaveMessage;
	private String kickReason;
	private Boolean cancel;

	public PlayerKickEvent(final Player playerKicked, final String kickReason, final String leaveMessage) {
		super(playerKicked);
		this.kickReason = kickReason;
		this.leaveMessage = leaveMessage;
		this.cancel = false;
	}

	public String getReason() {
		return kickReason;
	}

	public String getLeaveMessage() {
		return leaveMessage;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public void setReason(String kickReason) {
		this.kickReason = kickReason;
	}

	public void setLeaveMessage(String leaveMessage) {
		this.leaveMessage = leaveMessage;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
