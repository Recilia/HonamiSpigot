package org.bukkit.material;

import org.bukkit.Material;

public class PressurePlate extends MaterialData implements PressureSensor {
	public PressurePlate() {
		super(Material.WOOD_PLATE);
	}

	@Deprecated
	public PressurePlate(int type) {
		super(type);
	}

	public PressurePlate(Material type) {
		super(type);
	}

	@Deprecated
	public PressurePlate(int type, byte data) {
		super(type, data);
	}

	@Deprecated
	public PressurePlate(Material type, byte data) {
		super(type, data);
	}

	public boolean isPressed() {
		return getData() == 0x1;
	}

	@Override
	public String toString() {
		return super.toString() + (isPressed() ? " PRESSED" : "");
	}

	@Override
	public PressurePlate clone() {
		return (PressurePlate) super.clone();
	}
}
