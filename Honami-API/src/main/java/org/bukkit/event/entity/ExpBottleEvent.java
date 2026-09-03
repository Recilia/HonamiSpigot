package org.bukkit.event.entity;

import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.HandlerList;

public class ExpBottleEvent extends ProjectileHitEvent {
	private static final HandlerList handlers = new HandlerList();
	private int exp;
	private boolean showEffect = true;

	public ExpBottleEvent(final ThrownExpBottle bottle, final int exp) {
		super(bottle);
		this.exp = exp;
	}

	@Override
	public ThrownExpBottle getEntity() {
		return (ThrownExpBottle) entity;
	}

	public boolean getShowEffect() {
		return this.showEffect;
	}

	public void setShowEffect(final boolean showEffect) {
		this.showEffect = showEffect;
	}

	public int getExperience() {
		return exp;
	}

	public void setExperience(final int exp) {
		this.exp = exp;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
