package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Stairs extends MaterialData implements Directional {

	@Deprecated
	public Stairs(final int type) {
		super(type);
	}

	public Stairs(final Material type) {
		super(type);
	}

	@Deprecated
	public Stairs(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Stairs(final Material type, final byte data) {
		super(type, data);
	}

	public BlockFace getAscendingDirection() {
		byte data = getData();

		switch (data & 0x3) {
		case 0x0:
		default:
			return BlockFace.EAST;

		case 0x1:
			return BlockFace.WEST;

		case 0x2:
			return BlockFace.SOUTH;

		case 0x3:
			return BlockFace.NORTH;
		}
	}

	public BlockFace getDescendingDirection() {
		return getAscendingDirection().getOppositeFace();
	}

	public void setFacingDirection(BlockFace face) {
		byte data;

		switch (face) {
		case NORTH:
			data = 0x3;
			break;

		case SOUTH:
			data = 0x2;
			break;

		case EAST:
		default:
			data = 0x0;
			break;

		case WEST:
			data = 0x1;
			break;
		}

		setData((byte) ((getData() & 0xC) | data));
	}

	public BlockFace getFacing() {
		return getDescendingDirection();
	}

	public boolean isInverted() {
		return ((getData() & 0x4) != 0);
	}

	public void setInverted(boolean inv) {
		int dat = getData() & 0x3;
		if (inv) {
			dat |= 0x4;
		}
		setData((byte) dat);
	}

	@Override
	public String toString() {
		return super.toString() + " facing " + getFacing() + (isInverted() ? " inverted" : "");
	}

	@Override
	public Stairs clone() {
		return (Stairs) super.clone();
	}
}
