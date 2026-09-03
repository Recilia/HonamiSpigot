package org.bukkit;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public enum DyeColor {

	WHITE(0x0, 0xF, Color.WHITE, Color.fromRGB(0xF0F0F0)),

	ORANGE(0x1, 0xE, Color.fromRGB(0xD87F33), Color.fromRGB(0xEB8844)),

	MAGENTA(0x2, 0xD, Color.fromRGB(0xB24CD8), Color.fromRGB(0xC354CD)),

	LIGHT_BLUE(0x3, 0xC, Color.fromRGB(0x6699D8), Color.fromRGB(0x6689D3)),

	YELLOW(0x4, 0xB, Color.fromRGB(0xE5E533), Color.fromRGB(0xDECF2A)),

	LIME(0x5, 0xA, Color.fromRGB(0x7FCC19), Color.fromRGB(0x41CD34)),

	PINK(0x6, 0x9, Color.fromRGB(0xF27FA5), Color.fromRGB(0xD88198)),

	GRAY(0x7, 0x8, Color.fromRGB(0x4C4C4C), Color.fromRGB(0x434343)),

	SILVER(0x8, 0x7, Color.fromRGB(0x999999), Color.fromRGB(0xABABAB)),

	CYAN(0x9, 0x6, Color.fromRGB(0x4C7F99), Color.fromRGB(0x287697)),

	PURPLE(0xA, 0x5, Color.fromRGB(0x7F3FB2), Color.fromRGB(0x7B2FBE)),

	BLUE(0xB, 0x4, Color.fromRGB(0x334CB2), Color.fromRGB(0x253192)),

	BROWN(0xC, 0x3, Color.fromRGB(0x664C33), Color.fromRGB(0x51301A)),

	GREEN(0xD, 0x2, Color.fromRGB(0x667F33), Color.fromRGB(0x3B511A)),

	RED(0xE, 0x1, Color.fromRGB(0x993333), Color.fromRGB(0xB3312C)),

	BLACK(0xF, 0x0, Color.fromRGB(0x191919), Color.fromRGB(0x1E1B1B));

	private final byte woolData;
	private final byte dyeData;
	private final Color color;
	private final Color firework;
	private final static DyeColor[] BY_WOOL_DATA;
	private final static DyeColor[] BY_DYE_DATA;
	private final static Map<Color, DyeColor> BY_COLOR;
	private final static Map<Color, DyeColor> BY_FIREWORK;

	private DyeColor(final int woolData, final int dyeData, Color color, Color firework) {
		this.woolData = (byte) woolData;
		this.dyeData = (byte) dyeData;
		this.color = color;
		this.firework = firework;
	}

	@Deprecated
	public byte getData() {
		return getWoolData();
	}

	@Deprecated
	public byte getWoolData() {
		return woolData;
	}

	@Deprecated
	public byte getDyeData() {
		return dyeData;
	}

	public Color getColor() {
		return color;
	}

	public Color getFireworkColor() {
		return firework;
	}

	@Deprecated
	public static DyeColor getByData(final byte data) {
		return getByWoolData(data);
	}

	@Deprecated
	public static DyeColor getByWoolData(final byte data) {
		int i = 0xff & data;
		if (i >= BY_WOOL_DATA.length) {
			return null;
		}
		return BY_WOOL_DATA[i];
	}

	@Deprecated
	public static DyeColor getByDyeData(final byte data) {
		int i = 0xff & data;
		if (i >= BY_DYE_DATA.length) {
			return null;
		}
		return BY_DYE_DATA[i];
	}

	public static DyeColor getByColor(final Color color) {
		return BY_COLOR.get(color);
	}

	public static DyeColor getByFireworkColor(final Color color) {
		return BY_FIREWORK.get(color);
	}

	static {
		BY_WOOL_DATA = values();
		BY_DYE_DATA = values();
		ImmutableMap.Builder<Color, DyeColor> byColor = ImmutableMap.builder();
		ImmutableMap.Builder<Color, DyeColor> byFirework = ImmutableMap.builder();

		for (DyeColor color : values()) {
			BY_WOOL_DATA[color.woolData & 0xff] = color;
			BY_DYE_DATA[color.dyeData & 0xff] = color;
			byColor.put(color.getColor(), color);
			byFirework.put(color.getFireworkColor(), color);
		}

		BY_COLOR = byColor.build();
		BY_FIREWORK = byFirework.build();
	}
}
