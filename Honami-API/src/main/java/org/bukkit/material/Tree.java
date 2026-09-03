package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockFace;

public class Tree extends MaterialData {
	public Tree() {
		super(Material.LOG);
	}

	public Tree(TreeSpecies species) {
		this();
		setSpecies(species);
	}

	public Tree(TreeSpecies species, BlockFace dir) {
		this();
		setSpecies(species);
		setDirection(dir);
	}

	@Deprecated
	public Tree(final int type) {
		super(type);
	}

	public Tree(final Material type) {
		super(type);
	}

	@Deprecated
	public Tree(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Tree(final Material type, final byte data) {
		super(type, data);
	}

	public TreeSpecies getSpecies() {
		return TreeSpecies.getByData((byte) (getData() & 0x3));
	}

	public void setSpecies(TreeSpecies species) {
		setData((byte) ((getData() & 0xC) | species.getData()));
	}

	public BlockFace getDirection() {
		switch ((getData() >> 2) & 0x3) {
		case 0: 
		default:
			return BlockFace.UP;
		case 1: 
			return BlockFace.WEST;
		case 2: 
			return BlockFace.NORTH;
		case 3: 
			return BlockFace.SELF;
		}
	}

	public void setDirection(BlockFace dir) {
		int dat;
		switch (dir) {
		case UP:
		case DOWN:
		default:
			dat = 0;
			break;
		case WEST:
		case EAST:
			dat = 1;
			break;
		case NORTH:
		case SOUTH:
			dat = 2;
			break;
		case SELF:
			dat = 3;
			break;
		}
		setData((byte) ((getData() & 0x3) | (dat << 2)));
	}

	@Override
	public String toString() {
		return getSpecies() + " " + getDirection() + " " + super.toString();
	}

	@Override
	public Tree clone() {
		return (Tree) super.clone();
	}
}
