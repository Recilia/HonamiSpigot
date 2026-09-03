package org.bukkit.material;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class Step extends TexturedMaterial {
	private static final List<Material> textures = new ArrayList<Material>();
	static {
		textures.add(Material.STONE);
		textures.add(Material.SANDSTONE);
		textures.add(Material.WOOD);
		textures.add(Material.COBBLESTONE);
		textures.add(Material.BRICK);
		textures.add(Material.SMOOTH_BRICK);
		textures.add(Material.NETHER_BRICK);
		textures.add(Material.QUARTZ_BLOCK);
	}

	public Step() {
		super(Material.STEP);
	}

	@Deprecated
	public Step(final int type) {
		super(type);
	}

	public Step(final Material type) {
		super((textures.contains(type)) ? Material.STEP : type);
		if (textures.contains(type)) {
			setMaterial(type);
		}
	}

	@Deprecated
	public Step(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public Step(final Material type, final byte data) {
		super(type, data);
	}

	@Override
	public List<Material> getTextures() {
		return textures;
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

	@Deprecated
	@Override
	protected int getTextureIndex() {
		return getData() & 0x7;
	}

	@Deprecated
	@Override
	protected void setTextureIndex(int idx) {
		setData((byte) ((getData() & 0x8) | idx));
	}

	@Override
	public Step clone() {
		return (Step) super.clone();
	}

	@Override
	public String toString() {
		return super.toString() + (isInverted() ? "inverted" : "");
	}
}
