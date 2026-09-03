package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum Difficulty {

	PEACEFUL(0),

	EASY(1),

	NORMAL(2),

	HARD(3);

	private final int value;
	private final static Map<Integer, Difficulty> BY_ID = Maps.newHashMap();

	private Difficulty(final int value) {
		this.value = value;
	}

	@Deprecated
	public int getValue() {
		return value;
	}

	@Deprecated
	public static Difficulty getByValue(final int value) {
		return BY_ID.get(value);
	}

	static {
		for (Difficulty diff : values()) {
			BY_ID.put(diff.value, diff);
		}
	}
}
