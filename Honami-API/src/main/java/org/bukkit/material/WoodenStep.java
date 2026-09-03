package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;

public class WoodenStep extends MaterialData {

	public WoodenStep() {
		super(Material.WOOD_STEP);
	}

	@Deprecated
	public WoodenStep(final int type) {
		super(type);
	}

	public WoodenStep(TreeSpecies species) {
		this();
		setSpecies(species);
	}

	public WoodenStep(TreeSpecies species, boolean inv) {
		this();
		setSpecies(species);
		setInverted(inv);
	}

	@Deprecated
	public WoodenStep(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public WoodenStep(final Material type, final byte data) {
		super(type, data);
	}

	public TreeSpecies getSpecies() {
		return TreeSpecies.getByData((byte) (getData() & 0x3));
	}

	public void setSpecies(TreeSpecies species) {
		setData((byte) ((getData() & 0xC) | species.getData()));
	}

	public boolean isInverted() {
		return ((getData() & 0x8) != 0);
	}

	public void setInverted(boolean inv) {
		int dat = getData() & 0x7;
		if (inv) {
			dat |= 0x8;
		}
		setData((byte) dat);
	}

	@Override
	public WoodenStep clone() {
		return (WoodenStep) super.clone();
	}

	@Override
	public String toString() {
		return super.toString() + " " + getSpecies() + (isInverted() ? " inverted" : "");
	}
}
