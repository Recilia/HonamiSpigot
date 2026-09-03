package org.bukkit.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.Recipe;

public class PrepareItemCraftEvent extends InventoryEvent {
	private static final HandlerList handlers = new HandlerList();
	private boolean repair;
	private CraftingInventory matrix;

	public PrepareItemCraftEvent(CraftingInventory what, InventoryView view, boolean isRepair) {
		super(view);
		this.matrix = what;
		this.repair = isRepair;
	}

	public Recipe getRecipe() {
		return matrix.getRecipe();
	}

	@Override
	public CraftingInventory getInventory() {
		return matrix;
	}

	public boolean isRepair() {
		return repair;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
