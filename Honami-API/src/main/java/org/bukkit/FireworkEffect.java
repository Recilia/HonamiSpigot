package org.bukkit;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

@SerializableAs("Firework")
public final class FireworkEffect implements ConfigurationSerializable {

	public enum Type {

		BALL,

		BALL_LARGE,

		STAR,

		BURST,

		CREEPER,;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		boolean flicker = false;
		boolean trail = false;
		final ImmutableList.Builder<Color> colors = ImmutableList.builder();
		ImmutableList.Builder<Color> fadeColors = null;
		Type type = Type.BALL;

		Builder() {
		}

		public Builder with(Type type) throws IllegalArgumentException {
			Validate.notNull(type, "Cannot have null type");
			this.type = type;
			return this;
		}

		public Builder withFlicker() {
			flicker = true;
			return this;
		}

		public Builder flicker(boolean flicker) {
			this.flicker = flicker;
			return this;
		}

		public Builder withTrail() {
			trail = true;
			return this;
		}

		public Builder trail(boolean trail) {
			this.trail = trail;
			return this;
		}

		public Builder withColor(Color color) throws IllegalArgumentException {
			Validate.notNull(color, "Cannot have null color");

			colors.add(color);

			return this;
		}

		public Builder withColor(Color... colors) throws IllegalArgumentException {
			Validate.notNull(colors, "Cannot have null colors");
			if (colors.length == 0) {
				return this;
			}

			ImmutableList.Builder<Color> list = this.colors;
			for (Color color : colors) {
				Validate.notNull(color, "Color cannot be null");
				list.add(color);
			}

			return this;
		}

		public Builder withColor(Iterable<?> colors) throws IllegalArgumentException {
			Validate.notNull(colors, "Cannot have null colors");

			ImmutableList.Builder<Color> list = this.colors;
			for (Object color : colors) {
				if (!(color instanceof Color)) {
					throw new IllegalArgumentException(color + " is not a Color in " + colors);
				}
				list.add((Color) color);
			}

			return this;
		}

		public Builder withFade(Color color) throws IllegalArgumentException {
			Validate.notNull(color, "Cannot have null color");

			if (fadeColors == null) {
				fadeColors = ImmutableList.builder();
			}

			fadeColors.add(color);

			return this;
		}

		public Builder withFade(Color... colors) throws IllegalArgumentException {
			Validate.notNull(colors, "Cannot have null colors");
			if (colors.length == 0) {
				return this;
			}

			ImmutableList.Builder<Color> list = this.fadeColors;
			if (list == null) {
				list = this.fadeColors = ImmutableList.builder();
			}

			for (Color color : colors) {
				Validate.notNull(color, "Color cannot be null");
				list.add(color);
			}

			return this;
		}

		public Builder withFade(Iterable<?> colors) throws IllegalArgumentException {
			Validate.notNull(colors, "Cannot have null colors");

			ImmutableList.Builder<Color> list = this.fadeColors;
			if (list == null) {
				list = this.fadeColors = ImmutableList.builder();
			}

			for (Object color : colors) {
				if (!(color instanceof Color)) {
					throw new IllegalArgumentException(color + " is not a Color in " + colors);
				}
				list.add((Color) color);
			}

			return this;
		}

		public FireworkEffect build() {
			return new FireworkEffect(flicker, trail, colors.build(),
					fadeColors == null ? ImmutableList.<Color>of() : fadeColors.build(), type);
		}
	}

	private static final String FLICKER = "flicker";
	private static final String TRAIL = "trail";
	private static final String COLORS = "colors";
	private static final String FADE_COLORS = "fade-colors";
	private static final String TYPE = "type";

	private final boolean flicker;
	private final boolean trail;
	private final ImmutableList<Color> colors;
	private final ImmutableList<Color> fadeColors;
	private final Type type;
	private String string = null;

	FireworkEffect(boolean flicker, boolean trail, ImmutableList<Color> colors, ImmutableList<Color> fadeColors,
			Type type) {
		if (colors.isEmpty()) {
			throw new IllegalStateException("Cannot make FireworkEffect without any color");
		}
		this.flicker = flicker;
		this.trail = trail;
		this.colors = colors;
		this.fadeColors = fadeColors;
		this.type = type;
	}

	public boolean hasFlicker() {
		return flicker;
	}

	public boolean hasTrail() {
		return trail;
	}

	public List<Color> getColors() {
		return colors;
	}

	public List<Color> getFadeColors() {
		return fadeColors;
	}

	public Type getType() {
		return type;
	}

	public static ConfigurationSerializable deserialize(Map<String, Object> map) {
		Type type = Type.valueOf((String) map.get(TYPE));
		if (type == null) {
			throw new IllegalArgumentException(map.get(TYPE) + " is not a valid Type");
		}

		return builder().flicker((Boolean) map.get(FLICKER)).trail((Boolean) map.get(TRAIL))
				.withColor((Iterable<?>) map.get(COLORS)).withFade((Iterable<?>) map.get(FADE_COLORS)).with(type)
				.build();
	}

	@Override
	public Map<String, Object> serialize() {
		return ImmutableMap.<String, Object>of(FLICKER, flicker, TRAIL, trail, COLORS, colors, FADE_COLORS, fadeColors,
				TYPE, type.name());
	}

	@Override
	public String toString() {
		final String string = this.string;
		if (string == null) {
			return this.string = "FireworkEffect:" + serialize();
		}
		return string;
	}

	@Override
	public int hashCode() {

		final int PRIME = 31, TRUE = 1231, FALSE = 1237;
		int hash = 1;
		hash = hash * PRIME + (flicker ? TRUE : FALSE);
		hash = hash * PRIME + (trail ? TRUE : FALSE);
		hash = hash * PRIME + type.hashCode();
		hash = hash * PRIME + colors.hashCode();
		hash = hash * PRIME + fadeColors.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FireworkEffect)) {
			return false;
		}

		FireworkEffect that = (FireworkEffect) obj;
		return this.flicker == that.flicker && this.trail == that.trail && this.type == that.type
				&& this.colors.equals(that.colors) && this.fadeColors.equals(that.fadeColors);
	}
}
