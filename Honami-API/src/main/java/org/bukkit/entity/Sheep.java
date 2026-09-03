package org.bukkit.entity;

import org.bukkit.material.Colorable;

public interface Sheep extends Animals, Colorable {

	public boolean isSheared();

	public void setSheared(boolean flag);
}
