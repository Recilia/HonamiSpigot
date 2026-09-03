package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.util.NumberConversions;

public class EntityRegainHealthEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private double amount;
	private final RegainReason regainReason;

	@Deprecated
	public EntityRegainHealthEvent(final Entity entity, final int amount, final RegainReason regainReason) {
		this(entity, (double) amount, regainReason);
	}

	public EntityRegainHealthEvent(final Entity entity, final double amount, final RegainReason regainReason) {
		super(entity);
		this.amount = amount;
		this.regainReason = regainReason;
	}

	public double getAmount() {
		return amount;
	}

	@Deprecated
	public int _INVALID_getAmount() {
		return NumberConversions.ceil(getAmount());
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Deprecated
	public void _INVALID_setAmount(int amount) {
		setAmount(amount);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;
	}

	public RegainReason getRegainReason() {
		return regainReason;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum RegainReason {

		REGEN,

		SATIATED,

		EATING,

		ENDER_CRYSTAL,

		MAGIC,

		MAGIC_REGEN,

		WITHER_SPAWN,

		WITHER,

		CUSTOM
	}
}
