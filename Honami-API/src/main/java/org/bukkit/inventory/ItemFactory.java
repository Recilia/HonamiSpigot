package org.bukkit.inventory;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public interface ItemFactory {

	ItemMeta getItemMeta(final Material material);

	boolean isApplicable(final ItemMeta meta, final ItemStack stack) throws IllegalArgumentException;

	boolean isApplicable(final ItemMeta meta, final Material material) throws IllegalArgumentException;

	boolean equals(final ItemMeta meta1, final ItemMeta meta2) throws IllegalArgumentException;

	ItemMeta asMetaFor(final ItemMeta meta, final ItemStack stack) throws IllegalArgumentException;

	ItemMeta asMetaFor(final ItemMeta meta, final Material material) throws IllegalArgumentException;

	Color getDefaultLeatherColor();
}
