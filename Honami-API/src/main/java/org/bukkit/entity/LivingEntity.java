package org.bukkit.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import rein.honami.spigot.nacho.knockback.KnockbackProfile;

public interface LivingEntity extends Entity, Damageable, ProjectileSource {

	public double getEyeHeight();

	public double getEyeHeight(boolean ignoreSneaking);

	public Location getEyeLocation();

	@Deprecated
	public List<Block> getLineOfSight(HashSet<Byte> transparent, int maxDistance);

	public List<Block> getLineOfSight(Set<Material> transparent, int maxDistance);

	@Deprecated
	public Block getTargetBlock(HashSet<Byte> transparent, int maxDistance);

	public Block getTargetBlock(Set<Material> transparent, int maxDistance);

	@Deprecated
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> transparent, int maxDistance);

	public List<Block> getLastTwoTargetBlocks(Set<Material> transparent, int maxDistance);

	@Deprecated
	public Egg throwEgg();

	@Deprecated
	public Snowball throwSnowball();

	@Deprecated
	public Arrow shootArrow();

	public int getRemainingAir();

	public void setRemainingAir(int ticks);

	public int getMaximumAir();

	public void setMaximumAir(int ticks);

	public int getMaximumNoDamageTicks();

	public void setMaximumNoDamageTicks(int ticks);

	public double getLastDamage();

	@Deprecated
	public int _INVALID_getLastDamage();

	public void setLastDamage(double damage);

	@Deprecated
	public void _INVALID_setLastDamage(int damage);

	public int getNoDamageTicks();

	public void setNoDamageTicks(int ticks);

	KnockbackProfile getKnockbackProfile();

	void setKnockbackProfile(KnockbackProfile profile);

	public Player getKiller();

	public boolean addPotionEffect(PotionEffect effect);

	public boolean addPotionEffect(PotionEffect effect, boolean force);

	public boolean addPotionEffects(Collection<PotionEffect> effects);

	public boolean hasPotionEffect(PotionEffectType type);

	public void removePotionEffect(PotionEffectType type);

	public Collection<PotionEffect> getActivePotionEffects();

	public boolean hasLineOfSight(Entity other);

	

	public boolean hasLineOfSight(Location location);

	

	public boolean getRemoveWhenFarAway();

	public void setRemoveWhenFarAway(boolean remove);

	public EntityEquipment getEquipment();

	public void setCanPickupItems(boolean pickup);

	public boolean getCanPickupItems();

	public boolean isLeashed();

	public Entity getLeashHolder() throws IllegalStateException;

	public boolean setLeashHolder(Entity holder);

	

	int getArrowsStuck();

	void setArrowsStuck(int arrows);

	
	public boolean shouldBreakLeash();

	public void setShouldBreakLeash(boolean shouldBreakLeash);

	public boolean shouldPullWhileLeashed();

	public void setPullWhileLeashed(boolean pullWhileLeashed);
	
}
