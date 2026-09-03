package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockPhysicsEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final int changed;
	private boolean cancel = false;

	@Deprecated
	public BlockPhysicsEvent(final Block block, final int changed) {
		super(block);
		this.changed = changed;
	}

	@Deprecated
	public int getChangedTypeId() {
		return changed;
	}

	public Material getChangedType() {
		return Material.getMaterial(changed);
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
