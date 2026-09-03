package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialData implements Cloneable {
	private final int type;
	private byte data = 0;

	@Deprecated
	public MaterialData(final int type) {
		this(type, (byte) 0);
	}

	public MaterialData(final Material type) {
		this(type, (byte) 0);
	}

	@Deprecated
	public MaterialData(final int type, final byte data) {
		this.type = type;
		this.data = data;
	}

	@Deprecated
	public MaterialData(final Material type, final byte data) {
		this(type.getId(), data);
	}

	@Deprecated
	public byte getData() {
		return data;
	}

	@Deprecated
	public void setData(byte data) {
		this.data = data;
	}

	public Material getItemType() {
		return Material.getMaterial(type);
	}

	@Deprecated
	public int getItemTypeId() {
		return type;
	}

	public ItemStack toItemStack() {
		return new ItemStack(type, 0, data);
	}

	public ItemStack toItemStack(int amount) {
		return new ItemStack(type, amount, data);
	}

	@Override
	public String toString() {
		return getItemType() + "(" + getData() + ")";
	}

	@Override
	public int hashCode() {
		return ((getItemTypeId() << 8) ^ getData());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof MaterialData) {
			MaterialData md = (MaterialData) obj;

			return (md.getItemTypeId() == getItemTypeId() && md.getData() == getData());
		} else {
			return false;
		}
	}

	@Override
	public MaterialData clone() {
		try {
			return (MaterialData) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
