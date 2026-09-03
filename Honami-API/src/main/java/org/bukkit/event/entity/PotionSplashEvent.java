package org.bukkit.event.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class PotionSplashEvent extends ProjectileHitEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	private final Map<LivingEntity, Double> affectedEntities;

	public PotionSplashEvent(final ThrownPotion potion, final Map<LivingEntity, Double> affectedEntities) {
		super(potion);

		this.affectedEntities = affectedEntities;
	}

	@Override
	public ThrownPotion getEntity() {
		return (ThrownPotion) entity;
	}

	public ThrownPotion getPotion() {
		return (ThrownPotion) getEntity();
	}

	public Collection<LivingEntity> getAffectedEntities() {
		return new ArrayList<LivingEntity>(affectedEntities.keySet());
	}

	public double getIntensity(LivingEntity entity) {
		Double intensity = affectedEntities.get(entity);
		return intensity != null ? intensity : 0.0;
	}

	public void setIntensity(LivingEntity entity, double intensity) {
		Validate.notNull(entity, "You must specify a valid entity.");
		if (intensity <= 0.0) {
			affectedEntities.remove(entity);
		} else {
			affectedEntities.put(entity, Math.min(intensity, 1.0));
		}
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
