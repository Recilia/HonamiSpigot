package org.bukkit.inventory.meta;

public interface MapMeta extends ItemMeta {

	boolean isScaling();

	void setScaling(boolean value);

	MapMeta clone();
}
