package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerExpChangeEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private int exp;

	public PlayerExpChangeEvent(final Player player, final int expAmount) {
		super(player);
		exp = expAmount;
	}

	public int getAmount() {
		return exp;
	}

	public void setAmount(int amount) {
		exp = amount;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
