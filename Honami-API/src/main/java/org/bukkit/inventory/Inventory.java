package org.bukkit.inventory;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;

public interface Inventory extends Iterable<ItemStack> {

	public int getSize();

	public int getMaxStackSize();

	public void setMaxStackSize(int size);

	public String getName();

	public ItemStack getItem(int index);

	public void setItem(int index, ItemStack item);

	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException;

	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException;

	public ItemStack[] getContents();

	public void setContents(ItemStack[] items) throws IllegalArgumentException;

	@Deprecated
	public boolean contains(int materialId);

	public boolean contains(Material material) throws IllegalArgumentException;

	public boolean contains(ItemStack item);

	@Deprecated
	public boolean contains(int materialId, int amount);

	public boolean contains(Material material, int amount) throws IllegalArgumentException;

	public boolean contains(ItemStack item, int amount);

	public boolean containsAtLeast(ItemStack item, int amount);

	@Deprecated
	public HashMap<Integer, ? extends ItemStack> all(int materialId);

	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException;

	public HashMap<Integer, ? extends ItemStack> all(ItemStack item);

	@Deprecated
	public int first(int materialId);

	public int first(Material material) throws IllegalArgumentException;

	public int first(ItemStack item);

	public int firstEmpty();

	@Deprecated
	public void remove(int materialId);

	public void remove(Material material) throws IllegalArgumentException;

	public void remove(ItemStack item);

	public void clear(int index);

	public void clear();

	public List<HumanEntity> getViewers();

	public String getTitle();

	public InventoryType getType();

	public InventoryHolder getHolder();

	@Override
	public ListIterator<ItemStack> iterator();

	public ListIterator<ItemStack> iterator(int index);
}
