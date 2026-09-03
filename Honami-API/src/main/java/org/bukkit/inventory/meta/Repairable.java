package org.bukkit.inventory.meta;

public interface Repairable {

	boolean hasRepairCost();

	int getRepairCost();

	void setRepairCost(int cost);

	@SuppressWarnings("javadoc")
	Repairable clone();
}
