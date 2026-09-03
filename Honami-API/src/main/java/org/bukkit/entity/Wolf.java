package org.bukkit.entity;

import org.bukkit.DyeColor;

public interface Wolf extends Animals, Tameable {

	public boolean isAngry();

	public void setAngry(boolean angry);

	public boolean isSitting();

	public void setSitting(boolean sitting);

	public DyeColor getCollarColor();

	public void setCollarColor(DyeColor color);
}
