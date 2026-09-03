package org.bukkit.inventory;

import org.bukkit.entity.Entity;

public interface EntityEquipment {

	ItemStack getItemInHand();

	void setItemInHand(ItemStack stack);

	ItemStack getHelmet();

	void setHelmet(ItemStack helmet);

	ItemStack getChestplate();

	void setChestplate(ItemStack chestplate);

	ItemStack getLeggings();

	void setLeggings(ItemStack leggings);

	ItemStack getBoots();

	void setBoots(ItemStack boots);

	ItemStack[] getArmorContents();

	void setArmorContents(ItemStack[] items);

	void clear();

	float getItemInHandDropChance();

	void setItemInHandDropChance(float chance);

	float getHelmetDropChance();

	void setHelmetDropChance(float chance);

	float getChestplateDropChance();

	void setChestplateDropChance(float chance);

	float getLeggingsDropChance();

	void setLeggingsDropChance(float chance);

	float getBootsDropChance();

	void setBootsDropChance(float chance);

	Entity getHolder();
}
