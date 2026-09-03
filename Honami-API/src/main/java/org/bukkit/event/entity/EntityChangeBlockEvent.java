package org.bukkit.event.entity;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityChangeBlockEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Block block;
	private boolean cancel;
	private final Material to;
	private final byte data;

	@Deprecated
	public EntityChangeBlockEvent(final LivingEntity what, final Block block, final Material to) {
		this(what, block, to, (byte) 0);
	}

	@Deprecated
	public EntityChangeBlockEvent(final Entity what, final Block block, final Material to, final byte data) {
		super(what);
		this.block = block;
		this.cancel = false;
		this.to = to;
		this.data = data;
	}

	public Block getBlock() {
		return block;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public Material getTo() {
		return to;
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
