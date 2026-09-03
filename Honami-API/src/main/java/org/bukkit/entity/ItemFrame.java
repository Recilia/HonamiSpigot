package org.bukkit.entity;

import org.bukkit.Rotation;
import org.bukkit.inventory.ItemStack;

public interface ItemFrame extends Hanging {

	public ItemStack getItem();

	public void setItem(ItemStack item);

	public Rotation getRotation();

	public void setRotation(Rotation rotation) throws IllegalArgumentException;
}
