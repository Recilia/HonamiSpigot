package org.bukkit.entity;

public interface Boat extends Vehicle {

	public double getMaxSpeed();

	public void setMaxSpeed(double speed);

	public double getOccupiedDeceleration();

	public void setOccupiedDeceleration(double rate);

	public double getUnoccupiedDeceleration();

	public void setUnoccupiedDeceleration(double rate);

	public boolean getWorkOnLand();

	public void setWorkOnLand(boolean workOnLand);
}
