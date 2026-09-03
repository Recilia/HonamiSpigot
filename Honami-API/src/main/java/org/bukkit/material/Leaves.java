package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class Leaves extends MaterialData {
	public Leaves() {
		super(Material.LEAVES);
	}

	public Leaves(TreeSpecies species) {
		this();
		setSpecies(species);
	}

	@Deprecated
	public Leaves(final int type) {
		super(type);
	}

	public Leaves(final Material type) {
		super(type);
	}

	@Deprecated
	public Leaves(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Leaves(final Material type, final byte data) {
		super(type, data);
	}

	public TreeSpecies getSpecies() {
		return TreeSpecies.getByData((byte) (getData() & 3));
	}

	public void setSpecies(TreeSpecies species) {
		setData(species.getData());
	}

	@Override
	public String toString() {
		return getSpecies() + " " + super.toString();
	}

	@Override
	public Leaves clone() {
		return (Leaves) super.clone();
	}
}
