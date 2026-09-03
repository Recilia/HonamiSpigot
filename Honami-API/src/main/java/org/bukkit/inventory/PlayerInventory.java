package org.bukkit.inventory;

import org.bukkit.entity.HumanEntity;

public interface PlayerInventory extends Inventory {

	public ItemStack[] getArmorContents();

	public ItemStack getHelmet();

	public ItemStack getChestplate();

	public ItemStack getLeggings();

	public ItemStack getBoots();

	@Override
	public void setItem(int index, ItemStack item);

	public void setArmorContents(ItemStack[] items);

	public void setHelmet(ItemStack helmet);

	public void setChestplate(ItemStack chestplate);

	public void setLeggings(ItemStack leggings);

	public void setBoots(ItemStack boots);

	public ItemStack getItemInHand();

	public void setItemInHand(ItemStack stack);

	public int getHeldItemSlot();

	public void setHeldItemSlot(int slot);

	@Deprecated
	public int clear(int id, int data);

	public HumanEntity getHolder();
}
