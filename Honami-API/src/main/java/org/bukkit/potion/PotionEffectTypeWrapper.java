package org.bukkit.potion;

public class PotionEffectTypeWrapper extends PotionEffectType {
	protected PotionEffectTypeWrapper(int id) {
		super(id);
	}

	@Override
	public double getDurationModifier() {
		return getType().getDurationModifier();
	}

	@Override
	public String getName() {
		return getType().getName();
	}

	public PotionEffectType getType() {
		return PotionEffectType.getById(getId());
	}

	@Override
	public boolean isInstant() {
		return getType().isInstant();
	}
}
