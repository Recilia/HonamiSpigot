package org.bukkit.enchantments;

import org.bukkit.inventory.ItemStack;

public class EnchantmentWrapper extends Enchantment {
	public EnchantmentWrapper(int id) {
		super(id);
	}

	public Enchantment getEnchantment() {
		return Enchantment.getById(getId());
	}

	@Override
	public int getMaxLevel() {
		return getEnchantment().getMaxLevel();
	}

	@Override
	public int getStartLevel() {
		return getEnchantment().getStartLevel();
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return getEnchantment().getItemTarget();
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return getEnchantment().canEnchantItem(item);
	}

	@Override
	public String getName() {
		return getEnchantment().getName();
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return getEnchantment().conflictsWith(other);
	}
}
