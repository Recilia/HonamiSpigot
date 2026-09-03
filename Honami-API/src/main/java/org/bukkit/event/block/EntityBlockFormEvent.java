package org.bukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

public class EntityBlockFormEvent extends BlockFormEvent {
	private final Entity entity;

	public EntityBlockFormEvent(final Entity entity, final Block block, final BlockState blockstate) {
		super(block, blockstate);

		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}
}