package org.bukkit.inventory;

public interface CraftingInventory extends Inventory {

	ItemStack getResult();

	ItemStack[] getMatrix();

	void setResult(ItemStack newResult);

	void setMatrix(ItemStack[] contents);

	Recipe getRecipe();
}