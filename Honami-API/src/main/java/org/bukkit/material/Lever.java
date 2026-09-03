package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Lever extends SimpleAttachableMaterialData implements Redstone {
	public Lever() {
		super(Material.LEVER);
	}

	@Deprecated
	public Lever(final int type) {
		super(type);
	}

	public Lever(final Material type) {
		super(type);
	}

	@Deprecated
	public Lever(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Lever(final Material type, final byte data) {
		super(type, data);
	}

	public boolean isPowered() {
		return (getData() & 0x8) == 0x8;
	}

	public void setPowered(boolean isPowered) {
		setData((byte) (isPowered ? (getData() | 0x8) : (getData() & ~0x8)));
	}

	public BlockFace getAttachedFace() {
		byte data = (byte) (getData() & 0x7);

		switch (data) {
		case 0x1:
			return BlockFace.WEST;

		case 0x2:
			return BlockFace.EAST;

		case 0x3:
			return BlockFace.NORTH;

		case 0x4:
			return BlockFace.SOUTH;

		case 0x5:
		case 0x6:
			return BlockFace.DOWN;

		case 0x0:
		case 0x7:
			return BlockFace.UP;

		}

		return null;
	}

	public void setFacingDirection(BlockFace face) {
		byte data = (byte) (getData() & 0x8);
		BlockFace attach = getAttachedFace();

		if (attach == BlockFace.DOWN) {
			switch (face) {
			case SOUTH:
			case NORTH:
				data |= 0x5;
				break;

			case EAST:
			case WEST:
				data |= 0x6;
				break;
			}
		} else if (attach == BlockFace.UP) {
			switch (face) {
			case SOUTH:
			case NORTH:
				data |= 0x7;
				break;

			case EAST:
			case WEST:
				data |= 0x0;
				break;
			}
		} else {
			switch (face) {
			case EAST:
				data |= 0x1;
				break;

			case WEST:
				data |= 0x2;
				break;

			case SOUTH:
				data |= 0x3;
				break;

			case NORTH:
				data |= 0x4;
				break;
			}
		}
		setData(data);
	}

	@Override
	public String toString() {
		return super.toString() + " facing " + getFacing() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
	}

	@Override
	public Lever clone() {
		return (Lever) super.clone();
	}
}
