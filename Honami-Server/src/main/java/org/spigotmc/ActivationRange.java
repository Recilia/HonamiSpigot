package org.spigotmc;

import java.util.List;

import co.aikar.timings.SpigotTimings;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.Chunk;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityAmbient;
import net.minecraft.server.EntityAnimal;
import net.minecraft.server.EntityArrow;
import net.minecraft.server.EntityComplexPart;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.EntityCreeper;
import net.minecraft.server.EntityEnderCrystal;
import net.minecraft.server.EntityEnderDragon;
import net.minecraft.server.EntityFallingBlock;
import net.minecraft.server.EntityFireball;
import net.minecraft.server.EntityFireworks;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityMonster;
import net.minecraft.server.EntityProjectile;
import net.minecraft.server.EntitySheep;
import net.minecraft.server.EntitySlime;
import net.minecraft.server.EntityTNTPrimed;
import net.minecraft.server.EntityVillager;
import net.minecraft.server.EntityWeather;
import net.minecraft.server.EntityWither;
import net.minecraft.server.MathHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.World;

public class ActivationRange {

	static AxisAlignedBB maxBB = AxisAlignedBB.a(0, 0, 0, 0, 0, 0);
	static AxisAlignedBB miscBB = AxisAlignedBB.a(0, 0, 0, 0, 0, 0);
	static AxisAlignedBB animalBB = AxisAlignedBB.a(0, 0, 0, 0, 0, 0);
	static AxisAlignedBB monsterBB = AxisAlignedBB.a(0, 0, 0, 0, 0, 0);

	public static byte initializeEntityActivationType(Entity entity) {
		if (entity instanceof EntityMonster || entity instanceof EntitySlime) {
			return 1; 
		} else if (entity instanceof EntityCreature || entity instanceof EntityAmbient) {
			return 2; 
		} else {
			return 3; 
		}
	}

	public static boolean initializeEntityActivationState(Entity entity, SpigotWorldConfig config) {
		if ((entity.activationType == 3 && config.miscActivationRange == 0)
				|| (entity.activationType == 2 && config.animalActivationRange == 0)
				|| (entity.activationType == 1 && config.monsterActivationRange == 0) || entity instanceof EntityHuman
				|| entity instanceof EntityProjectile || entity instanceof EntityEnderDragon
				|| entity instanceof EntityComplexPart || entity instanceof EntityWither
				|| entity instanceof EntityFireball || entity instanceof EntityWeather
				|| entity instanceof EntityTNTPrimed || entity instanceof EntityFallingBlock 
																								
				|| entity instanceof EntityEnderCrystal || entity instanceof EntityFireworks) {
			return true;
		}

		return false;
	}

	public static void activateEntities(World world) {
		SpigotTimings.entityActivationCheckTimer.startTiming();
		final int miscActivationRange = world.spigotConfig.miscActivationRange;
		final int animalActivationRange = world.spigotConfig.animalActivationRange;
		final int monsterActivationRange = world.spigotConfig.monsterActivationRange;

		int maxRange = Math.max(monsterActivationRange, animalActivationRange);
		maxRange = Math.max(maxRange, miscActivationRange);
		maxRange = Math.min((world.spigotConfig.viewDistance << 4) - 8, maxRange);

		for (Entity player : (List<Entity>) (List) world.players) {

			player.activatedTick = MinecraftServer.currentTick;
			maxBB = player.getBoundingBox().grow(maxRange, 256, maxRange);
			miscBB = player.getBoundingBox().grow(miscActivationRange, 256, miscActivationRange);
			animalBB = player.getBoundingBox().grow(animalActivationRange, 256, animalActivationRange);
			monsterBB = player.getBoundingBox().grow(monsterActivationRange, 256, monsterActivationRange);

			int i = MathHelper.floor(maxBB.a / 16.0D);
			int j = MathHelper.floor(maxBB.d / 16.0D);
			int k = MathHelper.floor(maxBB.c / 16.0D);
			int l = MathHelper.floor(maxBB.f / 16.0D);

			for (int i1 = i; i1 <= j; ++i1) {
				for (int j1 = k; j1 <= l; ++j1) {
					Chunk chunk = world.getChunkIfLoaded(i1, j1);
					if (chunk != null) {
						activateChunkEntities(chunk);
					}
				}
			}
		}
		SpigotTimings.entityActivationCheckTimer.stopTiming();
	}

	private static void activateChunkEntities(Chunk chunk) {
		for (List<Entity> slice : chunk.entitySlices) {
			for (Entity entity : slice) {
				if (MinecraftServer.currentTick > entity.activatedTick) {
					if (entity.defaultActivationState) {
						entity.activatedTick = MinecraftServer.currentTick;
						continue;
					}
					switch (entity.activationType) {
					case 1:
						if (monsterBB.b(entity.getBoundingBox())) {
							entity.activatedTick = MinecraftServer.currentTick;
						}
						break;
					case 2:
						if (animalBB.b(entity.getBoundingBox())) {
							entity.activatedTick = MinecraftServer.currentTick;
						}
						break;
					case 3:
					default:
						if (miscBB.b(entity.getBoundingBox())) {
							entity.activatedTick = MinecraftServer.currentTick;
						}
					}
				}
			}
		}
	}

	public static boolean checkEntityImmunities(Entity entity) {
		
		if (entity.inWater || entity.fireTicks > 0) {
			return true;
		}
		if (!(entity instanceof EntityArrow)) {
			if (!entity.onGround || entity.passenger != null || entity.vehicle != null) {
				return true;
			}
		} else if (!((EntityArrow) entity).inGround) {
			return true;
		}
		
		if (entity instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) entity;
			if (  living.hurtTicks > 0
					|| living.effects.size() > 0) {
				return true;
			}
			if (entity instanceof EntityCreature && ((EntityCreature) entity).getGoalTarget() != null) {
				return true;
			}
			if (entity instanceof EntityVillager && ((EntityVillager) entity).cm()  ) {
				return true;
			}
			if (entity instanceof EntityAnimal) {
				EntityAnimal animal = (EntityAnimal) entity;
				if (animal.isBaby() || animal.isInLove()) {
					return true;
				}
				if (entity instanceof EntitySheep && ((EntitySheep) entity).isSheared()) {
					return true;
				}
			}
			if (entity instanceof EntityCreeper && ((EntityCreeper) entity).cn()) { 
				return true;
			}
		}
		return false;
	}

	public static boolean checkIfActive(Entity entity) {
		SpigotTimings.checkIfActiveTimer.startTiming();
		
		if (!entity.isAddedToChunk() || entity instanceof EntityFireworks || entity.loadChunks) { 
			SpigotTimings.checkIfActiveTimer.stopTiming();
			return true;
		}

		boolean isActive = entity.activatedTick >= MinecraftServer.currentTick || entity.defaultActivationState;

		if (!isActive) {
			if ((MinecraftServer.currentTick - entity.activatedTick - 1) % 20 == 0) {
				
				if (checkEntityImmunities(entity)) {
					
					entity.activatedTick = MinecraftServer.currentTick + 20;
				}
				isActive = true;
			}
			
		} else if (!entity.defaultActivationState && entity.ticksLived % 4 == 0 && !checkEntityImmunities(entity)) {
			isActive = false;
		}
		int x = MathHelper.floor(entity.locX);
		int z = MathHelper.floor(entity.locZ);
		
		Chunk chunk = entity.world.getChunkIfLoaded(x >> 4, z >> 4);
		if (isActive && !(chunk != null && chunk.areNeighborsLoaded(1))) {
			isActive = false;
		}
		SpigotTimings.checkIfActiveTimer.stopTiming();
		return isActive;
	}
}
