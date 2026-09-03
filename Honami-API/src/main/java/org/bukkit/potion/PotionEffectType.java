package org.bukkit.potion;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

public abstract class PotionEffectType {

	public static final PotionEffectType SPEED = new PotionEffectTypeWrapper(1);

	public static final PotionEffectType SLOW = new PotionEffectTypeWrapper(2);

	public static final PotionEffectType FAST_DIGGING = new PotionEffectTypeWrapper(3);

	public static final PotionEffectType SLOW_DIGGING = new PotionEffectTypeWrapper(4);

	public static final PotionEffectType INCREASE_DAMAGE = new PotionEffectTypeWrapper(5);

	public static final PotionEffectType HEAL = new PotionEffectTypeWrapper(6);

	public static final PotionEffectType HARM = new PotionEffectTypeWrapper(7);

	public static final PotionEffectType JUMP = new PotionEffectTypeWrapper(8);

	public static final PotionEffectType CONFUSION = new PotionEffectTypeWrapper(9);

	public static final PotionEffectType REGENERATION = new PotionEffectTypeWrapper(10);

	public static final PotionEffectType DAMAGE_RESISTANCE = new PotionEffectTypeWrapper(11);

	public static final PotionEffectType FIRE_RESISTANCE = new PotionEffectTypeWrapper(12);

	public static final PotionEffectType WATER_BREATHING = new PotionEffectTypeWrapper(13);

	public static final PotionEffectType INVISIBILITY = new PotionEffectTypeWrapper(14);

	public static final PotionEffectType BLINDNESS = new PotionEffectTypeWrapper(15);

	public static final PotionEffectType NIGHT_VISION = new PotionEffectTypeWrapper(16);

	public static final PotionEffectType HUNGER = new PotionEffectTypeWrapper(17);

	public static final PotionEffectType WEAKNESS = new PotionEffectTypeWrapper(18);

	public static final PotionEffectType POISON = new PotionEffectTypeWrapper(19);

	public static final PotionEffectType WITHER = new PotionEffectTypeWrapper(20);

	public static final PotionEffectType HEALTH_BOOST = new PotionEffectTypeWrapper(21);

	public static final PotionEffectType ABSORPTION = new PotionEffectTypeWrapper(22);

	public static final PotionEffectType SATURATION = new PotionEffectTypeWrapper(23);

	private final int id;

	protected PotionEffectType(int id) {
		this.id = id;
	}

	public PotionEffect createEffect(int duration, int amplifier) {
		return Potion.getBrewer().createEffect(this, duration, amplifier);
	}

	public abstract double getDurationModifier();

	@Deprecated
	public int getId() {
		return id;
	}

	public abstract String getName();

	public abstract boolean isInstant();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PotionEffectType)) {
			return false;
		}
		final PotionEffectType other = (PotionEffectType) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "PotionEffectType[" + id + ", " + getName() + "]";
	}

	private static final PotionEffectType[] byId = new PotionEffectType[24];
	private static final Map<String, PotionEffectType> byName = new HashMap<String, PotionEffectType>();
	
	private static boolean acceptingNew = true;

	@Deprecated
	public static PotionEffectType getById(int id) {
		if (id >= byId.length || id < 0)
			return null;
		return byId[id];
	}

	public static PotionEffectType getByName(String name) {
		Validate.notNull(name, "name cannot be null");
		return byName.get(name.toLowerCase());
	}

	public static void registerPotionEffectType(PotionEffectType type) {
		if (byId[type.id] != null || byName.containsKey(type.getName().toLowerCase())) {
			throw new IllegalArgumentException("Cannot set already-set type");
		} else if (!acceptingNew) {
			throw new IllegalStateException(
					"No longer accepting new potion effect types (can only be done by the server implementation)");
		}

		byId[type.id] = type;
		byName.put(type.getName().toLowerCase(), type);
	}

	public static void stopAcceptingRegistrations() {
		acceptingNew = false;
	}

	public static PotionEffectType[] values() {
		return byId.clone();
	}
}
