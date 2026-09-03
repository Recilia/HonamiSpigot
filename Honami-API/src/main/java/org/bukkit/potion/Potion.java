package org.bukkit.potion;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;

public class Potion {
	private boolean extended = false;
	private boolean splash = false;
	private int level = 1;
	private int name = -1;
	private PotionType type;

	public Potion(PotionType type) {
		this.type = type;
		if (type != null) {
			this.name = type.getDamageValue();
		}
		if (type == null || type == PotionType.WATER) {
			this.level = 0;
		}
	}

	@Deprecated
	public Potion(PotionType type, Tier tier) {
		this(type, tier == Tier.TWO ? 2 : 1);
		Validate.notNull(type, "Type cannot be null");
	}

	@Deprecated
	public Potion(PotionType type, Tier tier, boolean splash) {
		this(type, tier == Tier.TWO ? 2 : 1, splash);
	}

	@Deprecated
	public Potion(PotionType type, Tier tier, boolean splash, boolean extended) {
		this(type, tier, splash);
		this.extended = extended;
	}

	public Potion(PotionType type, int level) {
		this(type);
		Validate.notNull(type, "Type cannot be null");
		Validate.isTrue(type != PotionType.WATER, "Water bottles don't have a level!");
		Validate.isTrue(level > 0 && level < 3, "Level must be 1 or 2");
		this.level = level;
	}

	@Deprecated
	public Potion(PotionType type, int level, boolean splash) {
		this(type, level);
		this.splash = splash;
	}

	@Deprecated
	public Potion(PotionType type, int level, boolean splash, boolean extended) {
		this(type, level, splash);
		this.extended = extended;
	}

	public Potion(int name) {
		this(PotionType.getByDamageValue(name & POTION_BIT));
		this.name = name & NAME_BIT;
		if ((name & POTION_BIT) == 0) {

			this.type = null;
		}
	}

	public Potion splash() {
		setSplash(true);
		return this;
	}

	public Potion extend() {
		setHasExtendedDuration(true);
		return this;
	}

	public void apply(ItemStack to) {
		Validate.notNull(to, "itemstack cannot be null");
		Validate.isTrue(to.getType() == Material.POTION, "given itemstack is not a potion");
		to.setDurability(toDamageValue());
	}

	public void apply(LivingEntity to) {
		Validate.notNull(to, "entity cannot be null");
		to.addPotionEffects(getEffects());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Potion other = (Potion) obj;
		return extended == other.extended && splash == other.splash && level == other.level && type == other.type;
	}

	public Collection<PotionEffect> getEffects() {
		if (type == null)
			return ImmutableList.<PotionEffect>of();
		return getBrewer().getEffectsFromDamage(toDamageValue());
	}

	public int getLevel() {
		return level;
	}

	@Deprecated
	public Tier getTier() {
		return level == 2 ? Tier.TWO : Tier.ONE;
	}

	public PotionType getType() {
		return type;
	}

	public boolean hasExtendedDuration() {
		return extended;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + level;
		result = prime * result + (extended ? 1231 : 1237);
		result = prime * result + (splash ? 1231 : 1237);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	public boolean isSplash() {
		return splash;
	}

	public void setHasExtendedDuration(boolean isExtended) {
		Validate.isTrue(type == null || !type.isInstant(), "Instant potions cannot be extended");
		extended = isExtended;
	}

	public void setSplash(boolean isSplash) {
		splash = isSplash;
	}

	@Deprecated
	public void setTier(Tier tier) {
		Validate.notNull(tier, "tier cannot be null");
		this.level = (tier == Tier.TWO ? 2 : 1);
	}

	public void setType(PotionType type) {
		this.type = type;
	}

	public void setLevel(int level) {
		Validate.notNull(this.type, "No-effect potions don't have a level.");
		int max = type.getMaxLevel();
		Validate.isTrue(level > 0 && level <= max,
				"Level must be " + (max == 1 ? "" : "between 1 and ") + max + " for this potion");
		this.level = level;
	}

	@Deprecated
	public short toDamageValue() {
		short damage;
		if (type == PotionType.WATER) {
			return 0;
		} else if (type == null) {
			
			damage = (short) (name == 0 ? 8192 : name);
		} else {
			damage = (short) (level - 1);
			damage <<= TIER_SHIFT;
			damage |= (short) type.getDamageValue();
		}
		if (splash) {
			damage |= SPLASH_BIT;
		}
		if (extended) {
			damage |= EXTENDED_BIT;
		}
		return damage;
	}

	public ItemStack toItemStack(int amount) {
		return new ItemStack(Material.POTION, amount, toDamageValue());
	}

	@Deprecated
	public enum Tier {
		ONE(0), TWO(0x20);

		private int damageBit;

		Tier(int bit) {
			damageBit = bit;
		}

		public int getDamageBit() {
			return damageBit;
		}

		public static Tier getByDamageBit(int damageBit) {
			for (Tier tier : Tier.values()) {
				if (tier.damageBit == damageBit)
					return tier;
			}
			return null;
		}
	}

	private static PotionBrewer brewer;

	private static final int EXTENDED_BIT = 0x40;
	private static final int POTION_BIT = 0xF;
	private static final int SPLASH_BIT = 0x4000;
	private static final int TIER_BIT = 0x20;
	private static final int TIER_SHIFT = 5;
	private static final int NAME_BIT = 0x3F;

	@Deprecated
	public static Potion fromDamage(int damage) {
		PotionType type = PotionType.getByDamageValue(damage & POTION_BIT);
		Potion potion;
		if (type == null || type == PotionType.WATER) {
			potion = new Potion(damage & NAME_BIT);
		} else {
			int level = (damage & TIER_BIT) >> TIER_SHIFT;
			level++;
			potion = new Potion(type, level);
		}
		if ((damage & SPLASH_BIT) > 0) {
			potion = potion.splash();
		}
		if ((type == null || !type.isInstant()) && (damage & EXTENDED_BIT) > 0) {
			potion = potion.extend();
		}
		return potion;
	}

	public static Potion fromItemStack(ItemStack item) {
		Validate.notNull(item, "item cannot be null");
		if (item.getType() != Material.POTION)
			throw new IllegalArgumentException("item is not a potion");
		return fromDamage(item.getDurability());
	}

	public static PotionBrewer getBrewer() {
		return brewer;
	}

	public static void setPotionBrewer(PotionBrewer other) {
		if (brewer != null)
			throw new IllegalArgumentException("brewer can only be set internally");
		brewer = other;
	}

	@Deprecated
	public int getNameId() {
		return name;
	}
}
