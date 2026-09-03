package org.bukkit.potion;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.LivingEntity;

import com.google.common.collect.ImmutableMap;

@SerializableAs("PotionEffect")
public class PotionEffect implements ConfigurationSerializable {
	private static final String AMPLIFIER = "amplifier";
	private static final String DURATION = "duration";
	private static final String TYPE = "effect";
	private static final String AMBIENT = "ambient";
	private static final String PARTICLES = "has-particles";
	private final int amplifier;
	private final int duration;
	private final PotionEffectType type;
	private final boolean ambient;
	private final boolean particles;

	public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
		Validate.notNull(type, "effect type cannot be null");
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
		this.ambient = ambient;
		this.particles = particles;
	}

	public PotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient) {
		this(type, duration, amplifier, ambient, true);
	}

	public PotionEffect(PotionEffectType type, int duration, int amplifier) {
		this(type, duration, amplifier, true);
	}

	public PotionEffect(Map<String, Object> map) {
		this(getEffectType(map), getInt(map, DURATION), getInt(map, AMPLIFIER), getBool(map, AMBIENT, false),
				getBool(map, PARTICLES, true));
	}

	private static PotionEffectType getEffectType(Map<?, ?> map) {
		int type = getInt(map, TYPE);
		PotionEffectType effect = PotionEffectType.getById(type);
		if (effect != null) {
			return effect;
		}
		throw new NoSuchElementException(map + " does not contain " + TYPE);
	}

	private static int getInt(Map<?, ?> map, Object key) {
		Object num = map.get(key);
		if (num instanceof Integer) {
			return (Integer) num;
		}
		throw new NoSuchElementException(map + " does not contain " + key);
	}

	private static boolean getBool(Map<?, ?> map, Object key, boolean def) {
		Object bool = map.get(key);
		if (bool instanceof Boolean) {
			return (Boolean) bool;
		}
		return def;
	}

	public Map<String, Object> serialize() {
		return ImmutableMap.<String, Object>of(TYPE, type.getId(), DURATION, duration, AMPLIFIER, amplifier, AMBIENT,
				ambient, PARTICLES, particles);
	}

	public boolean apply(LivingEntity entity) {
		return entity.addPotionEffect(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PotionEffect)) {
			return false;
		}
		PotionEffect that = (PotionEffect) obj;
		return this.type.equals(that.type) && this.ambient == that.ambient && this.amplifier == that.amplifier
				&& this.duration == that.duration && this.particles == that.particles;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public int getDuration() {
		return duration;
	}

	public PotionEffectType getType() {
		return type;
	}

	public boolean isAmbient() {
		return ambient;
	}

	public boolean hasParticles() {
		return particles;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + type.hashCode();
		hash = hash * 31 + amplifier;
		hash = hash * 31 + duration;
		hash ^= 0x22222222 >> (ambient ? 1 : -1);
		hash ^= 0x22222222 >> (particles ? 1 : -1);
		return hash;
	}

	@Override
	public String toString() {
		return type.getName() + (ambient ? ":(" : ":") + duration + "t-x" + amplifier + (ambient ? ")" : "");
	}
}
