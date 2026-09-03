package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum TreeSpecies {

	GENERIC(0x0),

	REDWOOD(0x1),

	BIRCH(0x2),

	JUNGLE(0x3),

	ACACIA(0x4),

	DARK_OAK(0x5),;

	private final byte data;
	private final static Map<Byte, TreeSpecies> BY_DATA = Maps.newHashMap();

	private TreeSpecies(final int data) {
		this.data = (byte) data;
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Deprecated
	public static TreeSpecies getByData(final byte data) {
		return BY_DATA.get(data);
	}

	static {
		for (TreeSpecies species : values()) {
			BY_DATA.put(species.data, species);
		}
	}
}
