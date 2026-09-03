package org.bukkit.inventory.meta;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;

public interface LeatherArmorMeta extends ItemMeta {

	Color getColor();

	void setColor(Color color);

	LeatherArmorMeta clone();
}
