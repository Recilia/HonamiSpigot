package org.bukkit.event.enchantment;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class EnchantItemEvent extends InventoryEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Block table;
	private final ItemStack item;
	private int level;
	private boolean cancelled;
	private final Map<Enchantment, Integer> enchants;
	private final Player enchanter;
	private int button;

	public EnchantItemEvent(final Player enchanter, final InventoryView view, final Block table, final ItemStack item,
			final int level, final Map<Enchantment, Integer> enchants, final int i) {
		super(view);
		this.enchanter = enchanter;
		this.table = table;
		this.item = item;
		this.level = level;
		this.enchants = new HashMap<Enchantment, Integer>(enchants);
		this.cancelled = false;
		this.button = i;
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

	public int getExpLevelCost() {
		return level;
	}

	public void setExpLevelCost(int level) {
		this.level = level;
	}

	public Map<Enchantment, Integer> getEnchantsToAdd() {
		return enchants;
	}

	public int whichButton() {
		return button;
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
