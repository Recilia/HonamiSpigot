package org.bukkit.material;

import org.bukkit.Material;

public class RedstoneTorch extends Torch implements Redstone {
	public RedstoneTorch() {
		super(Material.REDSTONE_TORCH_ON);
	}

	@Deprecated
	public RedstoneTorch(final int type) {
		super(type);
	}

	public RedstoneTorch(final Material type) {
		super(type);
	}

	@Deprecated
	public RedstoneTorch(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public RedstoneTorch(final Material type, final byte data) {
		super(type, data);
	}

	public boolean isPowered() {
		return getItemType() == Material.REDSTONE_TORCH_ON;
	}

	@Override
	public String toString() {
		return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
	}

	@Override
	public RedstoneTorch clone() {
		return (RedstoneTorch) super.clone();
	}
}
