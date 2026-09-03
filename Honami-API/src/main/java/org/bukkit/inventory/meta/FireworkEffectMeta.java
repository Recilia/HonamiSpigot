package org.bukkit.inventory.meta;

import org.bukkit.FireworkEffect;
import org.bukkit.Material;

public interface FireworkEffectMeta extends ItemMeta {

	void setEffect(FireworkEffect effect);

	boolean hasEffect();

	FireworkEffect getEffect();

	FireworkEffectMeta clone();
}
