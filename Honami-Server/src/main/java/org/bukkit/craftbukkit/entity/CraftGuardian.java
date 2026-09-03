package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

import net.minecraft.server.EntityGuardian;
import net.minecraft.server.GenericAttributes;

public class CraftGuardian extends CraftMonster implements Guardian {

	public CraftGuardian(CraftServer server, EntityGuardian entity) {
		super(server, entity);
	}

	@Override
	public String toString() {
		return "CraftGuardian";
	}

	@Override
	public EntityType getType() {
		return EntityType.GUARDIAN;
	}

	@Override
	public boolean isElder() {
		return ((EntityGuardian) entity).isElder();
	}

	@Override
	public void setElder(boolean shouldBeElder) {
		EntityGuardian entityGuardian = (EntityGuardian) entity;

		if (!isElder() && shouldBeElder) {
			entityGuardian.setElder(true);
		} else if (isElder() && !shouldBeElder) {
			entityGuardian.setElder(false);

			
			entity.setSize(0.85F, 0.85F);

			
			
			entityGuardian.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(6.0D);
			entityGuardian.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.5D);
			entityGuardian.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16.0D);
			entityGuardian.getAttributeInstance(GenericAttributes.maxHealth).setValue(30.0D);

			entityGuardian.goalRandomStroll.setTimeBetweenMovement(80);

			entityGuardian.initAttributes();
		}
	}
}
