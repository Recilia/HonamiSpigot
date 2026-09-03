package org.bukkit.event.enchantment;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class PrepareItemEnchantEvent extends InventoryEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Block table;
	private final ItemStack item;
	private final int[] levelsOffered;
	private final int bonus;
	private boolean cancelled;
	private final Player enchanter;

	public PrepareItemEnchantEvent(final Player enchanter, InventoryView view, final Block table, final ItemStack item,
			final int[] levelsOffered, final int bonus) {
		super(view);
		this.enchanter = enchanter;
		this.table = table;
		this.item = item;
		this.levelsOffered = levelsOffered;
		this.bonus = bonus;
		this.cancelled = false;
	}

	public Player getEnchanter() {
		return enchanter;
	}

	public Block getEnchantBlock() {
		return table;
	}

	public ItemStack getItem() {
		return item;
	}

	public int[] getExpLevelCostsOffered() {
		return levelsOffered;
	}

	public int getEnchantmentBonus() {
		return bonus;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
