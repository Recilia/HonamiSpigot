package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

@Deprecated
public class PlayerInventoryEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	protected Inventory inventory;

	public PlayerInventoryEvent(final Player player, final Inventory inventory) {
		super(player);
		this.inventory = inventory;
	}

	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
