package org.bukkit.event.player;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerBucketEvent extends PlayerEvent implements Cancellable {
	private ItemStack itemStack;
	private boolean cancelled = false;
	private final Block blockClicked;
	private final BlockFace blockFace;
	private final Material bucket;

	public PlayerBucketEvent(final Player who, final Block blockClicked, final BlockFace blockFace,
			final Material bucket, final ItemStack itemInHand) {
		super(who);
		this.blockClicked = blockClicked;
		this.blockFace = blockFace;
		this.itemStack = itemInHand;
		this.bucket = bucket;
	}

	public Material getBucket() {
		return bucket;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public Block getBlockClicked() {
		return blockClicked;
	}

	public BlockFace getBlockFace() {
		return blockFace;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}