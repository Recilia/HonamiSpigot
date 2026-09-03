package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class Wool extends MaterialData implements Colorable {
	public Wool() {
		super(Material.WOOL);
	}

	public Wool(DyeColor color) {
		this();
		setColor(color);
	}

	@Deprecated
	public Wool(final int type) {
		super(type);
	}

	public Wool(final Material type) {
		super(type);
	}

	@Deprecated
	public Wool(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Wool(final Material type, final byte data) {
		super(type, data);
	}

	public DyeColor getColor() {
		return DyeColor.getByWoolData(getData());
	}

	public void setColor(DyeColor color) {
		setData(color.getWoolData());
	}

	@Override
	public String toString() {
		return getColor() + " " + super.toString();
	}

	@Override
	public Wool clone() {
		return (Wool) super.clone();
	}
}
