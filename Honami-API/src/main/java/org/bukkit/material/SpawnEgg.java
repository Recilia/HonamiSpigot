package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class SpawnEgg extends MaterialData {

	public SpawnEgg() {
		super(Material.MONSTER_EGG);
	}

	@Deprecated
	public SpawnEgg(int type, byte data) {
		super(type, data);
	}

	@Deprecated
	public SpawnEgg(byte data) {
		super(Material.MONSTER_EGG, data);
	}

	public SpawnEgg(EntityType type) {
		this();
		setSpawnedType(type);
	}

	public EntityType getSpawnedType() {
		return EntityType.fromId(getData());
	}

	public void setSpawnedType(EntityType type) {
		setData((byte) type.getTypeId());
	}

	@Override
	public String toString() {
		return "SPAWN EGG{" + getSpawnedType() + "}";
	}

	@Override
	public SpawnEgg clone() {
		return (SpawnEgg) super.clone();
	}
}
