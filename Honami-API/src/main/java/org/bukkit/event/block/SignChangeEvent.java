package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class SignChangeEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private final Player player;
	private final String[] lines;

	public SignChangeEvent(final Block theBlock, final Player thePlayer, final String[] theLines) {
		super(theBlock);
		this.player = thePlayer;
		this.lines = theLines;
	}

	public Player getPlayer() {
		return player;
	}

	public String[] getLines() {
		return lines;
	}

	public String getLine(int index) throws IndexOutOfBoundsException {
		return lines[index];
	}

	public void setLine(int index, String line) throws IndexOutOfBoundsException {
		lines[index] = line;
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
