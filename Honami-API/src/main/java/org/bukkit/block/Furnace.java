package org.bukkit.block;

import org.bukkit.inventory.FurnaceInventory;

public interface Furnace extends BlockState, ContainerBlock {

	public short getBurnTime();

	public void setBurnTime(short burnTime);

	public short getCookTime();

	public void setCookTime(short cookTime);

	public FurnaceInventory getInventory();
}
