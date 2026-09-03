package org.bukkit.entity;

import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public interface Minecart extends Vehicle {

	@Deprecated
	public void _INVALID_setDamage(int damage);

	public void setDamage(double damage);

	@Deprecated
	public int _INVALID_getDamage();

	public double getDamage();

	public double getMaxSpeed();

	public void setMaxSpeed(double speed);

	public boolean isSlowWhenEmpty();

	public void setSlowWhenEmpty(boolean slow);

	public Vector getFlyingVelocityMod();

	public void setFlyingVelocityMod(Vector flying);

	public Vector getDerailedVelocityMod();

	public void setDerailedVelocityMod(Vector derailed);

	public void setDisplayBlock(MaterialData material);

	public MaterialData getDisplayBlock();

	public void setDisplayBlockOffset(int offset);

	public int getDisplayBlockOffset();
}
