package org.bukkit.inventory.meta;

import java.util.List;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;

public interface FireworkMeta extends ItemMeta {

	void addEffect(FireworkEffect effect) throws IllegalArgumentException;

	void addEffects(FireworkEffect... effects) throws IllegalArgumentException;

	void addEffects(Iterable<FireworkEffect> effects) throws IllegalArgumentException;

	List<FireworkEffect> getEffects();

	int getEffectsSize();

	void removeEffect(int index) throws IndexOutOfBoundsException;

	void clearEffects();

	boolean hasEffects();

	int getPower();

	void setPower(int power) throws IllegalArgumentException;

	FireworkMeta clone();
}
