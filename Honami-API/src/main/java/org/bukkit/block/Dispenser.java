package org.bukkit.block;

import org.bukkit.projectiles.BlockProjectileSource;

public interface Dispenser extends BlockState, ContainerBlock {

	public BlockProjectileSource getBlockProjectileSource();

	public boolean dispense();
}
