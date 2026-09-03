package org.bukkit.entity;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public interface ArmorStand extends LivingEntity {

	ItemStack getItemInHand();

	void setItemInHand(ItemStack item);

	ItemStack getBoots();

	void setBoots(ItemStack item);

	ItemStack getLeggings();

	void setLeggings(ItemStack item);

	ItemStack getChestplate();

	void setChestplate(ItemStack item);

	ItemStack getHelmet();

	void setHelmet(ItemStack item);

	EulerAngle getBodyPose();

	void setBodyPose(EulerAngle pose);

	EulerAngle getLeftArmPose();

	void setLeftArmPose(EulerAngle pose);

	EulerAngle getRightArmPose();

	void setRightArmPose(EulerAngle pose);

	EulerAngle getLeftLegPose();

	void setLeftLegPose(EulerAngle pose);

	EulerAngle getRightLegPose();

	void setRightLegPose(EulerAngle pose);

	EulerAngle getHeadPose();

	void setHeadPose(EulerAngle pose);

	boolean hasBasePlate();

	void setBasePlate(boolean basePlate);

	boolean hasGravity();

	void setGravity(boolean gravity);

	boolean isVisible();

	void setVisible(boolean visible);

	boolean hasArms();

	void setArms(boolean arms);

	boolean isSmall();

	void setSmall(boolean small);

	boolean isMarker();

	void setMarker(boolean marker);
}
