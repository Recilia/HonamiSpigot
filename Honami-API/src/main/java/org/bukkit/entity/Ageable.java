package org.bukkit.entity;

public interface Ageable extends Creature {

	public int getAge();

	public void setAge(int age);

	public void setAgeLock(boolean lock);

	public boolean getAgeLock();

	public void setBaby();

	public void setAdult();

	public boolean isAdult();

	public boolean canBreed();

	public void setBreed(boolean breed);
}
