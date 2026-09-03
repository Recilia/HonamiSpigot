package org.bukkit.event.block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

public class BlockPistonExtendEvent extends BlockPistonEvent {
	private static final HandlerList handlers = new HandlerList();
	private final int length;
	private List<Block> blocks;

	@Deprecated
	public BlockPistonExtendEvent(final Block block, final int length, final BlockFace direction) {
		super(block, direction);

		this.length = length;
	}

	public BlockPistonExtendEvent(final Block block, final List<Block> blocks, final BlockFace direction) {
		super(block, direction);

		this.length = blocks.size();
		this.blocks = blocks;
	}

	@Deprecated
	public int getLength() {
		return this.length;
	}

	public List<Block> getBlocks() {
		if (blocks == null) {
			ArrayList<Block> tmp = new ArrayList<Block>();
			for (int i = 0; i < this.getLength(); i++) {
				tmp.add(block.getRelative(getDirection(), i + 1));
			}
			blocks = Collections.unmodifiableList(tmp);
		}
		return blocks;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
