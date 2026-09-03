package org.bukkit.event.entity;

import org.bukkit.entity.Entity;

public class EntityCombustByEntityEvent extends EntityCombustEvent {
	private final Entity combuster;

	public EntityCombustByEntityEvent(final Entity combuster, final Entity combustee, final int duration) {
		super(combustee, duration);
		this.combuster = combuster;
	}

	public Entity getCombuster() {
		return combuster;
	}
}
