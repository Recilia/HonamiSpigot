package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum GrassSpecies {

	DEAD(0x0),

	NORMAL(0x1),

	FERN_LIKE(0x2);

	private final byte data;
	private final static Map<Byte, GrassSpecies> BY_DATA = Maps.newHashMap();

	private GrassSpecies(final int data) {
		this.data = (byte) data;
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Deprecated
	public static GrassSpecies getByData(final byte data) {
		return BY_DATA.get(data);
	}

	static {
		for (GrassSpecies grassSpecies : values()) {
			BY_DATA.put(grassSpecies.getData(), grassSpecies);
		}
	}
}
