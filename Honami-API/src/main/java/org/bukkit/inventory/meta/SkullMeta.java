package org.bukkit.inventory.meta;

import org.bukkit.Material;

public interface SkullMeta extends ItemMeta {

	String getOwner();

	boolean hasOwner();

	boolean setOwner(String owner);

	SkullMeta clone();
}
