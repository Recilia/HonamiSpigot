package org.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class Location implements Cloneable, ConfigurationSerializable {
	private World world;
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;

	public Location(final World world, final double x, final double y, final double z) {
		this(world, x, y, z, 0, 0);
	}

	public Location(final World world, final double x, final double y, final double z, final float yaw,
			final float pitch) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public Chunk getChunk() {
		return world.getChunkAt(this);
	}

	public Block getBlock() {
		return world.getBlockAt(this);
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public int getBlockX() {
		return locToBlock(x);
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public int getBlockY() {
		return locToBlock(y);
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double getZ() {
		return z;
	}

	public int getBlockZ() {
		return locToBlock(z);
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getYaw() {
		return yaw;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getPitch() {
		return pitch;
	}

	public Vector getDirection() {
		Vector vector = new Vector();

		double rotX = this.getYaw();
		double rotY = this.getPitch();

		vector.setY(-Math.sin(Math.toRadians(rotY)));

		double xz = Math.cos(Math.toRadians(rotY));

		vector.setX(-xz * Math.sin(Math.toRadians(rotX)));
		vector.setZ(xz * Math.cos(Math.toRadians(rotX)));

		return vector;
	}

	public Location setDirection(Vector vector) {

		final double _2PI = 2 * Math.PI;
		final double x = vector.getX();
		final double z = vector.getZ();

		if (x == 0 && z == 0) {
			pitch = vector.getY() > 0 ? -90 : 90;
			return this;
		}

		double theta = Math.atan2(-x, z);
		yaw = (float) Math.toDegrees((theta + _2PI) % _2PI);

		double x2 = NumberConversions.square(x);
		double z2 = NumberConversions.square(z);
		double xz = Math.sqrt(x2 + z2);
		pitch = (float) Math.toDegrees(Math.atan(-vector.getY() / xz));

		return this;
	}

	public Location add(Location vec) {
		if (vec == null || vec.getWorld() != getWorld()) {
			throw new IllegalArgumentException("Cannot add Locations of differing worlds");
		}

		x += vec.x;
		y += vec.y;
		z += vec.z;
		return this;
	}

	public Location add(Vector vec) {
		this.x += vec.getX();
		this.y += vec.getY();
		this.z += vec.getZ();
		return this;
	}

	public Location add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Location subtract(Location vec) {
		if (vec == null || vec.getWorld() != getWorld()) {
			throw new IllegalArgumentException("Cannot add Locations of differing worlds");
		}

		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		return this;
	}

	public Location subtract(Vector vec) {
		this.x -= vec.getX();
		this.y -= vec.getY();
		this.z -= vec.getZ();
		return this;
	}

	public Location subtract(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public double length() {
		return Math.sqrt(NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z));
	}

	public double lengthSquared() {
		return NumberConversions.square(x) + NumberConversions.square(y) + NumberConversions.square(z);
	}

	public double distance(Location o) {
		return Math.sqrt(distanceSquared(o));
	}

	public double distanceSquared(Location o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null location");
		} else if (o.getWorld() == null || getWorld() == null) {
			throw new IllegalArgumentException("Cannot measure distance to a null world");
		} else if (o.getWorld() != getWorld()) {
			throw new IllegalArgumentException(
					"Cannot measure distance between " + getWorld().getName() + " and " + o.getWorld().getName());
		}

		return NumberConversions.square(x - o.x) + NumberConversions.square(y - o.y)
				+ NumberConversions.square(z - o.z);
	}

	public Location multiply(double m) {
		x *= m;
		y *= m;
		z *= m;
		return this;
	}

	public Location zero() {
		x = 0;
		y = 0;
		z = 0;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Location other = (Location) obj;

		if (this.world != other.world && (this.world == null || !this.world.equals(other.world))) {
			return false;
		}
		if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
			return false;
		}
		if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
			return false;
		}
		if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
			return false;
		}
		if (Float.floatToIntBits(this.pitch) != Float.floatToIntBits(other.pitch)) {
			return false;
		}
		if (Float.floatToIntBits(this.yaw) != Float.floatToIntBits(other.yaw)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;

		hash = 19 * hash + (this.world != null ? this.world.hashCode() : 0);
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
		hash = 19 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
		hash = 19 * hash + Float.floatToIntBits(this.pitch);
		hash = 19 * hash + Float.floatToIntBits(this.yaw);
		return hash;
	}

	@Override
	public String toString() {
		return "Location{" + "world=" + world + ",x=" + x + ",y=" + y + ",z=" + z + ",pitch=" + pitch + ",yaw=" + yaw
				+ '}';
	}

	public Vector toVector() {
		return new Vector(x, y, z);
	}

	@Override
	public Location clone() {
		try {
			return (Location) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	public static int locToBlock(double loc) {
		return NumberConversions.floor(loc);
	}

	@Utility
	public Map<String, Object> serialize() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("world", this.world.getName());

		data.put("x", this.x);
		data.put("y", this.y);
		data.put("z", this.z);

		data.put("yaw", this.yaw);
		data.put("pitch", this.pitch);

		return data;
	}

	public static Location deserialize(Map<String, Object> args) {
		World world = Bukkit.getWorld((String) args.get("world"));
		if (world == null) {
			throw new IllegalArgumentException("unknown world");
		}

		return new Location(world, NumberConversions.toDouble(args.get("x")), NumberConversions.toDouble(args.get("y")),
				NumberConversions.toDouble(args.get("z")), NumberConversions.toFloat(args.get("yaw")),
				NumberConversions.toFloat(args.get("pitch")));
	}
}
