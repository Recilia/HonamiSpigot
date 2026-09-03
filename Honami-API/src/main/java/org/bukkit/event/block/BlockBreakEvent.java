package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class BlockBreakEvent extends BlockExpEvent implements Cancellable {
	private final Player player;
	private boolean cancel;

	public BlockBreakEvent(final Block theBlock, final Player player) {
		super(theBlock, 0);

		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
