package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class EntityTargetEvent extends EntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Entity target;
	private final TargetReason reason;

	public EntityTargetEvent(final Entity entity, final Entity target, final TargetReason reason) {
		super(entity);
		this.target = target;
		this.reason = reason;
	}

	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public TargetReason getReason() {
		return reason;
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public enum TargetReason {

		TARGET_DIED,

		CLOSEST_PLAYER,

		TARGET_ATTACKED_ENTITY,

		PIG_ZOMBIE_TARGET,

		FORGOT_TARGET,

		TARGET_ATTACKED_OWNER,

		OWNER_ATTACKED_TARGET,

		RANDOM_TARGET,

		DEFEND_VILLAGE,

		TARGET_ATTACKED_NEARBY_ENTITY,

		REINFORCEMENT_TARGET,

		COLLISION,

		CUSTOM,

		CLOSEST_ENTITY,

		UNKNOWN;
	}
}
