package org.bukkit.event.entity;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityExplodeEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel;
	private final Location location;
	private final List<Block> blocks;
	private float yield;

	public EntityExplodeEvent(final Entity what, final Location location, final List<Block> blocks, final float yield) {
		super(what);
		this.location = location;
		this.blocks = blocks;
		this.yield = yield;
		this.cancel = false;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public List<Block> blockList() {
		return blocks;
	}

	public Location getLocation() {
		return location;
	}

	public float getYield() {
		return yield;
	}

	public void setYield(float yield) {
		this.yield = yield;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
