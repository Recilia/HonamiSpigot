package org.bukkit.entity;

import org.bukkit.util.Vector;

public interface Vehicle extends Entity {

	public Vector getVelocity();

	public void setVelocity(Vector vel);
}
