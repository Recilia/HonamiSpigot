package org.bukkit.util;

public class EulerAngle {

	public static final EulerAngle ZERO = new EulerAngle(0, 0, 0);

	private final double x;
	private final double y;
	private final double z;

	public EulerAngle(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public EulerAngle setX(double x) {
		return new EulerAngle(x, y, z);
	}

	public EulerAngle setY(double y) {
		return new EulerAngle(x, y, z);
	}

	public EulerAngle setZ(double z) {
		return new EulerAngle(x, y, z);
	}

	public EulerAngle add(double x, double y, double z) {
		return new EulerAngle(this.x + x, this.y + y, this.z + z);
	}

	public EulerAngle subtract(double x, double y, double z) {
		return add(-x, -y, -z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		EulerAngle that = (EulerAngle) o;

		return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
