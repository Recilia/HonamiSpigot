package org.bukkit.entity;

public interface Tameable {

	public boolean isTamed();

	public void setTamed(boolean tame);

	public AnimalTamer getOwner();

	public void setOwner(AnimalTamer tamer);

}
