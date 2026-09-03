package org.bukkit.event.inventory;

import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

public class InventoryClickEvent extends InventoryInteractEvent {
	private static final HandlerList handlers = new HandlerList();
	private final ClickType click;
	private final InventoryAction action;
	private final Inventory clickedInventory;
	private SlotType slot_type;
	private int whichSlot;
	private int rawSlot;
	private ItemStack current = null;
	private int hotbarKey = -1;

	@Deprecated
	public InventoryClickEvent(InventoryView view, SlotType type, int slot, boolean right, boolean shift) {
		this(view, type, slot, right ? (shift ? ClickType.SHIFT_RIGHT : ClickType.RIGHT)
				: (shift ? ClickType.SHIFT_LEFT : ClickType.LEFT), InventoryAction.SWAP_WITH_CURSOR);
	}

	public InventoryClickEvent(InventoryView view, SlotType type, int slot, ClickType click, InventoryAction action) {
		super(view);
		this.slot_type = type;
		this.rawSlot = slot;
		if (slot < 0) {
			this.clickedInventory = null;
		} else if (view.getTopInventory() != null && slot < view.getTopInventory().getSize()) {
			this.clickedInventory = view.getTopInventory();
		} else {
			this.clickedInventory = view.getBottomInventory();
		}
		this.whichSlot = view.convertSlot(slot);
		this.click = click;
		this.action = action;
	}

	public InventoryClickEvent(InventoryView view, SlotType type, int slot, ClickType click, InventoryAction action,
			int key) {
		this(view, type, slot, click, action);
		this.hotbarKey = key;
	}

	public Inventory getClickedInventory() {
		return clickedInventory;
	}

	public SlotType getSlotType() {
		return slot_type;
	}

	public ItemStack getCursor() {
		return getView().getCursor();
	}

	public ItemStack getCurrentItem() {
		if (slot_type == SlotType.OUTSIDE) {
			return current;
		}
		return getView().getItem(rawSlot);
	}

	public boolean isRightClick() {
		return click.isRightClick();
	}

	public boolean isLeftClick() {
		return click.isLeftClick();
	}

	public boolean isShiftClick() {
		return click.isShiftClick();
	}

	@Deprecated
	public void setCursor(ItemStack stack) {
		getView().setCursor(stack);
	}

	public void setCurrentItem(ItemStack stack) {
		if (slot_type == SlotType.OUTSIDE) {
			current = stack;
		} else {
			getView().setItem(rawSlot, stack);
		}
	}

	public int getSlot() {
		return whichSlot;
	}

	public int getRawSlot() {
		return rawSlot;
	}

	public int getHotbarButton() {
		return hotbarKey;
	}

	public InventoryAction getAction() {
		return action;
	}

	public ClickType getClick() {
		return click;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
