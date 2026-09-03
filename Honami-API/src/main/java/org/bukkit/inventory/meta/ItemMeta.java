package org.bukkit.inventory.meta;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;

public interface ItemMeta extends Cloneable, ConfigurationSerializable {

	boolean hasDisplayName();

	String getDisplayName();

	void setDisplayName(String name);

	boolean hasLore();

	List<String> getLore();

	void setLore(List<String> lore);

	boolean hasEnchants();

	boolean hasEnchant(Enchantment ench);

	int getEnchantLevel(Enchantment ench);

	Map<Enchantment, Integer> getEnchants();

	boolean addEnchant(Enchantment ench, int level, boolean ignoreLevelRestriction);

	boolean removeEnchant(Enchantment ench);

	boolean hasConflictingEnchant(Enchantment ench);

	void addItemFlags(ItemFlag... itemFlags);

	void removeItemFlags(ItemFlag... itemFlags);

	Set<ItemFlag> getItemFlags();

	boolean hasItemFlag(ItemFlag flag);

	@SuppressWarnings("javadoc")
	ItemMeta clone();

	public class Spigot {

		public void setUnbreakable(boolean unbreakable) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public boolean isUnbreakable() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	Spigot spigot();
	
}
