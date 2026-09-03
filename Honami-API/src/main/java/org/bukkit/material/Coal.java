package org.bukkit.material;

import org.bukkit.CoalType;
import org.bukkit.Material;

public class Coal extends MaterialData {
	public Coal() {
		super(Material.COAL);
	}

	public Coal(CoalType type) {
		this();
		setType(type);
	}

	@Deprecated
	public Coal(final int type) {
		super(type);
	}

	public Coal(final Material type) {
		super(type);
	}

	@Deprecated
	public Coal(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Coal(final Material type, final byte data) {
		super(type, data);
	}

	public CoalType getType() {
		return CoalType.getByData(getData());
	}

	public void setType(CoalType type) {
		setData(type.getData());
	}

	@Override
	public String toString() {
		return getType() + " " + super.toString();
	}

	@Override
	public Coal clone() {
		return (Coal) super.clone();
	}
}
