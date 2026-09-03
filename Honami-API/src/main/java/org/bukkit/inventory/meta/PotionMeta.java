package org.bukkit.inventory.meta;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public interface PotionMeta extends ItemMeta {

	boolean hasCustomEffects();

	List<PotionEffect> getCustomEffects();

	boolean addCustomEffect(PotionEffect effect, boolean overwrite);

	boolean removeCustomEffect(PotionEffectType type);

	boolean hasCustomEffect(PotionEffectType type);

	boolean setMainEffect(PotionEffectType type);

	boolean clearCustomEffects();

	PotionMeta clone();
}
