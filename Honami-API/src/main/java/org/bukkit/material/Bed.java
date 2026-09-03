package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Bed extends MaterialData implements Directional {

	public Bed() {
		super(Material.BED_BLOCK);
	}

	public Bed(BlockFace direction) {
		this();
		setFacingDirection(direction);
	}

	@Deprecated
	public Bed(final int type) {
		super(type);
	}

	public Bed(final Material type) {
		super(type);
	}

	@Deprecated
	public Bed(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Bed(final Material type, final byte data) {
		super(type, data);
	}

	public boolean isHeadOfBed() {
		return (getData() & 0x8) == 0x8;
	}

	public void setHeadOfBed(boolean isHeadOfBed) {
		setData((byte) (isHeadOfBed ? (getData() | 0x8) : (getData() & ~0x8)));
	}

	public void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
		case SOUTH:
			data = 0x0;
			break;

		case WEST:
			data = 0x1;
			break;

		case NORTH:
			data = 0x2;
			break;

		case EAST:
		default:
			data = 0x3;
		}

		if (isHeadOfBed()) {
			data |= 0x8;
		}

		setData(data);
	}

	public BlockFace getFacing() {
		byte data = (byte) (getData() & 0x7);

		switch (data) {
		case 0x0:
			return BlockFace.SOUTH;

		case 0x1:
			return BlockFace.WEST;

		case 0x2:
			return BlockFace.NORTH;

		case 0x3:
		default:
			return BlockFace.EAST;
		}
	}

	@Override
	public String toString() {
		return (isHeadOfBed() ? "HEAD" : "FOOT") + " of " + super.toString() + " facing " + getFacing();
	}

	@Override
	public Bed clone() {
		return (Bed) super.clone();
	}
}
