package org.bukkit.entity;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permissible;

public interface HumanEntity extends LivingEntity, AnimalTamer, Permissible, InventoryHolder {

	public String getName();

	public PlayerInventory getInventory();

	public Inventory getEnderChest();

	public boolean setWindowProperty(InventoryView.Property prop, int value);

	public InventoryView getOpenInventory();

	public InventoryView openInventory(Inventory inventory);

	public InventoryView openWorkbench(Location location, boolean force);

	public InventoryView openEnchanting(Location location, boolean force);

	public void openInventory(InventoryView inventory);

	public void closeInventory();

	public ItemStack getItemInHand();

	public void setItemInHand(ItemStack item);

	public ItemStack getItemOnCursor();

	public void setItemOnCursor(ItemStack item);

	public boolean isSleeping();

	public int getSleepTicks();

	public GameMode getGameMode();

	public void setGameMode(GameMode mode);

	public boolean isBlocking();

	public int getExpToLevel();
}
