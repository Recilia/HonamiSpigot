package org.bukkit.event.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;

public class BlockCanBuildEvent extends BlockEvent {
	private static final HandlerList handlers = new HandlerList();
	protected boolean buildable;

	@Deprecated
	protected int material;

	@Deprecated
	public BlockCanBuildEvent(final Block block, final int id, final boolean canBuild) {
		super(block);
		buildable = canBuild;
		material = id;
	}

	public boolean isBuildable() {
		return buildable;
	}

	public void setBuildable(boolean cancel) {
		this.buildable = cancel;
	}

	public Material getMaterial() {
		return Material.getMaterial(material);
	}

	@Deprecated
	public int getMaterialId() {
		return material;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
