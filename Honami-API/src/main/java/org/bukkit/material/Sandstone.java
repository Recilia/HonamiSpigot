package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.SandstoneType;

public class Sandstone extends MaterialData {
	public Sandstone() {
		super(Material.SANDSTONE);
	}

	public Sandstone(SandstoneType type) {
		this();
		setType(type);
	}

	@Deprecated
	public Sandstone(final int type) {
		super(type);
	}

	public Sandstone(final Material type) {
		super(type);
	}

	@Deprecated
	public Sandstone(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Sandstone(final Material type, final byte data) {
		super(type, data);
	}

	public SandstoneType getType() {
		return SandstoneType.getByData(getData());
	}

	public void setType(SandstoneType type) {
		setData(type.getData());
	}

	@Override
	public String toString() {
		return getType() + " " + super.toString();
	}

	@Override
	public Sandstone clone() {
		return (Sandstone) super.clone();
	}
}
