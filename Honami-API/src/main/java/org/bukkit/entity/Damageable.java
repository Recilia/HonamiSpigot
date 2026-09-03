package org.bukkit.entity;

public interface Damageable extends Entity {

	void damage(double amount);

	@Deprecated
	void _INVALID_damage(int amount);

	void damage(double amount, Entity source);

	@Deprecated
	void _INVALID_damage(int amount, Entity source);

	double getHealth();

	@Deprecated
	int _INVALID_getHealth();

	void setHealth(double health);

	@Deprecated
	void _INVALID_setHealth(int health);

	double getMaxHealth();

	@Deprecated
	int _INVALID_getMaxHealth();

	void setMaxHealth(double health);

	@Deprecated
	void _INVALID_setMaxHealth(int health);

	void resetMaxHealth();
}
