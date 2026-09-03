package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EntityShootBowEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final ItemStack bow;
	private Entity projectile;
	private final float force;
	private boolean cancelled;

	public EntityShootBowEvent(final LivingEntity shooter, final ItemStack bow, final Projectile projectile,
			final float force) {
		super(shooter);
		this.bow = bow;
		this.projectile = projectile;
		this.force = force;
	}

	@Override
	public LivingEntity getEntity() {
		return (LivingEntity) entity;
	}

	public ItemStack getBow() {
		return bow;
	}

	public Entity getProjectile() {
		return projectile;
	}

	public void setProjectile(Entity projectile) {
		this.projectile = projectile;
	}

	public float getForce() {
		return force;
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
