package org.bukkit.inventory.meta;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public interface EnchantmentStorageMeta extends ItemMeta {

	boolean hasStoredEnchants();

	boolean hasStoredEnchant(Enchantment ench);

	int getStoredEnchantLevel(Enchantment ench);

	Map<Enchantment, Integer> getStoredEnchants();

	boolean addStoredEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction);

	boolean removeStoredEnchant(Enchantment ench) throws IllegalArgumentException;

	boolean hasConflictingStoredEnchant(Enchantment ench);

	EnchantmentStorageMeta clone();
}
