package org.bukkit.craftbukkit.entity;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import net.minecraft.server.EntityPotion;

public class CraftThrownPotion extends CraftProjectile implements ThrownPotion {
	public CraftThrownPotion(CraftServer server, EntityPotion entity) {
		super(server, entity);
	}

	
	@Override
	public Collection<PotionEffect> getEffects() {
		return Potion.getBrewer().getEffectsFromDamage(getHandle().getPotionValue());
	}

	@Override
	public ItemStack getItem() {
		
		getHandle().getPotionValue();

		return CraftItemStack.asBukkitCopy(getHandle().item);
	}

	@Override
	public void setItem(ItemStack item) {
		
		Validate.notNull(item, "ItemStack cannot be null.");

		Validate.isTrue(item.getType() == Material.POTION,
				"ItemStack must be a potion. This item stack was " + item.getType() + ".");

		getHandle().item = CraftItemStack.asNMSCopy(item);
	}

	@Override
	public EntityPotion getHandle() {
		return (EntityPotion) entity;
	}

	@Override
	public String toString() {
		return "CraftThrownPotion";
	}

	@Override
	public EntityType getType() {
		return EntityType.SPLASH_POTION;
	}
}
