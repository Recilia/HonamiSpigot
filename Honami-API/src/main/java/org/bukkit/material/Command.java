package org.bukkit.material;

import org.bukkit.Material;

public class Command extends MaterialData implements Redstone {
	public Command() {
		super(Material.COMMAND);
	}

	@Deprecated
	public Command(final int type) {
		super(type);
	}

	public Command(final Material type) {
		super(type);
	}

	@Deprecated
	public Command(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Command(final Material type, final byte data) {
		super(type, data);
	}

	public boolean isPowered() {
		return (getData() & 1) != 0;
	}

	public void setPowered(boolean bool) {
		setData((byte) (bool ? (getData() | 1) : (getData() & -2)));
	}

	@Override
	public String toString() {
		return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
	}

	@Override
	public Command clone() {
		return (Command) super.clone();
	}
}
