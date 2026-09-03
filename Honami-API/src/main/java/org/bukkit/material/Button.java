package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class Button extends SimpleAttachableMaterialData implements Redstone {
	public Button() {
		super(Material.STONE_BUTTON);
	}

	@Deprecated
	public Button(final int type) {
		super(type);
	}

	public Button(final Material type) {
		super(type);
	}

	@Deprecated
	public Button(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Button(final Material type, final byte data) {
		super(type, data);
	}

	public boolean isPowered() {
		return (getData() & 0x8) == 0x8;
	}

	public void setPowered(boolean bool) {
		setData((byte) (bool ? (getData() | 0x8) : (getData() & ~0x8)));
	}

	public BlockFace getAttachedFace() {
		byte data = (byte) (getData() & 0x7);

		switch (data) {
		case 0x0:
			return BlockFace.UP;

		case 0x1:
			return BlockFace.WEST;

		case 0x2:
			return BlockFace.EAST;

		case 0x3:
			return BlockFace.NORTH;

		case 0x4:
			return BlockFace.SOUTH;

		case 0x5:
			return BlockFace.DOWN;
		}

		return null;
	}

	public void setFacingDirection(BlockFace face) {
		byte data = (byte) (getData() & 0x8);

		switch (face) {
		case DOWN:
			data |= 0x0;
			break;

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

		case UP:
			data |= 0x5;
			break;
		}

		setData(data);
	}

	@Override
	public String toString() {
		return super.toString() + " " + (isPowered() ? "" : "NOT ") + "POWERED";
	}

	@Override
	public Button clone() {
		return (Button) super.clone();
	}
}
