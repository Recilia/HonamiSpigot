package rein.honami.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EquipmentSetEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final int slot;
    private final ItemStack oldItem;
    private final ItemStack newItem;

    public EquipmentSetEvent(Player player, int slot, ItemStack oldItem, ItemStack newItem) {
        this.player = player;
        this.slot = slot;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    public Player getPlayer() {
        return player;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getOldItem() {
        return oldItem;
    }

    public ItemStack getNewItem() {
        return newItem;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
