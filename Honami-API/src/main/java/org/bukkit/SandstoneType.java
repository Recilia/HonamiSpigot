package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum SandstoneType {
	CRACKED(0x0), GLYPHED(0x1), SMOOTH(0x2);

	private final byte data;
	private final static Map<Byte, SandstoneType> BY_DATA = Maps.newHashMap();

	private SandstoneType(final int data) {
		this.data = (byte) data;
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Deprecated
	public static SandstoneType getByData(final byte data) {
		return BY_DATA.get(data);
	}

	static {
		for (SandstoneType type : values()) {
			BY_DATA.put(type.data, type);
		}
	}
}
