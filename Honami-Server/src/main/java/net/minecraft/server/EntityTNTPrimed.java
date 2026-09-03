package net.minecraft.server;

import org.bukkit.event.entity.ExplosionPrimeEvent; 

import rein.honami.spigot.cache.Constants;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.tnt.TNTVelocityCache;

public class EntityTNTPrimed extends Entity {

	public int fuseTicks;
	private EntityLiving source;
	public float yield = 4; 
	public boolean isIncendiary = false; 
	public org.bukkit.Location sourceLoc; 

	public EntityTNTPrimed(World world) {
		this(null, world);
	}

	public EntityTNTPrimed(org.bukkit.Location loc, World world) {
		super(world);
		sourceLoc = loc;
		
		this.k = true;
		this.setSize(0.98F, 0.98F);
		this.loadChunks = world.paperSpigotConfig.loadUnloadedTNTEntities; 
	}

	public EntityTNTPrimed(org.bukkit.Location loc, World world, double d0, double d1, double d2,
			EntityLiving entityliving) {
		this(loc, world);
		this.setPosition(d0, d1, d2);
		float f = (float) (Math.random() * 3.1415927410125732D * 2.0D);

		this.motX = -((float) Math.sin(f)) * 0.02F;
		this.motY = 0.20000000298023224D;
		this.motZ = -((float) Math.cos(f)) * 0.02F;
		this.fuseTicks = 80;
		this.lastX = d0;
		this.lastY = d1;
		this.lastZ = d2;
		this.source = entityliving;
		if (world.paperSpigotConfig.fixCannons) {
			this.motX = this.motZ = 0.0F; 
		}
	}

	@Override
	protected void h() {
	}

	@Override
	protected boolean s_() {
		return false;
	}

	@Override
	public boolean ad() {
		return !this.dead;
	}

	@Override
	public void t_() {
		if (world.spigotConfig.maxTntTicksPerTick > -1
				&& world.spigotConfig.currentPrimedTnt++ > world.spigotConfig.maxTntTicksPerTick) {
			return;
		} 
		this.lastX = this.locX;
		this.lastY = this.locY;
		this.lastZ = this.locZ;

		if (HonamiConfig.tntVelocityCacheEnabled) {
			TNTVelocityCache cache = TNTVelocityCache.getInstance();
			double[] cached = cache.getCachedVelocity(
					(int) this.locX, (int) this.locY, (int) this.locZ,
					this.world.getWorld().getName());
			if (cached != null) {
				this.motX = cached[0];
				this.motY = cached[1];
				this.motZ = cached[2];
			}
		}

		this.motY -= 0.03999999910593033D;
		this.move(this.motX, this.motY, this.motZ);

		if (this.world.paperSpigotConfig.tntEntityHeightNerf != 0
				&& this.locY > this.world.paperSpigotConfig.tntEntityHeightNerf) {
			this.die();
		}

		
		if (this.inUnloadedChunk && world.paperSpigotConfig.removeUnloadedTNTEntities) {
			this.die();
			this.fuseTicks = 2;
		}

		this.motX *= 0.9800000190734863D;
		this.motY *= 0.9800000190734863D;
		this.motZ *= 0.9800000190734863D;
		if (this.onGround) {
			this.motX *= 0.699999988079071D;
			this.motZ *= 0.699999988079071D;
			this.motY *= -0.5D;
		}

		if (HonamiConfig.tntVelocityCacheEnabled) {
			TNTVelocityCache.getInstance().cacheVelocity(
					(int) this.locX, (int) this.locY, (int) this.locZ,
					this.world.getWorld().getName(),
					this.motX, this.motY, this.motZ);
		}

		if (this.fuseTicks-- <= 0) {

			
			if (!this.world.isClientSide) {
				this.explode();
			}
			this.die();
			
		} else {
			this.W();
			this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D,
					Constants.EMPTY_ARRAY);
		}

	}

	private void explode() {

		
		ChunkProviderServer chunkProviderServer = ((ChunkProviderServer) world.chunkProvider);
		boolean forceChunkLoad = chunkProviderServer.forceChunkLoad;
		if (world.paperSpigotConfig.loadUnloadedTNTEntities) {
			chunkProviderServer.forceChunkLoad = true;
		}

		org.bukkit.craftbukkit.CraftServer server = this.world.getServer();

		ExplosionPrimeEvent event = new ExplosionPrimeEvent(
				(org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, this));
		server.getPluginManager().callEvent(event);

		if (!event.isCancelled()) {
			this.world.createExplosion(this, this.locX, this.locY + this.length / 2.0F, this.locZ, event.getRadius(),
					event.getFire(), true);
		}

		
		if (world.paperSpigotConfig.loadUnloadedTNTEntities) {
			chunkProviderServer.forceChunkLoad = forceChunkLoad;
		}
		
	}

	@Override
	protected void b(NBTTagCompound nbttagcompound) {
		nbttagcompound.setByte("Fuse", (byte) this.fuseTicks);
		
		if (sourceLoc != null) {
			nbttagcompound.setInt("SourceLoc_x", sourceLoc.getBlockX());
			nbttagcompound.setInt("SourceLoc_y", sourceLoc.getBlockY());
			nbttagcompound.setInt("SourceLoc_z", sourceLoc.getBlockZ());
		}
		
	}

	@Override
	protected void a(NBTTagCompound nbttagcompound) {
		this.fuseTicks = nbttagcompound.getByte("Fuse");
		
		if (nbttagcompound.hasKey("SourceLoc_x")) {
			int srcX = nbttagcompound.getInt("SourceLoc_x");
			int srcY = nbttagcompound.getInt("SourceLoc_y");
			int srcZ = nbttagcompound.getInt("SourceLoc_z");
			sourceLoc = new org.bukkit.Location(world.getWorld(), srcX, srcY, srcZ);
		}
		
	}

	public EntityLiving getSource() {
		return this.source;
	}

	@Override
	public double f(double d0, double d1, double d2) {
		if (!world.paperSpigotConfig.fixCannons) {
			return super.f(d0, d1, d2);
		}

		double d3 = this.locX - d0;
		double d4 = this.locY + this.getHeadHeight() - d1;
		double d5 = this.locZ - d2;

		return MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
	}

	@Override
	public boolean aL() {
		return !world.paperSpigotConfig.fixCannons && super.aL();
	}

	@Override
	public float getHeadHeight() {
		return world.paperSpigotConfig.fixCannons ? this.length / 2 : 0.0F;
	}

	@Override
	public boolean W() {
		if (!world.paperSpigotConfig.fixCannons) {
			return super.W();
		}

		

		

		return this.inWater;
	}
	
}
