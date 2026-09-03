package org.bukkit;

public interface WorldBorder {

	public void reset();

	public double getSize();

	public void setSize(double newSize);

	public void setSize(double newSize, long seconds);

	public Location getCenter();

	public void setCenter(double x, double z);

	public void setCenter(Location location);

	public double getDamageBuffer();

	public void setDamageBuffer(double blocks);

	public double getDamageAmount();

	public void setDamageAmount(double damage);

	public int getWarningTime();

	public void setWarningTime(int seconds);

	public int getWarningDistance();

	public void setWarningDistance(int distance);
}
