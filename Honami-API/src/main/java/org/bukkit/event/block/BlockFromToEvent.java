package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockFromToEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	protected Block to;
	protected BlockFace face;
	protected boolean cancel;

	public BlockFromToEvent(final Block block, final BlockFace face) {
		super(block);
		this.face = face;
		this.cancel = false;
	}

	public BlockFromToEvent(final Block block, final Block toBlock) {
		super(block);
		this.to = toBlock;
		this.face = BlockFace.SELF;
		this.cancel = false;
	}

	public BlockFace getFace() {
		return face;
	}

	public Block getToBlock() {
		if (to == null) {
			to = block.getRelative(face);
		}
		return to;
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
