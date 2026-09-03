package org.bukkit.material;

import org.bukkit.Material;

public class PoweredRail extends ExtendedRails implements Redstone {
	public PoweredRail() {
		super(Material.POWERED_RAIL);
	}

	@Deprecated
	public PoweredRail(final int type) {
		super(type);
	}

	public PoweredRail(final Material type) {
		super(type);
	}

	@Deprecated
	public PoweredRail(final int type, final byte data) {
		super(type, data);
	}

	@Deprecated
	public PoweredRail(final Material type, final byte data) {
		super(type, data);
	}

	public boolean isPowered() {
		return (getData() & 0x8) == 0x8;
	}

	public void setPowered(boolean isPowered) {
		setData((byte) (isPowered ? (getData() | 0x8) : (getData() & ~0x8)));
	}

	@Override
	public PoweredRail clone() {
		return (PoweredRail) super.clone();
	}
}
