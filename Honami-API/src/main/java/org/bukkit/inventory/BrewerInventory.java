package org.bukkit.inventory;

import org.bukkit.block.BrewingStand;

public interface BrewerInventory extends Inventory {

	ItemStack getIngredient();

	void setIngredient(ItemStack ingredient);

	BrewingStand getHolder();
}
