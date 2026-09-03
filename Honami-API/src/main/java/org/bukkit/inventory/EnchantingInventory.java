package org.bukkit.inventory;

public interface EnchantingInventory extends Inventory {

	void setItem(ItemStack item);

	ItemStack getItem();

	void setSecondary(ItemStack item);

	ItemStack getSecondary();
}
