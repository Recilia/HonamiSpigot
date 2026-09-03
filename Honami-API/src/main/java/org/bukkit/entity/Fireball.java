package org.bukkit.entity;

import org.bukkit.util.Vector;

public interface Fireball extends Projectile, Explosive {

	public void setDirection(Vector direction);

	public Vector getDirection();

}
