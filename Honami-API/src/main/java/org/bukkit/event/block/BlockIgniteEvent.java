package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class BlockIgniteEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final IgniteCause cause;
	private final Entity ignitingEntity;
	private final Block ignitingBlock;
	private boolean cancel;

	@Deprecated
	public BlockIgniteEvent(final Block theBlock, final IgniteCause cause, final Player thePlayer) {
		this(theBlock, cause, (Entity) thePlayer);
	}

	public BlockIgniteEvent(final Block theBlock, final IgniteCause cause, final Entity ignitingEntity) {
		this(theBlock, cause, ignitingEntity, null);
	}

	public BlockIgniteEvent(final Block theBlock, final IgniteCause cause, final Block ignitingBlock) {
		this(theBlock, cause, null, ignitingBlock);
	}

	public BlockIgniteEvent(final Block theBlock, final IgniteCause cause, final Entity ignitingEntity,
			final Block ignitingBlock) {
		super(theBlock);
		this.cause = cause;
		this.ignitingEntity = ignitingEntity;
		this.ignitingBlock = ignitingBlock;
		this.cancel = false;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public IgniteCause getCause() {
		return cause;
	}

	public Player getPlayer() {
		if (ignitingEntity instanceof Player) {
			return (Player) ignitingEntity;
		}

		return null;
	}

	public Entity getIgnitingEntity() {
		return ignitingEntity;
	}

	public Block getIgnitingBlock() {
		return ignitingBlock;
	}

	public enum IgniteCause {

		LAVA,

		FLINT_AND_STEEL,

		SPREAD,

		LIGHTNING,

		FIREBALL,

		ENDER_CRYSTAL,

		EXPLOSION,
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
