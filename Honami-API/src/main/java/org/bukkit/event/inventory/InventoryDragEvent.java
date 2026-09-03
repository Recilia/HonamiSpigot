package org.bukkit.event.inventory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.ImmutableSet;

public class InventoryDragEvent extends InventoryInteractEvent {
	private static final HandlerList handlers = new HandlerList();
	private final DragType type;
	private final Map<Integer, ItemStack> addedItems;
	private final Set<Integer> containerSlots;
	private final ItemStack oldCursor;
	private ItemStack newCursor;

	public InventoryDragEvent(InventoryView what, ItemStack newCursor, ItemStack oldCursor, boolean right,
			Map<Integer, ItemStack> slots) {
		super(what);

		Validate.notNull(oldCursor);
		Validate.notNull(slots);

		type = right ? DragType.SINGLE : DragType.EVEN;
		this.newCursor = newCursor;
		this.oldCursor = oldCursor;
		this.addedItems = slots;
		ImmutableSet.Builder<Integer> b = ImmutableSet.builder();
		for (Integer slot : slots.keySet()) {
			b.add(what.convertSlot(slot));
		}
		this.containerSlots = b.build();
	}

	public Map<Integer, ItemStack> getNewItems() {
		return Collections.unmodifiableMap(addedItems);
	}

	public Set<Integer> getRawSlots() {
		return addedItems.keySet();
	}

	public Set<Integer> getInventorySlots() {
		return containerSlots;
	}

	public ItemStack getCursor() {
		return newCursor;
	}

	public void setCursor(ItemStack newCursor) {
		this.newCursor = newCursor;
	}

	public ItemStack getOldCursor() {
		return oldCursor.clone();
	}

	public DragType getType() {
		return type;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
