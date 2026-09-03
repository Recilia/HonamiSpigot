package org.bukkit.potion;

import java.util.Collection;

public interface PotionBrewer {

	public PotionEffect createEffect(PotionEffectType potion, int duration, int amplifier);

	@Deprecated
	public Collection<PotionEffect> getEffectsFromDamage(int damage);
}
