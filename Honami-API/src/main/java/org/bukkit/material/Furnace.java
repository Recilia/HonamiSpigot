package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Furnace extends FurnaceAndDispenser {

	public Furnace() {
		super(Material.FURNACE);
	}

	public Furnace(BlockFace direction) {
		this();
		setFacingDirection(direction);
	}

	@Deprecated
	public Furnace(final int type) {
		super(type);
	}

	public Furnace(final Material type) {
		super(type);
	}

	@Deprecated
	public Furnace(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Furnace(final Material type, final byte data) {
		super(type, data);
	}

	@Override
	public Furnace clone() {
		return (Furnace) super.clone();
	}
}
