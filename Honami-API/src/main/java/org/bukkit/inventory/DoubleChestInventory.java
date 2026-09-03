package org.bukkit.inventory;

import org.bukkit.block.DoubleChest;

public interface DoubleChestInventory extends Inventory {

	Inventory getLeftSide();

	Inventory getRightSide();

	DoubleChest getHolder();
}
