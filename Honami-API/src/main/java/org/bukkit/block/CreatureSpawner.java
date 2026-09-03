package org.bukkit.block;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EntityType;

public interface CreatureSpawner extends BlockState {

	@Deprecated
	public CreatureType getCreatureType();

	public EntityType getSpawnedType();

	public void setSpawnedType(EntityType creatureType);

	@Deprecated
	public void setCreatureType(CreatureType creatureType);

	@Deprecated
	public String getCreatureTypeId();

	public void setCreatureTypeByName(String creatureType);

	public String getCreatureTypeName();

	@Deprecated
	public void setCreatureTypeId(String creatureType);

	public int getDelay();

	public void setDelay(int delay);
}
