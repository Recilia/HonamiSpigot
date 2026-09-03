package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum CropState {

	SEEDED(0x0),

	GERMINATED(0x1),

	VERY_SMALL(0x2),

	SMALL(0x3),

	MEDIUM(0x4),

	TALL(0x5),

	VERY_TALL(0x6),

	RIPE(0x7);

	private final byte data;
	private final static Map<Byte, CropState> BY_DATA = Maps.newHashMap();

	private CropState(final int data) {
		this.data = (byte) data;
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Deprecated
	public static CropState getByData(final byte data) {
		return BY_DATA.get(data);
	}

	static {
		for (CropState cropState : values()) {
			BY_DATA.put(cropState.getData(), cropState);
		}
	}
}
