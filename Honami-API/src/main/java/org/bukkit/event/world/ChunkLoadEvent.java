package org.bukkit.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;

public class ChunkLoadEvent extends ChunkEvent {
	private static final HandlerList handlers = new HandlerList();
	private final boolean newChunk;

	public ChunkLoadEvent(final Chunk chunk, final boolean newChunk) {
		super(chunk);
		this.newChunk = newChunk;
	}

	public boolean isNewChunk() {
		return newChunk;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
