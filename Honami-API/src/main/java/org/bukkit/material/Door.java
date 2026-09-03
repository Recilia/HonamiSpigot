package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;

public class Door extends MaterialData implements Directional, Openable {

	

	@Deprecated
	public Door() {
		super(Material.WOODEN_DOOR);
	}

	@Deprecated
	public Door(final int type) {
		super(type);
	}

	public Door(final Material type) {
		super(type);
	}

	public Door(final Material type, BlockFace face) {
		this(type, face, false);
	}

	public Door(final Material type, BlockFace face, boolean isOpen) {
		super(type);
		setTopHalf(false);
		setFacingDirection(face);
		setOpen(isOpen);
	}

	public Door(final Material type, boolean isHingeRight) {
		super(type);
		setTopHalf(true);
		setHinge(isHingeRight);
	}

	public Door(final TreeSpecies species, BlockFace face) {
		this(getWoodDoorOfSpecies(species), face, false);
	}

	public Door(final TreeSpecies species, BlockFace face, boolean isOpen) {
		this(getWoodDoorOfSpecies(species), face, isOpen);
	}

	public Door(final TreeSpecies species, boolean isHingeRight) {
		this(getWoodDoorOfSpecies(species), isHingeRight);
	}

	@Deprecated
	public Door(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Door(final Material type, final byte data) {
		super(type, data);
	}

	public static Material getWoodDoorOfSpecies(TreeSpecies species) {
		switch (species) {
		default:
		case GENERIC:
			return Material.WOODEN_DOOR;
		case BIRCH:
			return Material.BIRCH_DOOR;
		case REDWOOD:
			return Material.SPRUCE_DOOR;
		case JUNGLE:
			return Material.JUNGLE_DOOR;
		case ACACIA:
			return Material.ACACIA_DOOR;
		case DARK_OAK:
			return Material.DARK_OAK_DOOR;
		}
	}

	public boolean isOpen() {
		return ((getData() & 0x4) == 0x4);
	}

	public void setOpen(boolean isOpen) {
		setData((byte) (isOpen ? (getData() | 0x4) : (getData() & ~0x4)));
	}

	public boolean isTopHalf() {
		return ((getData() & 0x8) == 0x8);
	}

	public void setTopHalf(boolean isTopHalf) {
		setData((byte) (isTopHalf ? (getData() | 0x8) : (getData() & ~0x8)));
	}

	@Deprecated
	public BlockFace getHingeCorner() {
		return BlockFace.SELF;
	}

	@Override
	public String toString() {
		return (isTopHalf() ? "TOP" : "BOTTOM") + " half of " + super.toString();
	}

	public void setFacingDirection(BlockFace face) {
		byte data = (byte) (getData() & 0xC);
		switch (face) {
		case WEST:
			data |= 0x0;
			break;
		case NORTH:
			data |= 0x1;
			break;
		case EAST:
			data |= 0x2;
			break;
		case SOUTH:
			data |= 0x3;
			break;
		}
		setData(data);
	}

	public BlockFace getFacing() {
		byte data = (byte) (getData() & 0x3);
		switch (data) {
		case 0:
			return BlockFace.WEST;
		case 1:
			return BlockFace.NORTH;
		case 2:
			return BlockFace.EAST;
		case 3:
			return BlockFace.SOUTH;
		default:
			throw new IllegalStateException("Unknown door facing (data: " + data + ")");
		}
	}

	public boolean getHinge() {
		return (getData() & 0x1) == 1;
	}

	public void setHinge(boolean isHingeRight) {
		setData((byte) (isHingeRight ? (getData() | 0x1) : (getData() & ~0x1)));
	}

	@Override
	public Door clone() {
		return (Door) super.clone();
	}
}
