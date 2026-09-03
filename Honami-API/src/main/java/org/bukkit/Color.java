package org.bukkit;

import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.google.common.collect.ImmutableMap;

@SerializableAs("Color")
public final class Color implements ConfigurationSerializable {
	private static final int BIT_MASK = 0xff;

	public static final Color WHITE = fromRGB(0xFFFFFF);

	public static final Color SILVER = fromRGB(0xC0C0C0);

	public static final Color GRAY = fromRGB(0x808080);

	public static final Color BLACK = fromRGB(0x000000);

	public static final Color RED = fromRGB(0xFF0000);

	public static final Color MAROON = fromRGB(0x800000);

	public static final Color YELLOW = fromRGB(0xFFFF00);

	public static final Color OLIVE = fromRGB(0x808000);

	public static final Color LIME = fromRGB(0x00FF00);

	public static final Color GREEN = fromRGB(0x008000);

	public static final Color AQUA = fromRGB(0x00FFFF);

	public static final Color TEAL = fromRGB(0x008080);

	public static final Color BLUE = fromRGB(0x0000FF);

	public static final Color NAVY = fromRGB(0x000080);

	public static final Color FUCHSIA = fromRGB(0xFF00FF);

	public static final Color PURPLE = fromRGB(0x800080);

	public static final Color ORANGE = fromRGB(0xFFA500);

	private final byte red;
	private final byte green;
	private final byte blue;

	public static Color fromRGB(int red, int green, int blue) throws IllegalArgumentException {
		return new Color(red, green, blue);
	}

	public static Color fromBGR(int blue, int green, int red) throws IllegalArgumentException {
		return new Color(red, green, blue);
	}

	public static Color fromRGB(int rgb) throws IllegalArgumentException {
		Validate.isTrue((rgb >> 24) == 0, "Extrenuous data in: ", rgb);
		return fromRGB(rgb >> 16 & BIT_MASK, rgb >> 8 & BIT_MASK, rgb >> 0 & BIT_MASK);
	}

	public static Color fromBGR(int bgr) throws IllegalArgumentException {
		Validate.isTrue((bgr >> 24) == 0, "Extrenuous data in: ", bgr);
		return fromBGR(bgr >> 16 & BIT_MASK, bgr >> 8 & BIT_MASK, bgr >> 0 & BIT_MASK);
	}

	private Color(int red, int green, int blue) {
		Validate.isTrue(red >= 0 && red <= BIT_MASK, "Red is not between 0-255: ", red);
		Validate.isTrue(green >= 0 && green <= BIT_MASK, "Green is not between 0-255: ", green);
		Validate.isTrue(blue >= 0 && blue <= BIT_MASK, "Blue is not between 0-255: ", blue);

		this.red = (byte) red;
		this.green = (byte) green;
		this.blue = (byte) blue;
	}

	public int getRed() {
		return BIT_MASK & red;
	}

	public Color setRed(int red) {
		return fromRGB(red, getGreen(), getBlue());
	}

	public int getGreen() {
		return BIT_MASK & green;
	}

	public Color setGreen(int green) {
		return fromRGB(getRed(), green, getBlue());
	}

	public int getBlue() {
		return BIT_MASK & blue;
	}

	public Color setBlue(int blue) {
		return fromRGB(getRed(), getGreen(), blue);
	}

	public int asRGB() {
		return getRed() << 16 | getGreen() << 8 | getBlue() << 0;
	}

	public int asBGR() {
		return getBlue() << 16 | getGreen() << 8 | getRed() << 0;
	}

	
	
	public Color mixDyes(DyeColor... colors) {
		Validate.noNullElements(colors, "Colors cannot be null");

		Color[] toPass = new Color[colors.length];
		for (int i = 0; i < colors.length; i++) {
			toPass[i] = colors[i].getColor();
		}

		return mixColors(toPass);
	}

	
	
	public Color mixColors(Color... colors) {
		Validate.noNullElements(colors, "Colors cannot be null");

		int totalRed = this.getRed();
		int totalGreen = this.getGreen();
		int totalBlue = this.getBlue();
		int totalMax = Math.max(Math.max(totalRed, totalGreen), totalBlue);
		for (Color color : colors) {
			totalRed += color.getRed();
			totalGreen += color.getGreen();
			totalBlue += color.getBlue();
			totalMax += Math.max(Math.max(color.getRed(), color.getGreen()), color.getBlue());
		}

		float averageRed = totalRed / (colors.length + 1);
		float averageGreen = totalGreen / (colors.length + 1);
		float averageBlue = totalBlue / (colors.length + 1);
		float averageMax = totalMax / (colors.length + 1);

		float maximumOfAverages = Math.max(Math.max(averageRed, averageGreen), averageBlue);
		float gainFactor = averageMax / maximumOfAverages;

		return Color.fromRGB((int) (averageRed * gainFactor), (int) (averageGreen * gainFactor),
				(int) (averageBlue * gainFactor));
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Color)) {
			return false;
		}
		final Color that = (Color) o;
		return this.blue == that.blue && this.green == that.green && this.red == that.red;
	}

	@Override
	public int hashCode() {
		return asRGB() ^ Color.class.hashCode();
	}

	public Map<String, Object> serialize() {
		return ImmutableMap.<String, Object>of("RED", getRed(), "BLUE", getBlue(), "GREEN", getGreen());
	}

	@SuppressWarnings("javadoc")
	public static Color deserialize(Map<String, Object> map) {
		return fromRGB(asInt("RED", map), asInt("GREEN", map), asInt("BLUE", map));
	}

	private static int asInt(String string, Map<String, Object> map) {
		Object value = map.get(string);
		if (value == null) {
			throw new IllegalArgumentException(string + " not in map " + map);
		}
		if (!(value instanceof Number)) {
			throw new IllegalArgumentException(string + '(' + value + ") is not a number");
		}
		return ((Number) value).intValue();
	}

	@Override
	public String toString() {
		return "Color:[rgb0x" + Integer.toHexString(getRed()).toUpperCase()
				+ Integer.toHexString(getGreen()).toUpperCase() + Integer.toHexString(getBlue()).toUpperCase() + "]";
	}
}
