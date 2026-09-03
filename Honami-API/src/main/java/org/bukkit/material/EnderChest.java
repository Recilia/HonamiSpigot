package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class EnderChest extends DirectionalContainer {

	public EnderChest() {
		super(Material.ENDER_CHEST);
	}

	public EnderChest(BlockFace direction) {
		this();
		setFacingDirection(direction);
	}

	@Deprecated
	public EnderChest(final int type) {
		super(type);
	}

	public EnderChest(final Material type) {
		super(type);
	}

	@Deprecated
	public EnderChest(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public EnderChest(final Material type, final byte data) {
		super(type, data);
	}

	@Override
	public EnderChest clone() {
		return (EnderChest) super.clone();
	}
}
