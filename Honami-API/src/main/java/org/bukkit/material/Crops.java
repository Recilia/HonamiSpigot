package org.bukkit.material;

import org.bukkit.CropState;
import org.bukkit.Material;

public class Crops extends MaterialData {
	public Crops() {
		super(Material.CROPS);
	}

	public Crops(CropState state) {
		this();
		setState(state);
	}

	@Deprecated
	public Crops(final int type) {
		super(type);
	}

	public Crops(final Material type) {
		super(type);
	}

	@Deprecated
	public Crops(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Crops(final Material type, final byte data) {
		super(type, data);
	}

	public CropState getState() {
		return CropState.getByData(getData());
	}

	public void setState(CropState state) {
		setData(state.getData());
	}

	@Override
	public String toString() {
		return getState() + " " + super.toString();
	}

	@Override
	public Crops clone() {
		return (Crops) super.clone();
	}
}
