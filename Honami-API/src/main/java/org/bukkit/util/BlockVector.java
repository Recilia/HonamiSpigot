package org.bukkit.util;

import java.util.Map;

import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("BlockVector")
public class BlockVector extends Vector {

	public BlockVector() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public BlockVector(Vector vec) {
		this.x = vec.getX();
		this.y = vec.getY();
		this.z = vec.getZ();
	}

	public BlockVector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockVector(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BlockVector)) {
			return false;
		}
		BlockVector other = (BlockVector) obj;

		return (int) other.getX() == (int) this.x && (int) other.getY() == (int) this.y
				&& (int) other.getZ() == (int) this.z;

	}

	@Override
	public int hashCode() {
		return (Integer.valueOf((int) x).hashCode() >> 13) ^ (Integer.valueOf((int) y).hashCode() >> 7)
				^ Integer.valueOf((int) z).hashCode();
	}

	@Override
	public BlockVector clone() {
		return (BlockVector) super.clone();
	}

	public static BlockVector deserialize(Map<String, Object> args) {
		double x = 0;
		double y = 0;
		double z = 0;

		if (args.containsKey("x")) {
			x = (Double) args.get("x");
		}
		if (args.containsKey("y")) {
			y = (Double) args.get("y");
		}
		if (args.containsKey("z")) {
			z = (Double) args.get("z");
		}

		return new BlockVector(x, y, z);
	}
}
