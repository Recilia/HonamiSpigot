package net.minecraft.server;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import rein.honami.spigot.async.AsyncUtil;
import rein.honami.spigot.cache.Constants;
import rein.honami.spigot.config.HonamiConfig;

import rein.honami.spigot.nacho.async.AsyncExplosions;

public class Explosion {

	private final boolean a;
	private final boolean b;
	private final World world;
	private final double posX;
	private final double posY;
	private final double posZ;
	public final Entity source;
	private final float size;
	private final List<BlockPosition> blocks = Lists.newArrayList();
	private final Map<EntityHuman, Vec3D> k = Maps.newHashMap();
	public boolean wasCanceled = false; 

	public Explosion(World world, Entity entity, double d0, double d1, double d2, float f, boolean flag,
			boolean flag1) {
		this.world = world;
		this.source = entity;
		this.size = (float) Math.max(f, 0.0); 
		this.posX = d0;
		this.posY = d1;
		this.posZ = d2;
		this.a = flag;
		this.b = flag1;
	}

	public void a() {
		
		if (this.size < 0.1F) {
			return;
		}

		int i;
		int j;

		BlockPosition pos = new BlockPosition(posX, posY, posZ);
		Chunk chunk = world.getChunkAt(pos.getX() >> 4, pos.getZ() >> 4);
		Block b = chunk.getBlockData(pos).getBlock(); 

		if (!this.world.honamiTacoConfig.optimizeLiquidExplosions || !b.getMaterial().isLiquid()) { 

																									
            it.unimi.dsi.fastutil.longs.LongSet set = new it.unimi.dsi.fastutil.longs.LongOpenHashSet();
            searchForBlocks(set, chunk);
            for (it.unimi.dsi.fastutil.longs.LongIterator iterator = set.iterator(); iterator.hasNext(); ) {
                this.blocks.add(BlockPosition.fromLong(iterator.nextLong()));
            }
		}

		float f3 = this.size * 2.0F;

		i = MathHelper.floor(this.posX - f3 - 1.0D) >> 4;
		j = MathHelper.floor(this.posX + f3 + 1.0D) >> 4;
		int l = MathHelper.clamp(MathHelper.floor(this.posY - f3 - 1.0D) >> 4, 0, 15);
		int i1 = MathHelper.clamp(MathHelper.floor(this.posY + f3 + 1.0D) >> 4, 0, 15);
		int j1 = MathHelper.floor(this.posZ - f3 - 1.0D) >> 4;
		int k1 = MathHelper.floor(this.posZ + f3 + 1.0D) >> 4;

		
		
		Vec3D vec3d = new Vec3D(this.posX, this.posY, this.posZ);

		for (int chunkX = i; chunkX <= j; ++chunkX) {
			for (int chunkZ = j1; chunkZ <= k1; ++chunkZ) {
				chunk = world.getChunkIfLoaded(chunkX, chunkZ);

				if (chunk == null) {
					continue;
				}

				for (int chunkY = l; chunkY <= i1; ++chunkY) {
					affectEntities(chunk.entitySlices[chunkY], vec3d, f3);
				}
			}
		}
	}

	public void affectEntities(List<Entity> list, Vec3D vec3d, float f3) {
		double maxDistSq = (double) (f3 * f3); 
		for (Entity entity : list) {
			if (!entity.aW()) {
				if (!entity.dead) {
					double d8 = entity.locX - this.posX;
					double d9 = entity.locY + entity.getHeadHeight() - this.posY;
					double d10 = entity.locZ - this.posZ;
					double distanceSquared = d8 * d8 + d9 * d9 + d10 * d10;

					if (distanceSquared <= maxDistSq && distanceSquared != 0.0D) {
						double d11 = MathHelper.sqrt(distanceSquared);
						double d7 = d11 / f3;
						
						d8 /= d11;
						d9 /= d11;
						d10 /= d11;

						
						double finalD = d8;
						double finalD1 = d9;
						double finalD11 = d10;

						if (HonamiConfig.asyncTnt) {
							this.getBlockDensityAsync(vec3d, entity.getBoundingBox())
									.thenAccept((d12) -> AsyncUtil.runPostTick(() -> {
										processEntityKnockback(entity, d7, finalD, finalD1, finalD11, f3, d12);
									}));
						} else {
							processEntityKnockback(entity, d7, finalD, finalD1, finalD11, f3,
									this.getBlockDensitySync(vec3d, entity.getBoundingBox()));
						}
						
					}
				}
			}
		}
	}

	private void processEntityKnockback(Entity entity, double d7, double finalD, double finalD1, double finalD11, float f3, double d12) {
		double d13 = (1.0D - d7) * d12;
		if (d13 < 0.0D) {
			return; 
		}

		if (entity.isCannoningEntity) {
			entity.g(finalD * d13, finalD1 * d13, finalD11 * d13);
			return;
		}

		
		
		CraftEventFactory.entityDamage = source;
		entity.forceExplosionKnockback = false;
		boolean wasDamaged = entity.damageEntity(DamageSource.explosion(this),
				((int) ((d13 * d13 + d13) / 2.0D * 8.0D * f3 + 1.0D)));
		CraftEventFactory.entityDamage = null;

		if (!wasDamaged
				&& !(entity instanceof EntityTNTPrimed
						|| entity instanceof EntityFallingBlock)
				&& !entity.forceExplosionKnockback) {
			return;
		}

		double d14 = entity instanceof EntityHuman
				&& world.paperSpigotConfig.disableExplosionKnockback ? 0
						: EnchantmentProtection.a(entity, d13); 

		
		
		entity.g(finalD * d14, finalD1 * d14, finalD11 * d14);

		if (entity instanceof EntityHuman
				&& !((EntityHuman) entity).abilities.isInvulnerable
				&& !world.paperSpigotConfig.disableExplosionKnockback) { 
			this.k.put((EntityHuman) entity,
					new Vec3D(finalD * d13, finalD1 * d13, finalD11 * d13));
		}
	}

	public void a(boolean flag) {
		
		float volume = source instanceof EntityTNTPrimed ? world.paperSpigotConfig.tntExplosionVolume : 4.0F;

		if (HonamiConfig.explosionSounds) {
			this.world.makeSound(this.posX, this.posY, this.posZ, "random.explode", volume,
					(1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F);
		}

		

		if (HonamiConfig.explosionAnimation) {
			if (this.size >= 2.0F && this.b) {
				this.world.addParticle(EnumParticle.EXPLOSION_HUGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D,
						Constants.EMPTY_ARRAY);
			} else {
				this.world.addParticle(EnumParticle.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D,
						Constants.EMPTY_ARRAY);
			}
		}

		Iterator iterator;
		BlockPosition blockposition;

		if (this.b) {
			
			org.bukkit.World bworld = this.world.getWorld();
			org.bukkit.entity.Entity explode = this.source == null ? null : this.source.getBukkitEntity();
			Location location = new Location(bworld, this.posX, this.posY, this.posZ);

			List<org.bukkit.block.Block> blockList = Lists.newArrayList();
			for (int i1 = this.blocks.size() - 1; i1 >= 0; i1--) {
				BlockPosition cpos = this.blocks.get(i1);
				org.bukkit.block.Block bblock = bworld.getBlockAt(cpos.getX(), cpos.getY(), cpos.getZ());
				if (bblock.getType() != org.bukkit.Material.AIR) {
					blockList.add(bblock);
				}
			}

            boolean cancelled = false;
            List<org.bukkit.block.Block> bukkitBlocks = blockList;
            float yield = 0.3F; 

			if (explode != null) {
                EntityExplodeEvent event = new EntityExplodeEvent(explode, location, blockList, yield);
				if (HonamiConfig.fireEntityExplodeEvent) {
                    this.world.getServer().getPluginManager().callEvent(event);
                }
                cancelled = event.isCancelled();
                bukkitBlocks = event.blockList();
                yield = event.getYield();
			} else {
                BlockExplodeEvent event = new BlockExplodeEvent(location.getBlock(), blockList, yield);
				this.world.getServer().getPluginManager().callEvent(event);
				cancelled = event.isCancelled();
				bukkitBlocks = event.blockList();
				yield = event.getYield();
			}

			this.blocks.clear();

			for (org.bukkit.block.Block bblock : bukkitBlocks) {
				BlockPosition coords = new BlockPosition(bblock.getX(), bblock.getY(), bblock.getZ());
				blocks.add(coords);
			}

			if (cancelled) {
				this.wasCanceled = true;
				return;
			}
			
			iterator = this.blocks.iterator();

			while (iterator.hasNext()) {
				blockposition = (BlockPosition) iterator.next();
				Block block = this.world.getType(blockposition).getBlock();

				world.spigotConfig.antiXrayInstance.updateNearbyBlocks(world, blockposition); 

				

				if (block.getMaterial() != Material.AIR) {
					if (block.a(this)) {
						
						block.dropNaturally(this.world, blockposition, this.world.getType(blockposition), yield, 0);
					}

					this.world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
					block.wasExploded(this.world, blockposition, this);
				}
			}
		}

		if (this.a) {
			iterator = this.blocks.iterator();

			while (iterator.hasNext()) {
				blockposition = (BlockPosition) iterator.next();
				
				if (this.world.getType(blockposition).getBlock().getMaterial() == Material.AIR
						&& this.world.getType(blockposition.down()).getBlock().o() && ThreadLocalRandom.current().nextInt(3) == 0) { 
					
					if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(this.world,
							blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
						this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
					}
					
				}
			}
		}

	}

	public Map<EntityHuman, Vec3D> b() {
		return this.k;
	}

	public EntityLiving getSource() {
		
		return this.source == null ? null
				: (this.source instanceof EntityTNTPrimed ? ((EntityTNTPrimed) this.source).getSource()
						: (this.source instanceof EntityLiving ? (EntityLiving) this.source
								: (this.source instanceof EntityFireball ? ((EntityFireball) this.source).shooter
										: null)));
		
	}

	public void clearBlocks() {
		this.blocks.clear();
	}

	public List<BlockPosition> getBlocks() {
		return this.blocks;
	}

	private final static List<double[]> VECTORS = Lists.newArrayListWithCapacity(1352);

	static {
		for (int k = 0; k < 16; ++k) {
			for (int i = 0; i < 16; ++i) {
				for (int j = 0; j < 16; ++j) {
					if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
						double d0 = k / 15.0F * 2.0F - 1.0F;
						double d1 = i / 15.0F * 2.0F - 1.0F;
						double d2 = j / 15.0F * 2.0F - 1.0F;
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

						d0 = (d0 / d3) * 0.30000001192092896D;
						d1 = (d1 / d3) * 0.30000001192092896D;
						d2 = (d2 / d3) * 0.30000001192092896D;
						VECTORS.add(new double[] { d0, d1, d2 });
					}
				}
			}
		}
	}

	private void searchForBlocks(it.unimi.dsi.fastutil.longs.LongSet set, Chunk chunk) {
		BlockPosition.MutableBlockPosition position = new BlockPosition.MutableBlockPosition();

		for (double[] vector : VECTORS) {
			double d0 = vector[0];
			double d1 = vector[1];
			double d2 = vector[2];

			float f = this.size * (0.7F
					+ (world.nachoSpigotConfig.constantExplosions ? 0.7F : this.world.random.nextFloat()) * 0.6F);
			float resistance = 0;

			double stepX = this.posX;
			double stepY = this.posY;
			double stepZ = this.posZ;

			for (; f > 0.0F; f -= 0.22500001F) {
				int floorX = org.bukkit.util.NumberConversions.floor(stepX);
				int floorY = org.bukkit.util.NumberConversions.floor(stepY);
				int floorZ = org.bukkit.util.NumberConversions.floor(stepZ);

				if (position.getX() != floorX || position.getY() != floorY || position.getZ() != floorZ) {
					position.setValues(floorX, floorY, floorZ);

					int chunkX = floorX >> 4;
					int chunkZ = floorZ >> 4;
					if (chunk == null || !chunk.o() || chunk.locX != chunkX || chunk.locZ != chunkZ) {
						chunk = world.getChunkAt(chunkX, chunkZ);
					}

					IBlockData iblockdata = chunk.getBlockData(position);
					Block block = iblockdata.getBlock();

					if (block != Blocks.AIR) {
						float blockResistance = block.durability / 5.0f;
						resistance = (blockResistance + 0.3F) * 0.3F;
						f -= resistance;

						if (f > 0.0F
								&& (this.source == null || this.source.a(this, this.world, position, iblockdata, f))
								&& position.getY() < 256 && position.getY() >= 0) { 
																					
							set.add(position.asLong());
						}
					}
				} else {
					f -= resistance;
				}

				stepX += d0;
				stepY += d1;
				stepZ += d2;
			}
		}
	}

	
	private CompletableFuture<Float> getBlockDensityAsync(Vec3D vec3d, AxisAlignedBB aabb) {
		return CompletableFuture.supplyAsync(() -> {
			
			int key = createKey(this, aabb);
			float blockDensity = this.world.explosionDensityCache.get(key);
			if (blockDensity == -1.0f) {
				blockDensity = calculateDensity(vec3d, aabb);
				this.world.explosionDensityCache.put(key, blockDensity);
			}
			return blockDensity;
		}, AsyncExplosions.EXECUTOR);
	}

	private float getBlockDensitySync(Vec3D vec3d, AxisAlignedBB aabb) {
		
		int key = createKey(this, aabb);
		float blockDensity = this.world.explosionDensityCache.get(key);
		if (blockDensity == -1.0f) {
			blockDensity = calculateDensity(vec3d, aabb);
			this.world.explosionDensityCache.put(key, blockDensity);
		}
		return blockDensity;
	}

	private float calculateDensity(Vec3D vec3d, AxisAlignedBB aabb) {
		if (world.nachoSpigotConfig.reducedDensityRays) {
			return calculateDensityReducedRays(vec3d, aabb);
		} else {
			return this.world.a(vec3d, aabb);
		}
	}

	private float calculateDensityReducedRays(Vec3D vec3d, AxisAlignedBB aabb) {
		int arrived = 0;
		int rays = 0;

		for (Vec3D vector : calculateVectors(aabb)) {

			if (rays == 8 && arrived == 8) {
				return 1.0F;
			}

			if (world.rayTrace(vector, vec3d) == null) {
				++arrived;
			}

			++rays;
		}

		return (float) arrived / (float) rays;
	}

	private List<Vec3D> calculateVectors(AxisAlignedBB aabb) {
		double d0 = 1.0D / ((aabb.d - aabb.a) * 2.0D + 1.0D);
		double d1 = 1.0D / ((aabb.e - aabb.b) * 2.0D + 1.0D);
		double d2 = 1.0D / ((aabb.f - aabb.c) * 2.0D + 1.0D);
		double d3 = (1.0D - (Math.floor(1.0D / d0)) * d0) / 2.0D;
		double d4 = (1.0D - (Math.floor(1.0D / d2)) * d2) / 2.0D;

		if (d0 < 0.0 || d1 < 0.0 || d2 < 0.0) {
			return Collections.emptyList();
		}

		List<Vec3D> vectors = new LinkedList<>();

		for (float f = 0.0F; f <= 1.0F; f = (float) (f + d0)) {
			for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) (f1 + d1)) {
				for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) (f2 + d2)) {
					double d5 = aabb.a + (aabb.d - aabb.a) * f;
					double d6 = aabb.b + (aabb.e - aabb.b) * f1;
					double d7 = aabb.c + (aabb.f - aabb.c) * f2;
					Vec3D vector = new Vec3D(d5 + d3, d6, d7 + d4);

					if ((f == 0 || f + d0 > 1.0F) && (f1 == 0 || f1 + d1 > 1.0F) && (f2 == 0 || f2 + d2 > 1.0F)) {
						vectors.add(0, vector);
					} else {
						vectors.add(vector);
					}
				}
			}
		}

		return vectors;
	}

	static int createKey(Explosion explosion, AxisAlignedBB aabb) {
		int result;
		long temp;
		result = explosion.world.hashCode();
		temp = Double.doubleToLongBits(explosion.posX);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(explosion.posY);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(explosion.posZ);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(aabb.a);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(aabb.b);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(aabb.c);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(aabb.d);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(aabb.e);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(aabb.f);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	
	
}
