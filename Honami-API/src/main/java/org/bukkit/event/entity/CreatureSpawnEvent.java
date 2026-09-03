package org.bukkit.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class CreatureSpawnEvent extends EntitySpawnEvent {
	private final SpawnReason spawnReason;

	public CreatureSpawnEvent(final LivingEntity spawnee, final SpawnReason spawnReason) {
		super(spawnee);
		this.spawnReason = spawnReason;
	}

	@Deprecated
	public CreatureSpawnEvent(Entity spawnee, CreatureType type, Location loc, SpawnReason reason) {
		super(spawnee);
		spawnReason = reason;
	}

	@Override
	public LivingEntity getEntity() {
		return (LivingEntity) entity;
	}

	@Deprecated
	public CreatureType getCreatureType() {
		return CreatureType.fromEntityType(getEntityType());
	}

	public SpawnReason getSpawnReason() {
		return spawnReason;
	}

	public enum SpawnReason {

		NATURAL,

		JOCKEY,

		CHUNK_GEN,

		SPAWNER,

		EGG,

		SPAWNER_EGG,

		LIGHTNING,

		@Deprecated
		BED,

		BUILD_SNOWMAN,

		BUILD_IRONGOLEM,

		BUILD_WITHER,

		VILLAGE_DEFENSE,

		VILLAGE_INVASION,

		BREEDING,

		SLIME_SPLIT,

		REINFORCEMENTS,

		NETHER_PORTAL,

		DISPENSE_EGG,

		INFECTION,

		CURED,

		OCELOT_BABY,

		SILVERFISH_BLOCK,

		MOUNT,

		CUSTOM,

		DEFAULT
	}
}
