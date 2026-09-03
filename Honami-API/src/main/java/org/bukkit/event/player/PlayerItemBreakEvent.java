package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerItemBreakEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final ItemStack brokenItem;

	public PlayerItemBreakEvent(final Player player, final ItemStack brokenItem) {
		super(player);
		this.brokenItem = brokenItem;
	}

	public ItemStack getBrokenItem() {
		return brokenItem;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
