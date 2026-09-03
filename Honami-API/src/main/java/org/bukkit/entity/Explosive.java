package org.bukkit.entity;

public interface Explosive extends Entity {

	public void setYield(float yield);

	public float getYield();

	public void setIsIncendiary(boolean isIncendiary);

	public boolean isIncendiary();
}
