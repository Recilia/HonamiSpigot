package org.bukkit.entity;

public interface Creature extends LivingEntity {

	public void setTarget(LivingEntity target);

	public LivingEntity getTarget();
}
