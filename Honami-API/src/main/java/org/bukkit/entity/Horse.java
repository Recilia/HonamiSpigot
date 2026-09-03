package org.bukkit.entity;

import org.bukkit.inventory.HorseInventory;
import org.bukkit.inventory.InventoryHolder;

public interface Horse extends Animals, Vehicle, InventoryHolder, Tameable {

	public enum Variant {

		HORSE,

		DONKEY,

		MULE,

		UNDEAD_HORSE,

		SKELETON_HORSE,;
	}

	public enum Color {

		WHITE,

		CREAMY,

		CHESTNUT,

		BROWN,

		BLACK,

		GRAY,

		DARK_BROWN,;
	}

	public enum Style {

		NONE,

		WHITE,

		WHITEFIELD,

		WHITE_DOTS,

		BLACK_DOTS,;
	}

	public Variant getVariant();

	public void setVariant(Variant variant);

	public Color getColor();

	public void setColor(Color color);

	public Style getStyle();

	public void setStyle(Style style);

	public boolean isCarryingChest();

	public void setCarryingChest(boolean chest);

	public int getDomestication();

	public void setDomestication(int level);

	public int getMaxDomestication();

	public void setMaxDomestication(int level);

	public double getJumpStrength();

	public void setJumpStrength(double strength);

	@Override
	public HorseInventory getInventory();
}
