package org.bukkit.material;

import java.util.List;

import org.bukkit.Material;

public abstract class TexturedMaterial extends MaterialData {

	public TexturedMaterial(Material m) {
		super(m);
	}

	@Deprecated
	public TexturedMaterial(int type) {
		super(type);
	}

	@Deprecated
	public TexturedMaterial(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public TexturedMaterial(final Material type, final byte data) {
		super(type, data);
	}

	public abstract List<Material> getTextures();

	public Material getMaterial() {
		int n = getTextureIndex();
		if (n > getTextures().size() - 1) {
			n = 0;
		}

		return getTextures().get(n);
	}

	public void setMaterial(Material material) {
		if (getTextures().contains(material)) {
			setTextureIndex(getTextures().indexOf(material));
		} else {
			setTextureIndex(0x0);
		}
	}

	@Deprecated
	protected int getTextureIndex() {
		return getData(); 
	}

	@Deprecated
	protected void setTextureIndex(int idx) {
		setData((byte) idx); 
	}

	@Override
	public String toString() {
		return getMaterial() + " " + super.toString();
	}

	@Override
	public TexturedMaterial clone() {
		return (TexturedMaterial) super.clone();
	}
}
