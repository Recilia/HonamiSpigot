package org.bukkit.entity;

import org.bukkit.projectiles.ProjectileSource;

public interface Projectile extends Entity {

	@Deprecated
	public LivingEntity _INVALID_getShooter();

	public ProjectileSource getShooter();

	@Deprecated
	public void _INVALID_setShooter(LivingEntity shooter);

	public void setShooter(ProjectileSource source);

	public boolean doesBounce();

	public void setBounce(boolean doesBounce);
}
