package org.bukkit.event.world;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class StructureGrowEvent extends WorldEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	private final Location location;
	private final TreeType species;
	private final boolean bonemeal;
	private final Player player;
	private final List<BlockState> blocks;

	public StructureGrowEvent(final Location location, final TreeType species, final boolean bonemeal,
			final Player player, final List<BlockState> blocks) {
		super(location.getWorld());
		this.location = location;
		this.species = species;
		this.bonemeal = bonemeal;
		this.player = player;
		this.blocks = blocks;
	}

	public Location getLocation() {
		return location;
	}

	public TreeType getSpecies() {
		return species;
	}

	public boolean isFromBonemeal() {
		return bonemeal;
	}

	public Player getPlayer() {
		return player;
	}

	public List<BlockState> getBlocks() {
		return blocks;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
