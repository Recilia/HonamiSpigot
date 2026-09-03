package org.bukkit.entity;

public interface PigZombie extends Zombie {

	int getAnger();

	void setAnger(int level);

	void setAngry(boolean angry);

	boolean isAngry();
}
