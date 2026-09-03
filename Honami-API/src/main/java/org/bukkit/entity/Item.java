package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;

public interface Item extends Entity {

	public ItemStack getItemStack();

	public void setItemStack(ItemStack stack);

	public int getPickupDelay();

	public void setPickupDelay(int delay);
}
