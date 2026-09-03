package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

public class Dye extends MaterialData implements Colorable {
	public Dye() {
		super(Material.INK_SACK);
	}

	@Deprecated
	public Dye(final int type) {
		super(type);
	}

	public Dye(final Material type) {
		super(type);
	}

	@Deprecated
	public Dye(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Dye(final Material type, final byte data) {
		super(type, data);
	}

	public Dye(final DyeColor color) {
		super(Material.INK_SACK, color.getDyeData());
	}

	public DyeColor getColor() {
		return DyeColor.getByDyeData(getData());
	}

	public void setColor(DyeColor color) {
		setData(color.getDyeData());
	}

	@Override
	public String toString() {
		return getColor() + " DYE(" + getData() + ")";
	}

	@Override
	public Dye clone() {
		return (Dye) super.clone();
	}
}
