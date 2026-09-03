package net.minecraft.server;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.player.PlayerTeleportEvent;

import rein.honami.spigot.cache.Constants;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.pearl.MinemenPearlMode;

public class EntityEnderPearl extends EntityProjectile {

	private EntityLiving c;

	public EntityEnderPearl(World world) {
		super(world);
		this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls; 
	}

	public EntityEnderPearl(World world, EntityLiving entityliving) {
		super(world, entityliving);
		this.c = entityliving;
		this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls; 
	}

	@Override
	protected void a(MovingObjectPosition movingobjectposition) {
		EntityLiving entityliving = this.getShooter();

		if (movingobjectposition.entity != null) {
			if (movingobjectposition.entity == this.c) {
				return;
			}

			if (HonamiConfig.minemenPearlMode && HonamiConfig.minemenNoDamage) {
				
			} else {
				movingobjectposition.entity.damageEntity(DamageSource.projectile(this, entityliving), 0.0F);
			}
		}

		if (this.inUnloadedChunk && world.paperSpigotConfig.removeUnloadedEnderPearls) {
			this.die();
		}

		
		BlockPosition blockPosition = movingobjectposition.a();

		if (blockPosition != null) {
			IBlockData blockData = world.getType(blockPosition);
			Block block = blockData.getBlock();
			boolean collides = HonamiConfig.pearlPassthroughTripwire && block == Blocks.TRIPWIRE
					|| HonamiConfig.pearlPassthroughCobweb && block == Blocks.WEB
					|| HonamiConfig.pearlPassthroughBed && block == Blocks.BED
					|| HonamiConfig.pearlPassthroughFenceGate
							&& (block == Blocks.FENCE_GATE || block == Blocks.SPRUCE_FENCE_GATE
									|| block == Blocks.BIRCH_FENCE_GATE || block == Blocks.JUNGLE_FENCE_GATE
									|| block == Blocks.DARK_OAK_FENCE_GATE || block == Blocks.ACACIA_FENCE_GATE)
							&& blockData.get(BlockFenceGate.OPEN).booleanValue()
					|| HonamiConfig.pearlPassthroughSlab && (block == Blocks.STONE_SLAB
							|| block == Blocks.WOODEN_SLAB || block == Blocks.STONE_SLAB2)
					|| HonamiConfig.pearlPassthroughFence && block instanceof BlockFence
					|| HonamiConfig.pearlPassthroughStair && block instanceof BlockStairs
					|| HonamiConfig.pearlPassthroughCarpet && block == Blocks.CARPET
					|| HonamiConfig.pearlPassthroughButton && block instanceof BlockButtonAbstract
					|| HonamiConfig.pearlPassthroughFlower && block instanceof BlockFlowers
					|| HonamiConfig.pearlPassthroughGrass && block == Blocks.GRASS
					|| HonamiConfig.pearlPassthroughSnow && block == Blocks.SNOW_LAYER
					|| HonamiConfig.pearlPassthroughRail && block instanceof BlockMinecartTrack
					|| HonamiConfig.pearlPassthroughAnvil && block instanceof BlockAnvil
					|| HonamiConfig.pearlPassthroughEnchantTable && block == Blocks.ENCHANTING_TABLE
					|| HonamiConfig.pearlPassthroughDragonEgg && block == Blocks.DRAGON_EGG
					|| HonamiConfig.pearlPassthroughPortal && block == Blocks.PORTAL;

			if (collides) {
				return;
			}
		}

		for (int i = 0; i < 32; ++i) {
			this.world.addParticle(EnumParticle.PORTAL, this.locX, this.locY + this.random.nextDouble() * 2.0D,
					this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian(), Constants.EMPTY_ARRAY);
		}

		if (!this.world.isClientSide) {
			if (entityliving instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityliving;

				if (entityplayer.playerConnection.a().isConnected() && entityplayer.world == this.world
						&& !entityplayer.isSleeping()) {
					
					org.bukkit.craftbukkit.entity.CraftPlayer player = entityplayer.getBukkitEntity();
					org.bukkit.Location location = getBukkitEntity().getLocation();
					location.setPitch(player.getLocation().getPitch());
					location.setYaw(player.getLocation().getYaw());

					if (HonamiConfig.antiEnderPearlGlitch) {

						double diffX = location.getBlockX() - player.getLocation().getBlockX();
						double diffY = location.getBlockY() - player.getLocation().getBlockY();
						double diffZ = location.getBlockZ() - player.getLocation().getBlockZ();

						if (diffY <= 0) {
							location.setY(location.getBlockY() + 0.5D);
						} else {
							location.setY(location.getBlockY() - 0.5D);
							if (diffX <= 0) {
								location.setX(location.getBlockX() + 0.5D);
							} else {
								location.setX(location.getBlockX() - 0.5D);
							}
							if (diffZ <= 0) {
								location.setZ(location.getBlockZ() + 0.5D);
							} else {
								location.setZ(location.getBlockZ() - 0.5D);
							}
						}

					}

					PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location,
							PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
					Bukkit.getPluginManager().callEvent(teleEvent);

					if (!teleEvent.isCancelled() && !entityplayer.playerConnection.isDisconnected()) {
						if ((this.random.nextFloat() < 0.05F) && (this.world.getGameRules().getBoolean("doMobSpawning"))
								&& (world.nachoSpigotConfig.endermiteSpawning)) {
							EntityEndermite entityendermite = new EntityEndermite(this.world);

							entityendermite.a(true);
							entityendermite.setPositionRotation(entityliving.locX, entityliving.locY, entityliving.locZ,
									entityliving.yaw, entityliving.pitch);
							this.world.addEntity(entityendermite);
						}

						if (entityliving.au()) {
							entityliving.mount((Entity) null);
						}

						if (HonamiConfig.minemenPearlMode) {
							MinemenPearlMode.scheduleTeleport(entityplayer, teleEvent.getTo(), HonamiConfig.minemenTeleportDelay);
						} else {
							entityplayer.playerConnection.teleport(teleEvent.getTo());
							entityliving.fallDistance = 0.0F;
							CraftEventFactory.entityDamage = this;
							entityliving.damageEntity(DamageSource.FALL, 5.0F);
							CraftEventFactory.entityDamage = null;
						}
					}
					
				}
			} else if (entityliving != null) {
				entityliving.enderTeleportTo(this.locX, this.locY, this.locZ);
				entityliving.fallDistance = 0.0F;
			}

			this.die();
		}

	}

	@Override
	public void t_() {
		EntityLiving entityliving = this.getShooter();

		if (entityliving != null && entityliving instanceof EntityHuman && !entityliving.isAlive()) {
			this.die();
		} else {
			super.t_();
		}

	}
}
