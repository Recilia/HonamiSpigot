package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;

public enum WorldType {
	NORMAL("DEFAULT"), FLAT("FLAT"), VERSION_1_1("DEFAULT_1_1"), LARGE_BIOMES("LARGEBIOMES"), AMPLIFIED("AMPLIFIED"),
	CUSTOMIZED("CUSTOMIZED");

	private final static Map<String, WorldType> BY_NAME = Maps.newHashMap();
	private final String name;

	private WorldType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static WorldType getByName(String name) {
		return BY_NAME.get(name.toUpperCase());
	}

	static {
		for (WorldType type : values()) {
			BY_NAME.put(type.name, type);
		}
	}
}
