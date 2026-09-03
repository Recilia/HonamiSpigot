package org.bukkit.entity;

import org.bukkit.inventory.meta.FireworkMeta;

public interface Firework extends Entity {

	FireworkMeta getFireworkMeta();

	void setFireworkMeta(FireworkMeta meta);

	void detonate();
}
