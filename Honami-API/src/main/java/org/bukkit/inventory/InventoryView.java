package org.bukkit.inventory;

import org.bukkit.GameMode;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;

public abstract class InventoryView {
	public final static int OUTSIDE = -999;

	public enum Property {

		BREW_TIME(0, InventoryType.BREWING),

		COOK_TIME(0, InventoryType.FURNACE),

		BURN_TIME(1, InventoryType.FURNACE),

		TICKS_FOR_CURRENT_FUEL(2, InventoryType.FURNACE),

		ENCHANT_BUTTON1(0, InventoryType.ENCHANTING),

		ENCHANT_BUTTON2(1, InventoryType.ENCHANTING),

		ENCHANT_BUTTON3(2, InventoryType.ENCHANTING);

		int id;
		InventoryType style;

		private Property(int id, InventoryType appliesTo) {
			this.id = id;
			style = appliesTo;
		}

		public InventoryType getType() {
			return style;
		}

		@Deprecated
		public int getId() {
			return id;
		}
	}

	public abstract Inventory getTopInventory();

	public abstract Inventory getBottomInventory();

	public abstract HumanEntity getPlayer();

	public abstract InventoryType getType();

	public void setItem(int slot, ItemStack item) {
		if (slot != OUTSIDE) {
			if (slot < getTopInventory().getSize()) {
				getTopInventory().setItem(convertSlot(slot), item);
			} else {
				getBottomInventory().setItem(convertSlot(slot), item);
			}
		} else {
			getPlayer().getWorld().dropItemNaturally(getPlayer().getLocation(), item);
		}
	}

	public ItemStack getItem(int slot) {
		if (slot == OUTSIDE) {
			return null;
		}
		if (slot < getTopInventory().getSize()) {
			return getTopInventory().getItem(convertSlot(slot));
		} else {
			return getBottomInventory().getItem(convertSlot(slot));
		}
	}

	public final void setCursor(ItemStack item) {
		getPlayer().setItemOnCursor(item);
	}

	public final ItemStack getCursor() {
		return getPlayer().getItemOnCursor();
	}

	public final int convertSlot(int rawSlot) {
		int numInTop = getTopInventory().getSize();
		if (rawSlot < numInTop) {
			return rawSlot;
		}
		int slot = rawSlot - numInTop;
		if (getPlayer().getGameMode() == GameMode.CREATIVE && getType() == InventoryType.PLAYER) {
			return slot;
		}
		if (getType() == InventoryType.CRAFTING) {
			if (slot < 4)
				return 39 - slot;
			else
				slot -= 4;
		}
		if (slot >= 27)
			slot -= 27;
		else
			slot += 9;
		return slot;
	}

	public final void close() {
		getPlayer().closeInventory();
	}

	public final int countSlots() {
		return getTopInventory().getSize() + getBottomInventory().getSize();
	}

    

    public SlotType getSlotType(int slot) { 
        SlotType type = SlotType.CONTAINER;
        if (slot >= 0 && slot < getTopInventory().getSize()) {
            switch (getType()) {
                case FURNACE:
                    if (slot == 2) {
                        type = SlotType.RESULT;
                    } else if (slot == 1) {
                        type = SlotType.FUEL;
                    } else {
                        type = SlotType.CRAFTING;
                    }
                    break;
                case BREWING:
                    if (slot == 3) {
                        type = SlotType.FUEL;
                    } else {
                        type = SlotType.CRAFTING;
                    }
                    break;
                case ENCHANTING:
                case BEACON:
                    type = SlotType.CRAFTING;
                    break;
                case WORKBENCH:
                case CRAFTING:
                    if (slot == 0) {
                        type = SlotType.RESULT;
                    } else {
                        type = SlotType.CRAFTING;
                    }
                    break;
                case MERCHANT:
                case ANVIL:
                    if (slot == 2) {
                        type = SlotType.RESULT;
                    } else {
                        type = SlotType.CRAFTING;
                    }
                    break;
                default:
                    
            }
        } else {
            if (slot == -999) {
                type = SlotType.OUTSIDE;
            } else if (getType() == InventoryType.CRAFTING) {
                if (slot < 9) {
                    type = SlotType.ARMOR;
                } else if (slot > 35) {
                    type = SlotType.QUICKBAR;
                }
            } else if (slot >= (countSlots() - 9)) {
                type = SlotType.QUICKBAR;
            }
        }
        return type;
    }

	

	public final boolean setProperty(Property prop, int value) {
		return getPlayer().setWindowProperty(prop, value);
	}

	public final String getTitle() {
		return getTopInventory().getTitle();
	}
}
