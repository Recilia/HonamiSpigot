package org.bukkit.event.entity;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class EntityDeathEvent extends EntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private final List<ItemStack> drops;
	private int dropExp = 0;

	public EntityDeathEvent(final LivingEntity entity, final List<ItemStack> drops) {
		this(entity, drops, 0);
	}

	public EntityDeathEvent(final LivingEntity what, final List<ItemStack> drops, final int droppedExp) {
		super(what);
		this.drops = drops;
		this.dropExp = droppedExp;
	}

	@Override
	public LivingEntity getEntity() {
		return (LivingEntity) entity;
	}

	public int getDroppedExp() {
		return dropExp;
	}

	public void setDroppedExp(int exp) {
		this.dropExp = exp;
	}

	public List<ItemStack> getDrops() {
		return drops;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
