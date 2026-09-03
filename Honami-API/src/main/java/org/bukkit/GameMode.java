package org.bukkit;

import java.util.Map;

import org.bukkit.entity.HumanEntity;

import com.google.common.collect.Maps;

public enum GameMode {

	CREATIVE(1),

	SURVIVAL(0),

	ADVENTURE(2),

	SPECTATOR(3);

	private final int value;
	private final static Map<Integer, GameMode> BY_ID = Maps.newHashMap();

	private GameMode(final int value) {
		this.value = value;
	}

	@Deprecated
	public int getValue() {
		return value;
	}

	@Deprecated
	public static GameMode getByValue(final int value) {
		return BY_ID.get(value);
	}

	static {
		for (GameMode mode : values()) {
			BY_ID.put(mode.getValue(), mode);
		}
	}
}
