package org.bukkit.entity;

public interface TNTPrimed extends Explosive {

	public void setFuseTicks(int fuseTicks);

	public int getFuseTicks();

	public Entity getSource();

	public org.bukkit.Location getSourceLoc();
}
