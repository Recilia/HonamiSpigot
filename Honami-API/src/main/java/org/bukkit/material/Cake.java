package org.bukkit.material;

import org.bukkit.Material;

public class Cake extends MaterialData {
	public Cake() {
		super(Material.CAKE_BLOCK);
	}

	@Deprecated
	public Cake(int type) {
		super(type);
	}

	public Cake(Material type) {
		super(type);
	}

	@Deprecated
	public Cake(int type, byte data) {
		super(type, data);
	}

	@Deprecated
	public Cake(Material type, byte data) {
		super(type, data);
	}

	public int getSlicesEaten() {
		return getData();
	}

	public int getSlicesRemaining() {
		return 6 - getData();
	}

	public void setSlicesEaten(int n) {
		if (n < 6) {
			setData((byte) n);
		} 
	}

	public void setSlicesRemaining(int n) {
		if (n > 6) {
			n = 6;
		}
		setData((byte) (6 - n));
	}

	@Override
	public String toString() {
		return super.toString() + " " + getSlicesEaten() + "/" + getSlicesRemaining() + " slices eaten/remaining";
	}

	@Override
	public Cake clone() {
		return (Cake) super.clone();
	}
}
