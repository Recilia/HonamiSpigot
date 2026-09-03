package org.bukkit.entity;

public interface Rabbit extends Animals {

	public Type getRabbitType();

	public void setRabbitType(Type type);

	public enum Type {

		BROWN,

		WHITE,

		BLACK,

		BLACK_AND_WHITE,

		GOLD,

		SALT_AND_PEPPER,

		THE_KILLER_BUNNY
	}
}
