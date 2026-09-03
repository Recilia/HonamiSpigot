package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class BlockDamageEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private boolean instaBreak;
	private boolean cancel;
	private final ItemStack itemstack;

	public BlockDamageEvent(final Player player, final Block block, final ItemStack itemInHand,
			final boolean instaBreak) {
		super(block);
		this.instaBreak = instaBreak;
		this.player = player;
		this.itemstack = itemInHand;
		this.cancel = false;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean getInstaBreak() {
		return instaBreak;
	}

	public void setInstaBreak(boolean bool) {
		this.instaBreak = bool;
	}

	public ItemStack getItemInHand() {
		return itemstack;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
