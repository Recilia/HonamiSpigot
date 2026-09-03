package org.bukkit.entity;

import org.bukkit.material.MaterialData;

public interface Enderman extends Monster {

	public MaterialData getCarriedMaterial();

	public void setCarriedMaterial(MaterialData material);
}
