package org.bukkit.entity;

public interface FishHook extends Projectile {

	public double getBiteChance();

	public void setBiteChance(double chance) throws IllegalArgumentException;
}
