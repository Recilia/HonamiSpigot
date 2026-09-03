package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Chest extends DirectionalContainer {

	public Chest() {
		super(Material.CHEST);
	}

	public Chest(BlockFace direction) {
		this();
		setFacingDirection(direction);
	}

	@Deprecated
	public Chest(final int type) {
		super(type);
	}

	public Chest(final Material type) {
		super(type);
	}

	@Deprecated
	public Chest(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Chest(final Material type, final byte data) {
		super(type, data);
	}

	@Override
	public Chest clone() {
		return (Chest) super.clone();
	}
}
