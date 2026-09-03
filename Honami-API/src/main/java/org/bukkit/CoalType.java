package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum CoalType {
	COAL(0x0), CHARCOAL(0x1);

	private final byte data;
	private final static Map<Byte, CoalType> BY_DATA = Maps.newHashMap();

	private CoalType(final int data) {
		this.data = (byte) data;
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Deprecated
	public static CoalType getByData(final byte data) {
		return BY_DATA.get(data);
	}

	static {
		for (CoalType type : values()) {
			BY_DATA.put(type.data, type);
		}
	}
}
