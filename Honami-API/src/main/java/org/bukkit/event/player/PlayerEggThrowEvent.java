package org.bukkit.event.player;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerEggThrowEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Egg egg;
	private boolean hatching;
	private EntityType hatchType;
	private byte numHatches;

	public PlayerEggThrowEvent(final Player player, final Egg egg, final boolean hatching, final byte numHatches,
			final EntityType hatchingType) {
		super(player);
		this.egg = egg;
		this.hatching = hatching;
		this.numHatches = numHatches;
		this.hatchType = hatchingType;
	}

	@Deprecated
	public PlayerEggThrowEvent(Player player, Egg egg, boolean hatching, byte numHatches, CreatureType hatchingType) {
		this(player, egg, hatching, numHatches, hatchingType.toEntityType());
	}

	public Egg getEgg() {
		return egg;
	}

	public boolean isHatching() {
		return hatching;
	}

	public void setHatching(boolean hatching) {
		this.hatching = hatching;
	}

	@Deprecated
	public CreatureType getHatchType() {
		return CreatureType.fromEntityType(hatchType);
	}

	public EntityType getHatchingType() {
		return hatchType;
	}

	@Deprecated
	public void setHatchType(CreatureType hatchType) {
		this.hatchType = hatchType.toEntityType();
	}

	public void setHatchingType(EntityType hatchType) {
		if (!hatchType.isSpawnable())
			throw new IllegalArgumentException("Can't spawn that entity type from an egg!");
		this.hatchType = hatchType;
	}

	public byte getNumHatches() {
		return numHatches;
	}

	public void setNumHatches(byte numHatches) {
		this.numHatches = numHatches;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
