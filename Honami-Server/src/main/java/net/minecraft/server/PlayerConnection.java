package net.minecraft.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.util.NumberConversions;
import org.github.paperspigot.PaperSpigotConfig; 

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import rein.honami.spigot.Honami;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.async.AsyncKeepAlive;
import rein.honami.spigot.events.PlayerIllegalBehaviourEvent;

import co.aikar.timings.SpigotTimings; 
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.WorldSettings.EnumGamemode;

public class PlayerConnection implements PacketListenerPlayIn, IUpdatePlayerListBox {

	private static final Logger c = LogManager.getLogger();
	public final NetworkManager networkManager;
	private final MinecraftServer minecraftServer;
	public EntityPlayer player;
	private int e;
	private int f;
	private int g;
	private boolean h;
	private int i;
	private long j;
	private long k;
	
	private volatile int chatThrottle;
	private static final AtomicIntegerFieldUpdater<PlayerConnection> chatSpamField = AtomicIntegerFieldUpdater
			.newUpdater(PlayerConnection.class, "chatThrottle");
	
	private final java.util.concurrent.atomic.AtomicInteger tabSpamLimiter = new java.util.concurrent.atomic.AtomicInteger(); 
	private int m;
	private final IntHashMap<Short> n = new IntHashMap<>();
	private double o;
	private double p;
	private double q;
	private boolean checkMovement = true;
	private boolean processedDisconnect; 
	private int lastBookTick = 0;
	private int creativeSlotCount = 0;
	private long lastCustomPayloadPacketTS = -1L;
	private boolean isExploiter = false;

	private Queue<Packet<?>> queuedPackets = Queues.newLinkedBlockingQueue();

	public PlayerConnection(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
		this.minecraftServer = minecraftserver;
		this.networkManager = networkmanager;

		this.lastBookTick = MinecraftServer.currentTick;
		networkmanager.a(this);
		this.player = entityplayer;
		entityplayer.playerConnection = this;

		AsyncKeepAlive.playerJoin(this.getPlayer());

		this.server = minecraftserver.server;
	}

	private final org.bukkit.craftbukkit.CraftServer server;
	private int lastTick = MinecraftServer.currentTick;
	private int lastDropTick = MinecraftServer.currentTick;
	private int dropCount = MinecraftServer.currentTick;
	private static final int SURVIVAL_PLACE_DISTANCE_SQUARED = 6 * 6;
	private static final int CREATIVE_PLACE_DISTANCE_SQUARED = 7 * 7;

	private double lastPosX = Double.MAX_VALUE;
	private double lastPosY = Double.MAX_VALUE;
	private double lastPosZ = Double.MAX_VALUE;
	private float lastPitch = Float.MAX_VALUE;
	private float lastYaw = Float.MAX_VALUE;
	private boolean justTeleported = false;
	private boolean hasMoved; 

	public CraftPlayer getPlayer() {
		return (this.player == null) ? null : (CraftPlayer) this.player.getBukkitEntity();
	}

	private final static HashSet<Integer> invalidItems = new HashSet<Integer>(
			java.util.Arrays.asList(8, 9, 10, 11, 26, 34, 36, 43, 51, 52, 55, 59, 60, 62, 63, 64, 68, 71, 74, 75, 83,
					90, 92, 93, 94, 104, 105, 115, 117, 118, 119, 125, 127, 132, 140, 141, 142, 144)); 

	

	@Override
	public void c() {
		this.h = false;
		++this.e;

		if (AsyncKeepAlive.isEnabled()) {
			
		} else {
		
		if (HonamiConfig.modernKeepalive) {
			final long currentTime = this.getCurrentMillis();
			final long elapsedTime = currentTime - this.getLastPing();
			if (isPendingPing) {
				if (!this.processedDisconnect && elapsedTime >= KEEPALIVE_LIMIT) {
					this.disconnect("Timed out");
					return;
				}
			} else if (elapsedTime >= 15000L) {
				isPendingPing = true;
				this.setLastPing(currentTime);
				this.setKeepAliveID((int)  currentTime);
				this.sendPacket(new PacketPlayOutKeepAlive(this.getKeepAliveID()));
			}
			
		} else {
			this.minecraftServer.methodProfiler.a("keepAlive");
			if ((long) this.e - this.k > 40L) {
				this.k = (long) this.e;
				this.j = this.d();
				this.i = (int) this.j;
				this.sendPacket(new PacketPlayOutKeepAlive(this.i));
			}
			this.minecraftServer.methodProfiler.b();
		}
		} 

		for (int spam; (spam = this.chatThrottle) > 0 && !chatSpamField.compareAndSet(this, spam, spam - 1);) {
			;

			
		}
		if (tabSpamLimiter.get() > 0) tabSpamLimiter.getAndDecrement(); 

		if (this.m > 0) {
			--this.m;
		}

		if (this.player.D() > 0L && this.minecraftServer.getIdleTimeout() > 0 && MinecraftServer.az()
				- this.player.D() > ((long) this.minecraftServer.getIdleTimeout() * 1000 * 60)) {
			this.player.resetIdleTimer(); 
			this.disconnect("You have been idle for too long!");
		}

	}

	private boolean isDownloading;

	public boolean isDownloading() {
		return isDownloading;
	}

	public static final long KEEPALIVE_LIMIT = 30000;

	private boolean noKeepalives; 

	@Override
	public void a(PacketPlayInKeepAlive packetplayinkeepalive) {
		
		if (AsyncKeepAlive.isEnabled()) {
			AsyncKeepAlive.handleResponse(this.getPlayer());
			return;
		}

		if (HonamiConfig.modernKeepalive) {
			if (noKeepalives) {
				return;
			}
			if (isPendingPing && packetplayinkeepalive.a() == getKeepAliveID()) {
				int i = (int) (this.d() - getLastPing());
				this.player.ping = (this.player.ping * 3 + i) / 4;
				isPendingPing = false;
				isDownloading = false;
			} else if (packetplayinkeepalive.a() == 0) {
				isDownloading = true;
			} else {
				noKeepalives = true;
				c.warn("{} sent an invalid keepalive! pending keepalive: {} got id: {} expected id: {}",
						this.player.getName(), isPendingPing, packetplayinkeepalive.a(), this.getKeepAliveID());
				this.minecraftServer.postToMainThread(() -> disconnect("invalid keepalive"));
			}
		} else {
			
	        if (packetplayinkeepalive.a() == this.i) {
	            int i = (int) (this.d() - this.j);
	            this.player.ping = (this.player.ping * 3 + i) / 4;
	        }
		}
	}

	
	private boolean isPendingPing;

	private void setLastPing(final long lastPing) {
		this.j = lastPing;
	}

	private long getLastPing() {
		return this.j;
	}

	private void setKeepAliveID(final int keepAliveID) {
		this.i = keepAliveID;
	}

	private int getKeepAliveID() {
		return this.i;
	}

	private long getCurrentMillis() {
		return this.d();
	}

	public NetworkManager getNetworkManager() {
		return this.networkManager;
	}

	public NetworkManager a() {
		return this.networkManager;
	}

	public void disconnect(String s) {
		
		String leaveMessage = EnumChatFormat.YELLOW + this.player.getName() + " left the game.";

		PlayerKickEvent event = new PlayerKickEvent(this.server.getPlayer(this.player), s, leaveMessage);

		if (this.server.getServer().isRunning()) {
			this.server.getPluginManager().callEvent(event);
		}

		if (event.isCancelled()) {
			
			return;
		}
		
		s = event.getReason();
		
		final ChatComponentText chatcomponenttext = new ChatComponentText(s);

		this.networkManager.a(new PacketPlayOutKickDisconnect(chatcomponenttext), new GenericFutureListener() {
			@Override
			public void operationComplete(Future future) throws Exception { 
				PlayerConnection.this.networkManager.close(chatcomponenttext);
			}
		}, new GenericFutureListener[0]);
		this.a(chatcomponenttext); 
		this.networkManager.k();
		
		this.minecraftServer.postToMainThread(new Runnable() {
			@Override
			public void run() {
				PlayerConnection.this.networkManager.l();
			}
		});
	}

	@Override
	public void a(PacketPlayInSteerVehicle packetplayinsteervehicle) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsteervehicle, this, this.player.u());
		this.player.a(packetplayinsteervehicle.a(), packetplayinsteervehicle.b(), packetplayinsteervehicle.c(),
				packetplayinsteervehicle.d());
	}

	private boolean b(PacketPlayInFlying packetplayinflying) {
		return !Doubles.isFinite(packetplayinflying.a()) || !Doubles.isFinite(packetplayinflying.b())
				|| !Doubles.isFinite(packetplayinflying.c()) || !Floats.isFinite(packetplayinflying.e())
				|| !Floats.isFinite(packetplayinflying.d());
	}

	@Override
	public void a(PacketPlayInFlying packetplayinflying) {
		PlayerConnectionUtils.ensureMainThread(packetplayinflying, this, this.player.u());
		if (this.b(packetplayinflying)) {
			this.disconnect("Invalid move packet received");
		} else {
			creativeSlotCount = Math.max(creativeSlotCount--, 0);
			int windowClickCount = 0;
			WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);

			this.h = true;
			if (!this.player.viewingCredits) {
				double d0 = this.player.locX;
				double d1 = this.player.locY;
				double d2 = this.player.locZ;
				double d3 = 0.0D;
				double d4 = packetplayinflying.a() - this.o;
				double d5 = packetplayinflying.b() - this.p;
				double d6 = packetplayinflying.c() - this.q;

				if (packetplayinflying.g()) {
					d3 = d4 * d4 + d5 * d5 + d6 * d6;
					if (!this.checkMovement && d3 < 0.25D) {
						this.checkMovement = true;
					}
				}
				
				Player player = this.getPlayer();
				
				if (!hasMoved) {
					Location curPos = player.getLocation();
					lastPosX = curPos.getX();
					lastPosY = curPos.getY();
					lastPosZ = curPos.getZ();
					lastYaw = curPos.getYaw();
					lastPitch = curPos.getPitch();
					hasMoved = true;
				}
				
				Location from = new Location(player.getWorld(), lastPosX, lastPosY, lastPosZ, lastYaw, lastPitch); 

																													

				Location to = player.getLocation().clone(); 

				
				
				if (packetplayinflying.hasPos && packetplayinflying.y != -999.0D) {
					to.setX(packetplayinflying.x);
					to.setY(packetplayinflying.y);
					to.setZ(packetplayinflying.z);
				}

				
				if (packetplayinflying.hasLook) {
					to.setYaw(packetplayinflying.yaw);
					to.setPitch(packetplayinflying.pitch);
				}

				double delta = Math.pow(this.lastPosX - to.getX(), 2) + Math.pow(this.lastPosY - to.getY(), 2)
						+ Math.pow(this.lastPosZ - to.getZ(), 2);
				float deltaAngle = Math.abs(this.lastYaw - to.getYaw()) + Math.abs(this.lastPitch - to.getPitch());

				if ((packetplayinflying.hasPos) && ((delta > 0.0D) && (this.checkMovement && !this.player.dead))) {
					for (rein.honami.spigot.protocol.MovementListener movementListener : Honami.getInstance().getMovementListeners()) {
						try {
							movementListener.updateLocation(player, to, from, packetplayinflying);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				if ((packetplayinflying.hasLook)
						&& ((deltaAngle > 0.0F) && (this.checkMovement && !this.player.dead))) {
					for (rein.honami.spigot.protocol.MovementListener movementListener : Honami.getInstance().getMovementListeners()) {
						try {
							movementListener.updateRotation(player, to, from, packetplayinflying);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				if ((delta > 1f / 256 || deltaAngle > 10f) && (this.checkMovement && !this.player.dead)) {
					this.lastPosX = to.getX();
					this.lastPosY = to.getY();
					this.lastPosZ = to.getZ();
					this.lastYaw = to.getYaw();
					this.lastPitch = to.getPitch();

					if (HonamiConfig.firePlayerMoveEvent && PlayerMoveEvent.getHandlerList().getRegisteredListeners().length != 0) { 
						Location oldTo = to.clone();
						PlayerMoveEvent event = new PlayerMoveEvent(player, from, to);
						this.server.getPluginManager().callEvent(event);

						if (event.isCancelled()) {
							this.player.playerConnection.sendPacket(new PacketPlayOutPosition(from.getX(), from.getY(),
									from.getZ(), from.getYaw(), from.getPitch(),
									Collections.<PacketPlayOutPosition.EnumPlayerTeleportFlags>emptySet()));
							return;
						}

						if (!oldTo.equals(event.getTo()) && !event.isCancelled()) {
							this.player.getBukkitEntity().teleport(event.getTo(),
									PlayerTeleportEvent.TeleportCause.UNKNOWN);
							return;
						}

						if (!from.equals(this.getPlayer().getLocation()) && this.justTeleported) {
							this.justTeleported = false;
							return;
						}
					}
				}

				if (this.checkMovement && !this.player.dead) {
					
					this.f = this.e;
					double d7;
					double d8;
					double d9;

					if (this.player.vehicle != null) {
						float f = this.player.yaw;
						float f1 = this.player.pitch;

						this.player.vehicle.al();
						d7 = this.player.locX;
						d8 = this.player.locY;
						d9 = this.player.locZ;
						if (packetplayinflying.h()) {
							f = packetplayinflying.d();
							f1 = packetplayinflying.e();
						}

						this.player.onGround = packetplayinflying.f();
						this.player.l();
						this.player.setLocation(d7, d8, d9, f, f1);
						if (this.player.vehicle != null) {
							this.player.vehicle.al();
						}

						this.minecraftServer.getPlayerList().d(this.player);
						if (this.player.vehicle != null) {
							this.player.vehicle.ai = true; 
							if (d3 > 4.0D) {
								Entity entity = this.player.vehicle;

								this.player.playerConnection.sendPacket(new PacketPlayOutEntityTeleport(entity));
								this.a(this.player.locX, this.player.locY, this.player.locZ, this.player.yaw,
										this.player.pitch);
							}

						}

						if (this.checkMovement) {
							this.o = this.player.locX;
							this.p = this.player.locY;
							this.q = this.player.locZ;
						}

						worldserver.g(this.player);
						return;
					}

					if (this.player.isSleeping()) {
						this.player.l();
						this.player.setLocation(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
						worldserver.g(this.player);
						return;
					}

					double d10 = this.player.locY;

					this.o = this.player.locX;
					this.p = this.player.locY;
					this.q = this.player.locZ;
					d7 = this.player.locX;
					d8 = this.player.locY;
					d9 = this.player.locZ;
					float f2 = this.player.yaw;
					float f3 = this.player.pitch;

					if (packetplayinflying.g() && packetplayinflying.b() == -999.0D) {
						packetplayinflying.a(false);
					}

					if (packetplayinflying.g()) {
						d7 = packetplayinflying.a();
						d8 = packetplayinflying.b();
						d9 = packetplayinflying.c();
						if (Math.abs(packetplayinflying.a()) > 3.0E7D || Math.abs(packetplayinflying.c()) > 3.0E7D) {
							this.disconnect("Illegal position");
							return;
						}
					}

					if (packetplayinflying.h()) {
						f2 = packetplayinflying.d();
						f3 = packetplayinflying.e();
					}

					this.player.l();
					this.player.setLocation(this.o, this.p, this.q, f2, f3);
					if (!this.checkMovement) {
						return;
					}

					double d11 = d7 - this.player.locX;
					double d12 = d8 - this.player.locY;
					double d13 = d9 - this.player.locZ;
					double d14 = this.player.motX * this.player.motX + this.player.motY * this.player.motY
							+ this.player.motZ * this.player.motZ;
					double d15 = d11 * d11 + d12 * d12 + d13 * d13;

					if (d15 - d14 > org.spigotmc.SpigotConfig.movedTooQuicklyThreshold
							&& (!this.minecraftServer.T() || !this.minecraftServer.S().equals(this.player.getName()))) { 

																															

																															

																															

																															
						PlayerConnection.c.warn(this.player.getName() + " moved too quickly! " + d11 + "," + d12 + ","
								+ d13 + " (" + d11 + ", " + d12 + ", " + d13 + ")");
						this.a(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
						return;
					}

					float f4 = 0.0625F;
					boolean flag = worldserver.getCubes(this.player, this.player.getBoundingBox().shrink(f4, f4, f4))
							.isEmpty();

					if (this.player.onGround && !packetplayinflying.f() && d12 > 0.0D) {
						this.player.bF();
					}

					this.player.move(d11, d12, d13);
					this.player.onGround = packetplayinflying.f();
					double d16 = d12;

					d11 = d7 - this.player.locX;
					d12 = d8 - this.player.locY;
					if (d12 > -0.5D || d12 < 0.5D) {
						d12 = 0.0D;
					}

					d13 = d9 - this.player.locZ;
					d15 = d11 * d11 + d12 * d12 + d13 * d13;
					boolean flag1 = false;

					if (d15 > org.spigotmc.SpigotConfig.movedWronglyThreshold && !this.player.isSleeping()
							&& !this.player.playerInteractManager.isCreative()) {
						flag1 = true;
						PlayerConnection.c.warn(this.player.getName() + " moved wrongly!");
					}

					this.player.setLocation(d7, d8, d9, f2, f3);
					this.player.checkMovement(this.player.locX - d0, this.player.locY - d1, this.player.locZ - d2);
					if (!this.player.noclip) {
						boolean flag2 = worldserver
								.getCubes(this.player, this.player.getBoundingBox().shrink(f4, f4, f4)).isEmpty();

						if (flag && (flag1 || !flag2) && !this.player.isSleeping()) {
							this.a(this.o, this.p, this.q, f2, f3);
							return;
						}
					}

					AxisAlignedBB axisalignedbb = this.player.getBoundingBox().grow(f4, f4, f4).a(0.0D, -0.55D, 0.0D);

					if (!this.minecraftServer.getAllowFlight() && !this.player.abilities.canFly
							&& !worldserver.c(axisalignedbb)) {
						if (d16 >= -0.03125D) {
							++this.g;
							if (this.g > 80) {
								PlayerConnection.c.warn(this.player.getName() + " was kicked for floating too long!");
								this.disconnect("Flying is not enabled on this server");
								return;
							}
						}
					} else {
						this.g = 0;
					}

					this.player.onGround = packetplayinflying.f();
					this.minecraftServer.getPlayerList().d(this.player);
					this.player.a(this.player.locY - d10, packetplayinflying.f());
				} else if (this.e - this.f > 20) {
					this.a(this.o, this.p, this.q, this.player.yaw, this.player.pitch);
				}
			}

		}
	}

	public void a(double d0, double d1, double d2, float f, float f1) {
		this.a(d0, d1, d2, f, f1, Collections.emptySet()); 
	}

	public void a(double d0, double d1, double d2, float f, float f1,
			Set<PacketPlayOutPosition.EnumPlayerTeleportFlags> set) {
		
		Player player = this.getPlayer();
		Location from = player.getLocation();

		double x = d0;
		double y = d1;
		double z = d2;
		float yaw = f;
		float pitch = f1;
		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X)) {
			x += from.getX();
		}
		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y)) {
			y += from.getY();
		}
		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z)) {
			z += from.getZ();
		}
		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT)) {
			yaw += from.getYaw();
		}
		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT)) {
			pitch += from.getPitch();
		}

		Location to = new Location(this.getPlayer().getWorld(), x, y, z, yaw, pitch);
		
		PlayerTeleportEvent event = new PlayerTeleportEvent(player, from.clone(), to.clone(),
				PlayerTeleportEvent.TeleportCause.UNKNOWN);
		this.server.getPluginManager().callEvent(event);

		if (event.isCancelled() || to.equals(event.getTo())) {
			set.clear(); 
			to = event.isCancelled() ? event.getFrom() : event.getTo();
			d0 = to.getX();
			d1 = to.getY();
			d2 = to.getZ();
			f = to.getYaw();
			f1 = to.getPitch();
		}
		
		this.internalTeleport(d0, d1, d2, f, f1, set);
	}

	public void teleport(Location dest) {
		internalTeleport(dest.getX(), dest.getY(), dest.getZ(), dest.getYaw(), dest.getPitch(), Collections.emptySet());
	}

	private void internalTeleport(double d0, double d1, double d2, float f, float f1, Set set) {
		if (Float.isNaN(f)) {
			f = 0;
		}

		if (Float.isNaN(f1)) {
			f1 = 0;
		}
		this.justTeleported = true;
		
		this.checkMovement = false;
		this.o = d0;
		this.p = d1;
		this.q = d2;
		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X)) {
			this.o += this.player.locX;
		}

		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y)) {
			this.p += this.player.locY;
		}

		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Z)) {
			this.q += this.player.locZ;
		}

		float f2 = f;
		float f3 = f1;

		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.Y_ROT)) {
			f2 = f + this.player.yaw;
		}

		if (set.contains(PacketPlayOutPosition.EnumPlayerTeleportFlags.X_ROT)) {
			f3 = f1 + this.player.pitch;
		}

		this.lastPosX = this.o;
		this.lastPosY = this.p;
		this.lastPosZ = this.q;
		this.lastYaw = f2;
		this.lastPitch = f3;

		this.player.setLocation(this.o, this.p, this.q, f2, f3);
		this.player.playerConnection.sendPacket(new PacketPlayOutPosition(d0, d1, d2, f, f1, set));
	}

	@Override
	public void a(PacketPlayInBlockDig packetplayinblockdig) {
		PlayerConnectionUtils.ensureMainThread(packetplayinblockdig, this, this.player.u());
		if (this.player.dead) {
			return; 
		}
		WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
		BlockPosition blockposition = packetplayinblockdig.a();

		this.player.resetIdleTimer();
		
		switch (PlayerConnection.SyntheticClass_1.a[packetplayinblockdig.c().ordinal()]) {
		case 1: 
			if (!this.player.isSpectator()) {

				
				if (this.lastDropTick != MinecraftServer.currentTick) {
					this.dropCount = 0;
					this.lastDropTick = MinecraftServer.currentTick;
				} else {
					
					this.dropCount++;
					if (this.dropCount >= 20) {
						PlayerConnection.c.warn(this.player.getName() + " dropped their items too quickly!");
						this.disconnect("You dropped your items too quickly (Hacking?)");
						return;
					}
				}
				
				this.player.a(false);
			}

			return;

		case 2: 
			if (!this.player.isSpectator()) {
				this.player.a(true);
			}

			return;

		case 3: 
			this.player.bU();
			return;

		case 4: 
		case 5: 
		case 6: 
			double d0 = this.player.locX - (blockposition.getX() + 0.5D);
			double d1 = this.player.locY - (blockposition.getY() + 0.5D) + 1.5D;
			double d2 = this.player.locZ - (blockposition.getZ() + 0.5D);
			double d3 = d0 * d0 + d1 * d1 + d2 * d2;

			if (d3 > 36.0D) {
				return;
			} else if (blockposition.getY() >= this.minecraftServer.getMaxBuildHeight()) {
				return;
			} else {
				if (packetplayinblockdig.c() == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK) {
					if (!this.minecraftServer.a(worldserver, blockposition, this.player)
							&& worldserver.getWorldBorder().a(blockposition)) {
						this.player.playerInteractManager.a(blockposition, packetplayinblockdig.b());
					} else {
						
						CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_BLOCK, blockposition,
								packetplayinblockdig.b(), this.player.inventory.getItemInHand());
						this.player.playerConnection
								.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
						
						TileEntity tileentity = worldserver.getTileEntity(blockposition);
						if (tileentity != null) {
							this.player.playerConnection.sendPacket(tileentity.getUpdatePacket());
						}
						
					}
				} else {
					if (packetplayinblockdig.c() == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
						this.player.playerInteractManager.a(blockposition);
					} else if (packetplayinblockdig.c() == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
						this.player.playerInteractManager.e();
					}

					if (worldserver.getType(blockposition).getBlock().getMaterial() != Material.AIR) {
						this.player.playerConnection
								.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
					}
				}

				return;
			}

		default:
			throw new IllegalArgumentException("Invalid player action");
		}
		
	}

	private long lastPlace = -1;
	private int packets = 0;

	@Override
	public void a(PacketPlayInBlockPlace packetplayinblockplace) {
		PlayerConnectionUtils.ensureMainThread(packetplayinblockplace, this, this.player.u());
		WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
		boolean throttled = false;
		
		if (org.github.paperspigot.PaperSpigotConfig.interactLimitEnabled && lastPlace != -1
				&& packetplayinblockplace.timestamp - lastPlace < 30 && packets++ >= 4) {
			throttled = true;
		} else if (packetplayinblockplace.timestamp - lastPlace >= 30 || lastPlace == -1) {
			lastPlace = packetplayinblockplace.timestamp;
			packets = 0;
		}

		
		if (this.player.dead) {
			return;
		}

		

		boolean always = false;

		ItemStack itemstack = this.player.inventory.getItemInHand();
		boolean flag = false;
		BlockPosition blockposition = packetplayinblockplace.a();
		EnumDirection enumdirection = EnumDirection.fromType1(packetplayinblockplace.getFace());

		this.player.resetIdleTimer();
		if (packetplayinblockplace.getFace() == 255) {
			if (itemstack == null) {
				return;
			}

			int itemstackAmount = itemstack.count;
			
			if (!throttled) {
				
				float f1 = this.player.pitch;
				float f2 = this.player.yaw;
				double d0 = this.player.locX;
				double d1 = this.player.locY + this.player.getHeadHeight();
				double d2 = this.player.locZ;
				Vec3D vec3d = new Vec3D(d0, d1, d2);

				float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
				float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
				float f5 = -MathHelper.cos(-f1 * 0.017453292F);
				float f6 = MathHelper.sin(-f1 * 0.017453292F);
				float f7 = f4 * f5;
				float f8 = f3 * f5;
				double d3 = player.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.CREATIVE ? 5.0D
						: 4.5D;
				Vec3D vec3d1 = vec3d.add(f7 * d3, f6 * d3, f8 * d3);
				MovingObjectPosition movingobjectposition = this.player.world.rayTrace(vec3d, vec3d1, false);

				boolean cancelled = false;
				if (movingobjectposition == null
						|| movingobjectposition.type != MovingObjectPosition.EnumMovingObjectType.BLOCK) {
					org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory
							.callPlayerInteractEvent(this.player, Action.RIGHT_CLICK_AIR, itemstack);
					cancelled = event.useItemInHand() == Event.Result.DENY;
				} else {
					if (player.playerInteractManager.firedInteract) {
						player.playerInteractManager.firedInteract = false;
						cancelled = player.playerInteractManager.interactResult;
					} else {
						org.bukkit.event.player.PlayerInteractEvent event = CraftEventFactory.callPlayerInteractEvent(
								player, Action.RIGHT_CLICK_BLOCK, movingobjectposition.a(),
								movingobjectposition.direction, itemstack, true);
						cancelled = event.useItemInHand() == Event.Result.DENY;
					}
				}

				if (!cancelled) {
					this.player.playerInteractManager.useItem(this.player, this.player.world, itemstack);
				} else {
					this.player.getBukkitEntity().updateInventory(); 
					
					if (itemstack.getItem() == Item.getItemOf(Blocks.WATERLILY)) {
						MovingObjectPosition movingObjectPosition1 = this.player.world.rayTrace(vec3d, vec3d1, true,
								false, false);
						if (movingObjectPosition1 != null) {
							BlockPosition blockPosition = movingObjectPosition1.a().up();
							org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(worldserver,
									blockPosition.getX(), blockPosition.getY(), blockPosition.getZ())
									.update(true, false);
						}

					} else if (itemstack.getItem() == Items.BUCKET) {
						MovingObjectPosition movingObjectPosition1 = this.player.world.rayTrace(vec3d, vec3d1, true,
								false, false);
						if (movingObjectPosition1 != null) {
							BlockPosition blockPosition = movingObjectPosition1.a();
							org.bukkit.craftbukkit.block.CraftBlockState.getBlockState(worldserver,
									blockPosition.getX(), blockPosition.getY(), blockPosition.getZ())
									.update(true, false);
						}
					}
				}

			} else if (MinecraftServer.currentTick - lastDropTick > 1 && HonamiConfig.fixEatWhileRunning) {
				this.player.playerInteractManager.useItem(this.player, this.player.world, itemstack);
			}

			

			always = (itemstack.count != itemstackAmount) || itemstack.getItem() == Item.getItemOf(Blocks.WATERLILY);
			
		} else if (blockposition.getY() >= this.minecraftServer.getMaxBuildHeight() - 1
				&& (enumdirection == EnumDirection.UP
						|| blockposition.getY() >= this.minecraftServer.getMaxBuildHeight())) {
			ChatMessage chatmessage = new ChatMessage("build.tooHigh",
					new Object[] { Integer.valueOf(this.minecraftServer.getMaxBuildHeight()) });

			chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
			this.player.playerConnection.sendPacket(new PacketPlayOutChat(chatmessage));
			flag = true;
		} else {

			Location eyeLoc = this.getPlayer().getEyeLocation();
			double reachDistance = NumberConversions.square(eyeLoc.getX() - blockposition.getX())
					+ NumberConversions.square(eyeLoc.getY() - blockposition.getY())
					+ NumberConversions.square(eyeLoc.getZ() - blockposition.getZ());
			if (reachDistance > (this.getPlayer().getGameMode() == org.bukkit.GameMode.CREATIVE
					? CREATIVE_PLACE_DISTANCE_SQUARED
					: SURVIVAL_PLACE_DISTANCE_SQUARED)) {
                
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition)); 
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition.shift(enumdirection))); 
                if (this.player.activeContainer != null) {
                    Slot slot = this.player.activeContainer.getSlot(this.player.inventory, this.player.inventory.itemInHandIndex);
                    if (slot != null) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, slot.rawSlotIndex, this.player.inventory.getItemInHand())); 
                    }
                }
                
				return;
			}

			if (!worldserver.getWorldBorder().a(blockposition)) {
                
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition)); 
                this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition.shift(enumdirection))); 
                if (this.player.activeContainer != null) {
                    Slot slot = this.player.activeContainer.getSlot(this.player.inventory, this.player.inventory.itemInHandIndex);
                    if (slot != null) {
                        this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, slot.rawSlotIndex, this.player.inventory.getItemInHand())); 
                    }
                }
                
				return;
			}

			if (this.checkMovement
					&& this.player.e(blockposition.getX() + 0.5D, blockposition.getY() + 0.5D,
							blockposition.getZ() + 0.5D) < 64.0D
					&& !this.minecraftServer.a(worldserver, blockposition, this.player)
					&& worldserver.getWorldBorder().a(blockposition)) {
				always = throttled || !this.player.playerInteractManager.interact(this.player, worldserver, itemstack,
						blockposition, enumdirection, packetplayinblockplace.d(), packetplayinblockplace.e(),
						packetplayinblockplace.f());
			}

			flag = true;
		}

		if (flag) {
			this.player.playerConnection.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition));
			this.player.playerConnection
					.sendPacket(new PacketPlayOutBlockChange(worldserver, blockposition.shift(enumdirection)));
		}

		itemstack = this.player.inventory.getItemInHand();
		if (itemstack != null && itemstack.count == 0) {
			this.player.inventory.items[this.player.inventory.itemInHandIndex] = null;
			itemstack = null;
		}

		if (itemstack == null || itemstack.l() == 0) {
			this.player.g = true;
			this.player.inventory.items[this.player.inventory.itemInHandIndex] = ItemStack
					.b(this.player.inventory.items[this.player.inventory.itemInHandIndex]);
			Slot slot = this.player.activeContainer.getSlot(this.player.inventory,
					this.player.inventory.itemInHandIndex);

			this.player.activeContainer.b();
			this.player.g = false;

			if (!ItemStack.matches(this.player.inventory.getItemInHand(), packetplayinblockplace.getItemStack())
					|| always) {
				this.sendPacket(new PacketPlayOutSetSlot(this.player.activeContainer.windowId, slot.rawSlotIndex,
						this.player.inventory.getItemInHand()));
			}
		}

	}

	@Override
	public void a(PacketPlayInSpectate packetplayinspectate) {
		PlayerConnectionUtils.ensureMainThread(packetplayinspectate, this, this.player.u());
		if (this.player.isSpectator()) {
			Entity entity = null;
			WorldServer[] aworldserver = this.minecraftServer.worldServer;
			int i = aworldserver.length;

			for (WorldServer worldserver : minecraftServer.worlds) {

				if (worldserver != null) {
					entity = packetplayinspectate.a(worldserver);
					if (entity != null) {
						break;
					}
				}
			}

			if (entity != null) {
				this.player.setSpectatorTarget(this.player);
				this.player.mount((Entity) null);

				this.player.getBukkitEntity().teleport(entity.getBukkitEntity(),
						PlayerTeleportEvent.TeleportCause.SPECTATE);
				
			}
		}

	}

	@Override
	public void a(PacketPlayInResourcePackStatus packetplayinresourcepackstatus) {
		
		PlayerConnectionUtils.ensureMainThread(packetplayinresourcepackstatus, this, this.player.u());
		PlayerResourcePackStatusEvent.Status status = PlayerResourcePackStatusEvent.Status
				.values()[packetplayinresourcepackstatus.b.ordinal()];
		this.getPlayer().setResourcePackStatus(status, packetplayinresourcepackstatus.a);
		this.server.getPluginManager()
				.callEvent(new PlayerResourcePackStatusEvent(getPlayer(), status, packetplayinresourcepackstatus.a));
		
	}

	@Override
	public void a(IChatBaseComponent ichatbasecomponent) {
		
		if (this.processedDisconnect) {
			return;
		} else {
			this.processedDisconnect = true;
		}

		AsyncKeepAlive.playerQuit(this.getPlayer());
		PlayerConnection.c.info(this.player.getName() + " lost connection: " + ichatbasecomponent.c()); 

																										

		

		this.player.q();
		String quitMessage = this.minecraftServer.getPlayerList().disconnect(this.player);
		if ((quitMessage != null) && (quitMessage.length() > 0)) {
			this.minecraftServer.getPlayerList().sendMessage(CraftChatMessage.fromString(quitMessage));
		}
		
		if (this.minecraftServer.T() && this.player.getName().equals(this.minecraftServer.S())) {
			PlayerConnection.c.info("Stopping singleplayer server as player logged out");
			this.minecraftServer.safeShutdown();
		}

	}

	public void sendPacket(final Packet packet) {
		if (packet instanceof PacketPlayOutChat) {
			PacketPlayOutChat packetplayoutchat = (PacketPlayOutChat) packet;
			EntityHuman.EnumChatVisibility flags = this.player.getChatFlags();

			if (flags == EntityHuman.EnumChatVisibility.HIDDEN) {
				return;
			}

			if (flags == EntityHuman.EnumChatVisibility.SYSTEM && !packetplayoutchat.b()) {
				return;
			}
		}

		if (packet == null || this.processedDisconnect) { 
			return;
		} else if (packet instanceof PacketPlayOutSpawnPosition) {
			PacketPlayOutSpawnPosition packet6 = (PacketPlayOutSpawnPosition) packet;
			this.player.compassTarget = new Location(this.getPlayer().getWorld(), packet6.position.getX(),
					packet6.position.getY(), packet6.position.getZ());
		}

		try {
			for (rein.honami.spigot.protocol.PacketListener packetListener : Honami.getInstance().getPacketListeners()) {
				try {
					if (!packetListener.onSentPacket(this, packet)) {
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			this.networkManager.handle(packet);
		} catch (Throwable throwable) {
			CrashReport crashreport = CrashReport.a(throwable, "Sending packet");
			CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Packet being sent");

			crashreportsystemdetails.a("Packet class", new Callable() {
				public String a() throws Exception {
					return packet.getClass().getCanonicalName();
				}

				@Override
				public Object call() throws Exception {
					return this.a();
				}
			});
			throw new ReportedException(crashreport);
		}
	}

	@Override
	public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {
		
		if (this.player.dead) {
			return;
		}
		PlayerConnectionUtils.ensureMainThread(packetplayinhelditemslot, this, this.player.u());
		if (packetplayinhelditemslot.a() >= 0 && packetplayinhelditemslot.a() < PlayerInventory.getHotbarSize()) {
			PlayerItemHeldEvent event = new PlayerItemHeldEvent(this.getPlayer(), this.player.inventory.itemInHandIndex,
					packetplayinhelditemslot.a());
			this.server.getPluginManager().callEvent(event);
			if (event.isCancelled()) {
				this.sendPacket(new PacketPlayOutHeldItemSlot(this.player.inventory.itemInHandIndex));
				this.player.resetIdleTimer();
				return;
			}
			
			this.player.inventory.itemInHandIndex = packetplayinhelditemslot.a();
			this.player.resetIdleTimer();
		} else {
			PlayerConnection.c.warn(this.player.getName() + " tried to set an invalid carried item");
			this.disconnect("Invalid hotbar selection (Hacking?)"); 
		}
	}

	@Override
	public void a(PacketPlayInChat packetplayinchat) {
		
		boolean isSync = packetplayinchat.a().startsWith("/");
		if (isSync)
		 {
			PlayerConnectionUtils.ensureMainThread(packetplayinchat, this, this.player.u());
		}
		
		if (this.player.dead || this.player.getChatFlags() == EntityHuman.EnumChatVisibility.HIDDEN) { 

																										
																										
			ChatMessage chatmessage = new ChatMessage("chat.cannotSend");

			chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
			this.sendPacket(new PacketPlayOutChat(chatmessage));
		} else {
			this.player.resetIdleTimer();
			String s = packetplayinchat.a();

			s = StringUtils.normalizeSpace(s);

			for (int i = 0; i < s.length(); ++i) {
				if (!SharedConstants.isAllowedChatCharacter(s.charAt(i))) {
					
					if (!isSync) {
						Waitable waitable = new Waitable() {
							@Override
							protected Object evaluate() {
								PlayerConnection.this.disconnect("Illegal characters in chat");
								return null;
							}
						};

						this.minecraftServer.processQueue.add(waitable);

						try {
							waitable.get();
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						} catch (ExecutionException e) {
							throw new RuntimeException(e);
						}
					} else {
						this.disconnect("Illegal characters in chat");
					}
					
					return;
				}
			}

			if (isSync) {
				try {
					this.minecraftServer.server.playerCommandState = true;
					this.handleCommand(s);
				} finally {
					this.minecraftServer.server.playerCommandState = false;
				}
			} else if (s.isEmpty()) {
				c.warn(this.player.getName() + " tried to send an empty message");
			} else if (getPlayer().isConversing()) {
				
				final String message = s;
				this.minecraftServer.processQueue.add(new Waitable() {
					@Override
					protected Object evaluate() {
						getPlayer().acceptConversationInput(message);
						return null;
					}
				});
				
			} else if (this.player.getChatFlags() == EntityHuman.EnumChatVisibility.SYSTEM) { 
																								
				ChatMessage chatmessage = new ChatMessage("chat.cannotSend", new Object[0]);

				chatmessage.getChatModifier().setColor(EnumChatFormat.RED);
				this.sendPacket(new PacketPlayOutChat(chatmessage));
			} else if (true) {
				this.chat(s, true);
				
			} else {
				ChatMessage chatmessage1 = new ChatMessage("chat.type.text",
						new Object[] { this.player.getScoreboardDisplayName(), s });

				this.minecraftServer.getPlayerList().sendMessage(chatmessage1, false);
			}

			boolean counted = true;
			for (String exclude : org.spigotmc.SpigotConfig.spamExclusions) {
				if (exclude != null && s.startsWith(exclude)) {
					counted = false;
					break;
				}
			}

			
			if (counted && chatSpamField.addAndGet(this, 20) > 200
					&& !this.minecraftServer.getPlayerList().isOp(this.player.getProfile())) { 
				if (!isSync) {
					Waitable waitable = new Waitable() {
						@Override
						protected Object evaluate() {
							PlayerConnection.this.disconnect("disconnect.spam");
							return null;
						}
					};

					this.minecraftServer.processQueue.add(waitable);

					try {
						waitable.get();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					} catch (ExecutionException e) {
						throw new RuntimeException(e);
					}
				} else {
					this.disconnect("disconnect.spam");
				}
				
			}

		}
	}

	public void chat(String s, boolean async) {
		if (s.isEmpty() || this.player.getChatFlags() == EntityHuman.EnumChatVisibility.HIDDEN) {
			return;
		}

		if (!async && s.startsWith("/")) {
			
			if (!org.bukkit.Bukkit.isPrimaryThread()) {
				final String fCommandLine = s;
				MinecraftServer.LOGGER.log(org.apache.logging.log4j.Level.ERROR,
						"Command Dispatched Async: " + fCommandLine);
				MinecraftServer.LOGGER.log(org.apache.logging.log4j.Level.ERROR,
						"Please notify author of plugin causing this execution to fix this bug! see: http://bit.ly/1oSiM6C",
						new Throwable());
				Waitable wait = new Waitable() {
					@Override
					protected Object evaluate() {
						chat(fCommandLine, false);
						return null;
					}
				};
				minecraftServer.processQueue.add(wait);
				try {
					wait.get();
					return;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); 
														
				} catch (Exception e) {
					throw new RuntimeException("Exception processing chat command", e.getCause());
				}
			}
			
			this.handleCommand(s);
		} else if (this.player.getChatFlags() == EntityHuman.EnumChatVisibility.SYSTEM) {
			
		} else {
			Player player = this.getPlayer();
			AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(async, player, s, new LazyPlayerSet());
			this.server.getPluginManager().callEvent(event);

			if (PlayerChatEvent.getHandlerList().getRegisteredListeners().length != 0) {
				
				final PlayerChatEvent queueEvent = new PlayerChatEvent(player, event.getMessage(), event.getFormat(),
						event.getRecipients());
				queueEvent.setCancelled(event.isCancelled());
				Waitable waitable = new Waitable() {
					@Override
					protected Object evaluate() {
						org.bukkit.Bukkit.getPluginManager().callEvent(queueEvent);

						if (queueEvent.isCancelled()) {
							return null;
						}

						String message = String.format(queueEvent.getFormat(), queueEvent.getPlayer().getDisplayName(),
								queueEvent.getMessage());
						PlayerConnection.this.minecraftServer.console.sendMessage(message);
						if (((LazyPlayerSet) queueEvent.getRecipients()).isLazy()) {
							for (EntityPlayer player : PlayerConnection.this.minecraftServer.getPlayerList().players) {
								player.sendMessage(CraftChatMessage.fromString(message));
							}
						} else {
							for (Player player : queueEvent.getRecipients()) {
								player.sendMessage(message);
							}
						}
						return null;
					}
				};
				if (async) {
					minecraftServer.processQueue.add(waitable);
				} else {
					waitable.run();
				}
				try {
					waitable.get();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt(); 
														
				} catch (ExecutionException e) {
					throw new RuntimeException("Exception processing chat event", e.getCause());
				}
			} else {
				if (event.isCancelled()) {
					return;
				}

				s = String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage());
				minecraftServer.console.sendMessage(s);
				if (((LazyPlayerSet) event.getRecipients()).isLazy()) {
					for (EntityPlayer recipient : minecraftServer.getPlayerList().players) {
						recipient.sendMessage(CraftChatMessage.fromString(s));
					}
				} else {
					for (Player recipient : event.getRecipients()) {
						recipient.sendMessage(s);
					}
				}
			}
		}
	}

	private void handleCommand(String s) {
		SpigotTimings.playerCommandTimer.startTiming(); 
		
		if (org.spigotmc.SpigotConfig.logCommands) {
			PlayerConnection.c.info(this.player.getName() + " issued server command: " + s);
		}

		CraftPlayer player = this.getPlayer();

		PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(player, s, new LazyPlayerSet());
		this.server.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			SpigotTimings.playerCommandTimer.stopTiming(); 
			return;
		}

		try {
			if (this.server.dispatchCommand(event.getPlayer(), event.getMessage().substring(1))) {
				SpigotTimings.playerCommandTimer.stopTiming(); 
				return;
			}
		} catch (org.bukkit.command.CommandException ex) {
			player.sendMessage(
					org.bukkit.ChatColor.RED + "An internal error occurred while attempting to perform this command");
			java.util.logging.Logger.getLogger(PlayerConnection.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
			SpigotTimings.playerCommandTimer.stopTiming(); 
			return;
		}
		SpigotTimings.playerCommandTimer.stopTiming(); 

	}

	@Override
	public void a(PacketPlayInArmAnimation packetplayinarmanimation) {
		if (this.player.dead) {
			return; 
		}
		PlayerConnectionUtils.ensureMainThread(packetplayinarmanimation, this, this.player.u());
		this.player.resetIdleTimer();
		
		float f1 = this.player.pitch;
		float f2 = this.player.yaw;
		double d0 = this.player.locX;
		double d1 = this.player.locY + this.player.getHeadHeight();
		double d2 = this.player.locZ;
		Vec3D vec3d = new Vec3D(d0, d1, d2);

		float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = player.playerInteractManager.getGameMode() == WorldSettings.EnumGamemode.CREATIVE ? 5.0D : 4.5D;
		Vec3D vec3d1 = vec3d.add(f7 * d3, f6 * d3, f8 * d3);
		MovingObjectPosition movingobjectposition = this.player.world.rayTrace(vec3d, vec3d1, false);

		if (movingobjectposition == null
				|| movingobjectposition.type != MovingObjectPosition.EnumMovingObjectType.BLOCK) {
			CraftEventFactory.callPlayerInteractEvent(this.player, Action.LEFT_CLICK_AIR,
					this.player.inventory.getItemInHand());
		}

		PlayerAnimationEvent event = new PlayerAnimationEvent(this.getPlayer());
		this.server.getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}
		
		this.player.bw();
	}

	@Override
	public void a(PacketPlayInEntityAction packetplayinentityaction) {
		PlayerConnectionUtils.ensureMainThread(packetplayinentityaction, this, this.player.u());
		
		if (this.player.dead) {
			return;
		}
		switch (packetplayinentityaction.b()) {
		case START_SNEAKING:
		case STOP_SNEAKING:
			PlayerToggleSneakEvent event = new PlayerToggleSneakEvent(this.getPlayer(),
					packetplayinentityaction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING);
			this.server.getPluginManager().callEvent(event);

			if (event.isCancelled()) {
				return;
			}
			break;
		case START_SPRINTING:
		case STOP_SPRINTING:
			PlayerToggleSprintEvent e2 = new PlayerToggleSprintEvent(this.getPlayer(),
					packetplayinentityaction.b() == PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING);
			this.server.getPluginManager().callEvent(e2);

			if (e2.isCancelled()) {
				return;
			}
			break;
		}
		
		this.player.resetIdleTimer();
		switch (PlayerConnection.SyntheticClass_1.b[packetplayinentityaction.b().ordinal()]) {
		case 1:
			this.player.setSneaking(true);
			break;

		case 2:
			this.player.setSneaking(false);
			break;

		case 3:
			this.player.setSprinting(true);
			this.player.setExtraKnockback(true);
			break;

		case 4:
			this.player.setSprinting(false);
			this.player.setExtraKnockback(false);
			break;

		case 5:
			this.player.a(false, true, true);
			
			break;

		case 6:
			if (this.player.vehicle instanceof EntityHorse) {
				((EntityHorse) this.player.vehicle).v(packetplayinentityaction.c());
			}
			break;

		case 7:
			if (this.player.vehicle instanceof EntityHorse) {
				((EntityHorse) this.player.vehicle).g(this.player);
			}
			break;

		default:
			throw new IllegalArgumentException("Invalid client command!");
		}

	}

	@Override
	public void a(PacketPlayInUseEntity packetplayinuseentity) {
		if (this.player.dead) {
			return; 
		}
		PlayerConnectionUtils.ensureMainThread(packetplayinuseentity, this, this.player.u());
		WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
		Entity entity = packetplayinuseentity.a(worldserver);
		
		if (entity == player && !player.isSpectator()) {
			disconnect("Cannot interact with self!");
			return;
		}

		this.player.resetIdleTimer();
		if (entity != null) {
			
			boolean flag = this.player.hasLineOfSightAccurate(entity);
			double d0;

			if (!flag) {
                
                d0 = 12.75D;
            } else {
            	
            	if (HonamiConfig.creativeBypass && this.player.playerInteractManager.getGameMode() == EnumGamemode.CREATIVE) {
            		d0 = 36.0;
            	} else {
                	d0 = HonamiConfig.maxReachSqrd;
            	}	
            }

            if (this.player.distanceSqrdAccurate(entity) <= d0) { 
				ItemStack itemInHand = this.player.inventory.getItemInHand(); 

				if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT
						|| packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
					
					boolean triggerLeashUpdate = itemInHand != null && itemInHand.getItem() == Items.LEAD
							&& entity instanceof EntityInsentient;
					Item origItem = this.player.inventory.getItemInHand() == null ? null
							: this.player.inventory.getItemInHand().getItem();
					PlayerInteractEntityEvent event;
					if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
						event = new PlayerInteractEntityEvent(this.getPlayer(), entity.getBukkitEntity());
					} else {
						Vec3D target = packetplayinuseentity.b();
						event = new PlayerInteractAtEntityEvent(this.getPlayer(), entity.getBukkitEntity(),
								new org.bukkit.util.Vector(target.a, target.b, target.c));
					}
					this.server.getPluginManager().callEvent(event);

					if (triggerLeashUpdate && (event.isCancelled() || this.player.inventory.getItemInHand() == null
							|| this.player.inventory.getItemInHand().getItem() != Items.LEAD)) {
						
						this.sendPacket(
								new PacketPlayOutAttachEntity(1, entity, ((EntityInsentient) entity).getLeashHolder()));
					}

					if (event.isCancelled() || this.player.inventory.getItemInHand() == null
							|| this.player.inventory.getItemInHand().getItem() != origItem) {
						
						this.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.datawatcher, true));
					}

					if (event.isCancelled()) {
						return;
					}
					
				}
				if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
					this.player.u(entity);

					if (itemInHand != null && itemInHand.count <= -1) {
						this.player.updateInventory(this.player.activeContainer);
					}
					
				} else if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) {
					entity.a(this.player, packetplayinuseentity.b());

					if (itemInHand != null && itemInHand.count <= -1) {
						this.player.updateInventory(this.player.activeContainer);
					}
					
				} else if (packetplayinuseentity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
					if (entity instanceof EntityItem || entity instanceof EntityExperienceOrb
							|| entity instanceof EntityArrow || (entity == this.player && !player.isSpectator())) { 
						this.disconnect("Attempting to attack an invalid entity");
						this.minecraftServer
								.warning("Player " + this.player.getName() + " tried to attack an invalid entity");
						return;
					}

					this.player.attack(entity);		
                    
                    if (this.player.isBlocking()) {
                    	this.player.bU();
                    }

					
					if (itemInHand != null && itemInHand.count <= -1) {
						this.player.updateInventory(this.player.activeContainer);
					}
					
				}
			}
		}

	}

	@Override
	public void a(PacketPlayInClientCommand packetplayinclientcommand) {
		PlayerConnectionUtils.ensureMainThread(packetplayinclientcommand, this, this.player.u());
		this.player.resetIdleTimer();
		PacketPlayInClientCommand.EnumClientCommand command = packetplayinclientcommand.a();

		switch (command) {
		case PERFORM_RESPAWN:
			if (this.player.viewingCredits) {

				this.minecraftServer.getPlayerList().changeDimension(this.player, 0,
						PlayerTeleportEvent.TeleportCause.END_PORTAL); 
																		
			} else if (this.player.u().getWorldData().isHardcore()) {
				if (this.minecraftServer.T() && this.player.getName().equals(this.minecraftServer.S())) {
					this.player.playerConnection.disconnect("You have died. Game over, man, it's game over!");
					this.minecraftServer.aa();
				} else {
					GameProfileBanEntry gameprofilebanentry = new GameProfileBanEntry(this.player.getProfile(), null,
							"(You just lost the game)", null, "Death in Hardcore");

					this.minecraftServer.getPlayerList().getProfileBans().add(gameprofilebanentry);
					this.player.playerConnection.disconnect("You have died. Game over, man, it's game over!");
				}
			} else {
				if (this.player.getHealth() > 0.0F) {
					return;
				}

				this.player = this.minecraftServer.getPlayerList().moveToWorld(this.player, 0, false);
			}
			break;

		case REQUEST_STATS:
			this.player.getStatisticManager().a(this.player);
			break;

		case OPEN_INVENTORY_ACHIEVEMENT:
			this.player.b(AchievementList.f);
		}

	}

	@Override
	public void a(PacketPlayInCloseWindow packetplayinclosewindow) {
		if (this.player.dead) {
			return; 
		}
		PlayerConnectionUtils.ensureMainThread(packetplayinclosewindow, this, this.player.u());

		if (packetplayinclosewindow.getId() == player.activeContainer.windowId) {
			CraftEventFactory.handleInventoryCloseEvent(this.player); 
			this.player.p();
		}
	}

	@Override
	public void a(PacketPlayInWindowClick packetplayinwindowclick) {
		if (this.player.dead) {
			return; 
		}
		PlayerConnectionUtils.ensureMainThread(packetplayinwindowclick, this, this.player.u());
		this.player.resetIdleTimer();

		if (this.player.activeContainer.windowId == packetplayinwindowclick.a()
				&& this.player.activeContainer.c(this.player)) {
			boolean cancelled = this.player.isSpectator(); 

				

				
				if (packetplayinwindowclick.b() < -1 && packetplayinwindowclick.b() != -999) {
					return;
				}

				InventoryView inventory = this.player.activeContainer.getBukkitView();
				SlotType type = inventory.getSlotType(packetplayinwindowclick.b());

				InventoryClickEvent event = null;
				ClickType click = ClickType.UNKNOWN;
				InventoryAction action = InventoryAction.UNKNOWN;

				ItemStack itemstack = null;

				if (packetplayinwindowclick.b() == -1) {
					type = SlotType.OUTSIDE; 
					click = packetplayinwindowclick.c() == 0 ? ClickType.WINDOW_BORDER_LEFT
							: ClickType.WINDOW_BORDER_RIGHT;
					action = InventoryAction.NOTHING;
				} else if (packetplayinwindowclick.f() == 0) {
					if (packetplayinwindowclick.c() == 0) {
						click = ClickType.LEFT;
					} else if (packetplayinwindowclick.c() == 1) {
						click = ClickType.RIGHT;
					}
					if (packetplayinwindowclick.c() == 0 || packetplayinwindowclick.c() == 1) {
						action = InventoryAction.NOTHING; 
						if (packetplayinwindowclick.b() < 0) {
							if (player.inventory.getCarried() != null) {
								action = packetplayinwindowclick.c() == 0 ? InventoryAction.DROP_ALL_CURSOR
										: InventoryAction.DROP_ONE_CURSOR;
							}
						} else {
							Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
							if (slot != null) {
								ItemStack clickedItem = slot.getItem();
								ItemStack cursor = player.inventory.getCarried();
								if (clickedItem == null) {
									if (cursor != null) {
										action = packetplayinwindowclick.c() == 0 ? InventoryAction.PLACE_ALL
												: InventoryAction.PLACE_ONE;
									}
								} else if (slot.isAllowed(player)) {
									if (cursor == null) {
										action = packetplayinwindowclick.c() == 0 ? InventoryAction.PICKUP_ALL
												: InventoryAction.PICKUP_HALF;
									} else if (slot.isAllowed(cursor)) {
										if (clickedItem.doMaterialsMatch(cursor)
												&& ItemStack.equals(clickedItem, cursor)) {
											int toPlace = packetplayinwindowclick.c() == 0 ? cursor.count : 1;
											toPlace = Math.min(toPlace,
													clickedItem.getMaxStackSize() - clickedItem.count);
											toPlace = Math.min(toPlace,
													slot.inventory.getMaxStackSize() - clickedItem.count);
											if (toPlace == 1) {
												action = InventoryAction.PLACE_ONE;
											} else if (toPlace == cursor.count) {
												action = InventoryAction.PLACE_ALL;
											} else if (toPlace < 0) {
												action = toPlace != -1 ? InventoryAction.PICKUP_SOME
														: InventoryAction.PICKUP_ONE; 
																						
											} else if (toPlace != 0) {
												action = InventoryAction.PLACE_SOME;
											}
										} else if (cursor.count <= slot.getMaxStackSize()) {
											action = InventoryAction.SWAP_WITH_CURSOR;
										}
									} else if (cursor.getItem() == clickedItem.getItem()
											&& (!cursor.usesData() || cursor.getData() == clickedItem.getData())
											&& ItemStack.equals(cursor, clickedItem)) {
										if (clickedItem.count >= 0) {
											if (clickedItem.count + cursor.count <= cursor.getMaxStackSize()) {
												
												action = InventoryAction.PICKUP_ALL;
											}
										}
									}
								}
							}
						}
					}
				} else if (packetplayinwindowclick.f() == 1) {
					if (packetplayinwindowclick.c() == 0) {
						click = ClickType.SHIFT_LEFT;
					} else if (packetplayinwindowclick.c() == 1) {
						click = ClickType.SHIFT_RIGHT;
					}
					if (packetplayinwindowclick.c() == 0 || packetplayinwindowclick.c() == 1) {
						if (packetplayinwindowclick.b() < 0) {
							action = InventoryAction.NOTHING;
						} else {
							Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
							if (slot != null && slot.isAllowed(this.player) && slot.hasItem()) {
								action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
							} else {
								action = InventoryAction.NOTHING;
							}
						}
					}
				} else if (packetplayinwindowclick.f() == 2) {
					if (packetplayinwindowclick.c() >= 0 && packetplayinwindowclick.c() < 9) {
						click = ClickType.NUMBER_KEY;
						Slot clickedSlot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
						if (clickedSlot.isAllowed(player)) {
							ItemStack hotbar = this.player.inventory.getItem(packetplayinwindowclick.c());
							boolean canCleanSwap = hotbar == null
									|| (clickedSlot.inventory == player.inventory && clickedSlot.isAllowed(hotbar)); 

																														

																														
							if (clickedSlot.hasItem()) {
								if (canCleanSwap) {
									action = InventoryAction.HOTBAR_SWAP;
								} else {
									int firstEmptySlot = player.inventory.getFirstEmptySlotIndex();
									if (firstEmptySlot > -1) {
										action = InventoryAction.HOTBAR_MOVE_AND_READD;
									} else {
										action = InventoryAction.NOTHING; 
																			
									}
								}
							} else if (!clickedSlot.hasItem() && hotbar != null && clickedSlot.isAllowed(hotbar)) {
								action = InventoryAction.HOTBAR_SWAP;
							} else {
								action = InventoryAction.NOTHING;
							}
						} else {
							action = InventoryAction.NOTHING;
						}
						
						event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.b(), click, action,
								packetplayinwindowclick.c());
					}
				} else if (packetplayinwindowclick.f() == 3) {
					if (packetplayinwindowclick.c() == 2) {
						click = ClickType.MIDDLE;
						if (packetplayinwindowclick.b() == -999) {
							action = InventoryAction.NOTHING;
						} else {
							Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
							if (slot != null && slot.hasItem() && player.abilities.canInstantlyBuild
									&& player.inventory.getCarried() == null) {
								action = InventoryAction.CLONE_STACK;
							} else {
								action = InventoryAction.NOTHING;
							}
						}
					} else {
						click = ClickType.UNKNOWN;
						action = InventoryAction.UNKNOWN;
					}
				} else if (packetplayinwindowclick.f() == 4) {
					if (packetplayinwindowclick.b() >= 0) {
						if (packetplayinwindowclick.c() == 0) {
							click = ClickType.DROP;
							Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
							if (slot != null && slot.hasItem() && slot.isAllowed(player) && slot.getItem() != null
									&& slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)) {
								action = InventoryAction.DROP_ONE_SLOT;
							} else {
								action = InventoryAction.NOTHING;
							}
						} else if (packetplayinwindowclick.c() == 1) {
							click = ClickType.CONTROL_DROP;
							Slot slot = this.player.activeContainer.getSlot(packetplayinwindowclick.b());
							if (slot != null && slot.hasItem() && slot.isAllowed(player) && slot.getItem() != null
									&& slot.getItem().getItem() != Item.getItemOf(Blocks.AIR)) {
								action = InventoryAction.DROP_ALL_SLOT;
							} else {
								action = InventoryAction.NOTHING;
							}
						}
					} else {

						click = ClickType.LEFT;
						if (packetplayinwindowclick.c() == 1) {
							click = ClickType.RIGHT;
						}
						action = InventoryAction.NOTHING;
					}
				} else if (packetplayinwindowclick.f() == 5) {
					itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.b(),
							packetplayinwindowclick.c(), 5, this.player);
				} else if (packetplayinwindowclick.f() == 6) {
					click = ClickType.DOUBLE_CLICK;
					action = InventoryAction.NOTHING;
					if (packetplayinwindowclick.b() >= 0 && this.player.inventory.getCarried() != null) {
						ItemStack cursor = this.player.inventory.getCarried();
						action = InventoryAction.NOTHING;
						
						if (inventory.getTopInventory()
								.contains(org.bukkit.Material.getMaterial(Item.getId(cursor.getItem())))
								|| inventory.getBottomInventory()
										.contains(org.bukkit.Material.getMaterial(Item.getId(cursor.getItem())))) {
							action = InventoryAction.COLLECT_TO_CURSOR;
						}
					}
				}

				if (packetplayinwindowclick.f() != 5) {
					if (click == ClickType.NUMBER_KEY) {
						event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.b(), click, action,
								packetplayinwindowclick.c());
					} else {
						event = new InventoryClickEvent(inventory, type, packetplayinwindowclick.b(), click, action);
					}

					org.bukkit.inventory.Inventory top = inventory.getTopInventory();
					if (packetplayinwindowclick.b() == 0 && top instanceof CraftingInventory) {
						org.bukkit.inventory.Recipe recipe = ((CraftingInventory) top).getRecipe();
						if (recipe != null) {
							if (click == ClickType.NUMBER_KEY) {
								event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.b(), click,
										action, packetplayinwindowclick.c());
							} else {
								event = new CraftItemEvent(recipe, inventory, type, packetplayinwindowclick.b(), click,
										action);
							}
						}
					}

					event.setCancelled(cancelled);
					server.getPluginManager().callEvent(event);

					switch (event.getResult()) {
					case ALLOW:
					case DEFAULT:
						itemstack = this.player.activeContainer.clickItem(packetplayinwindowclick.b(),
								packetplayinwindowclick.c(), packetplayinwindowclick.f(), this.player);
						
						if (itemstack != null
								&& ((itemstack.getItem() == Items.LAVA_BUCKET && PaperSpigotConfig.stackableLavaBuckets)
										|| (itemstack.getItem() == Items.WATER_BUCKET
												&& PaperSpigotConfig.stackableWaterBuckets)
										|| (itemstack.getItem() == Items.MILK_BUCKET
												&& PaperSpigotConfig.stackableMilkBuckets))) {
							if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
								this.player.updateInventory(this.player.activeContainer);
							} else {
								this.player.playerConnection.sendPacket(
										new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
								this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(
										this.player.activeContainer.windowId, packetplayinwindowclick.b(),
										this.player.activeContainer.getSlot(packetplayinwindowclick.b()).getItem()));
							}
						}
						
						break;
					case DENY:

						switch (action) {
						
						case PICKUP_ALL:
						case MOVE_TO_OTHER_INVENTORY:
						case HOTBAR_MOVE_AND_READD:
						case HOTBAR_SWAP:
						case COLLECT_TO_CURSOR:
						case UNKNOWN:
							this.player.updateInventory(this.player.activeContainer);
							break;
						
						case PICKUP_SOME:
						case PICKUP_HALF:
						case PICKUP_ONE:
						case PLACE_ALL:
						case PLACE_SOME:
						case PLACE_ONE:
						case SWAP_WITH_CURSOR:
							this.player.playerConnection
									.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
							this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(
									this.player.activeContainer.windowId, packetplayinwindowclick.b(),
									this.player.activeContainer.getSlot(packetplayinwindowclick.b()).getItem()));
							break;
						
						case DROP_ALL_SLOT:
						case DROP_ONE_SLOT:
							this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(
									this.player.activeContainer.windowId, packetplayinwindowclick.b(),
									this.player.activeContainer.getSlot(packetplayinwindowclick.b()).getItem()));
							break;
						
						case DROP_ALL_CURSOR:
						case DROP_ONE_CURSOR:
						case CLONE_STACK:
							this.player.playerConnection
									.sendPacket(new PacketPlayOutSetSlot(-1, -1, this.player.inventory.getCarried()));
							break;
						
						case NOTHING:
							break;
						}
						return;
					}

					if (event instanceof CraftItemEvent) {

						player.updateInventory(player.activeContainer);
					}
				}

				if (ItemStack.matches(packetplayinwindowclick.e(), itemstack)) {
					this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.a(),
							packetplayinwindowclick.d(), true));
					this.player.g = true;
					this.player.activeContainer.b();
					this.player.broadcastCarriedItem();
					this.player.g = false;
				} else {
					this.n.a(this.player.activeContainer.windowId, packetplayinwindowclick.d());
					this.player.playerConnection.sendPacket(new PacketPlayOutTransaction(packetplayinwindowclick.a(),
							packetplayinwindowclick.d(), false));
					this.player.activeContainer.a(this.player, false);
					ArrayList<ItemStack> arraylist1 = Lists.newArrayList();

					for (int j = 0; j < this.player.activeContainer.c.size(); ++j) {
						arraylist1.add(this.player.activeContainer.c.get(j).getItem());
					}

					this.player.a(this.player.activeContainer, arraylist1);
				}
		}

	}

	@Override
	public void a(PacketPlayInEnchantItem packetplayinenchantitem) {
		PlayerConnectionUtils.ensureMainThread(packetplayinenchantitem, this, this.player.u());
		this.player.resetIdleTimer();

		if (this.player.activeContainer.windowId == packetplayinenchantitem.a()
				&& this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
			this.player.activeContainer.a(this.player, packetplayinenchantitem.b());
			this.player.activeContainer.b();
		}

	}

	@Override
	public void a(PacketPlayInSetCreativeSlot packetplayinsetcreativeslot) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsetcreativeslot, this, this.player.u());

		if (this.player.playerInteractManager.isCreative()) {
			boolean flag = packetplayinsetcreativeslot.a() < 0;
			ItemStack itemstack = packetplayinsetcreativeslot.getItemStack();

			if (itemstack != null && itemstack.hasTag() && itemstack.getTag().hasKeyOfType("BlockEntityTag", 10)) {
				NBTTagCompound nbttagcompound = itemstack.getTag().getCompound("BlockEntityTag");

				if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z")) {
					BlockPosition blockposition = new BlockPosition(nbttagcompound.getInt("x"),
							nbttagcompound.getInt("y"), nbttagcompound.getInt("z"));
					TileEntity tileentity = this.player.world.getTileEntity(blockposition);

					if (tileentity != null) {
						NBTTagCompound nbttagcompound1 = new NBTTagCompound();

						tileentity.b(nbttagcompound1);
						nbttagcompound1.remove("x");
						nbttagcompound1.remove("y");
						nbttagcompound1.remove("z");
						itemstack.a("BlockEntityTag", nbttagcompound1);
					}
				}
			}

			boolean flag1 = packetplayinsetcreativeslot.a() >= 1
					&& packetplayinsetcreativeslot.a() < 36 + PlayerInventory.getHotbarSize();
			
			boolean flag2 = itemstack == null
					|| itemstack.getItem() != null && (!invalidItems.contains(Item.getId(itemstack.getItem()))
							|| !org.spigotmc.SpigotConfig.filterCreativeItems); 
			boolean flag3 = itemstack == null
					|| itemstack.getData() >= 0 && itemstack.count <= 64 && itemstack.count > 0;
			
			if (flag || (flag1 && !ItemStack.matches(
					this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.a()).getItem(),
					packetplayinsetcreativeslot.getItemStack()))) { 

				org.bukkit.entity.HumanEntity player = this.player.getBukkitEntity();
				InventoryView inventory = new CraftInventoryView(player, player.getInventory(),
						this.player.defaultContainer);
				org.bukkit.inventory.ItemStack item = CraftItemStack
						.asBukkitCopy(packetplayinsetcreativeslot.getItemStack());

				SlotType type = SlotType.QUICKBAR;
				if (flag) {
					type = SlotType.OUTSIDE;
				} else if (packetplayinsetcreativeslot.a() < 36) {
					if (packetplayinsetcreativeslot.a() >= 5 && packetplayinsetcreativeslot.a() < 9) {
						type = SlotType.ARMOR;
					} else {
						type = SlotType.CONTAINER;
					}
				}
				InventoryCreativeEvent event = new InventoryCreativeEvent(inventory, type,
						flag ? -999 : packetplayinsetcreativeslot.a(), item);
				server.getPluginManager().callEvent(event);

				itemstack = CraftItemStack.asNMSCopy(event.getCursor());

				switch (event.getResult()) {
				case ALLOW:
					
					flag2 = flag3 = true;
					break;
				case DEFAULT:
					break;
				case DENY:
					
					if (packetplayinsetcreativeslot.a() >= 0) {
						this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(
								this.player.defaultContainer.windowId, packetplayinsetcreativeslot.a(),
								this.player.defaultContainer.getSlot(packetplayinsetcreativeslot.a()).getItem()));
						this.player.playerConnection.sendPacket(new PacketPlayOutSetSlot(-1, -1, null));
					}
					return;
				}
			}

			if (flag1 && flag2 && flag3) {
				if (itemstack == null) {
					this.player.defaultContainer.setItem(packetplayinsetcreativeslot.a(), (ItemStack) null);
				} else {
					this.player.defaultContainer.setItem(packetplayinsetcreativeslot.a(), itemstack);
				}

				this.player.defaultContainer.a(this.player, true);
			} else if (flag && flag2 && flag3 && this.m < 200) {
				this.m += 20;
				EntityItem entityitem = this.player.drop(itemstack, true);

				if (entityitem != null) {
					entityitem.j();
				}
			}
		} else if (HonamiConfig.kickOnIllegalBehavior) {
			PlayerIllegalBehaviourEvent event = new PlayerIllegalBehaviourEvent(this.server.getPlayer(this.player),
					PlayerIllegalBehaviourEvent.IllegalType.CREATIVE_ACTION_NOT_IN_CREATIVE);
			this.server.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				this.disconnect("Perform a creative action not in creative");
			}
		}

	}

	@Override
	public void a(PacketPlayInTransaction packetplayintransaction) {
		if (this.player.dead) {
			return; 
		}
		PlayerConnectionUtils.ensureMainThread(packetplayintransaction, this, this.player.u());
		Short id = this.n.get(this.player.activeContainer.windowId);

		if (id != null && packetplayintransaction.b() == id
				&& this.player.activeContainer.windowId == packetplayintransaction.a()
				&& !this.player.activeContainer.c(this.player) && !this.player.isSpectator()) {
			this.player.activeContainer.a(this.player, true);
		}

	}

	@Override
	public void a(PacketPlayInUpdateSign packetplayinupdatesign) {
		if (this.player.dead) {
			return; 
		}
		PlayerConnectionUtils.ensureMainThread(packetplayinupdatesign, this, this.player.u());
		this.player.resetIdleTimer();
		WorldServer worldserver = this.minecraftServer.getWorldServer(this.player.dimension);
		BlockPosition blockposition = packetplayinupdatesign.a();

		if (worldserver.isLoaded(blockposition)) {
			TileEntity tileentity = worldserver.getTileEntity(blockposition);

			if (!(tileentity instanceof TileEntitySign)) {
				return;
			}

			TileEntitySign tileentitysign = (TileEntitySign) tileentity;

			if (!tileentitysign.b() || tileentitysign.c() != this.player) {
				this.minecraftServer
						.warning("Player " + this.player.getName() + " just tried to change non-editable sign");
				this.sendPacket(new PacketPlayOutUpdateSign(tileentity.world, packetplayinupdatesign.a(),
						tileentitysign.lines)); 
				return;
			}

			IChatBaseComponent[] aichatbasecomponent = packetplayinupdatesign.b();

			Player player = this.server.getPlayer(this.player);
			int x = packetplayinupdatesign.a().getX();
			int y = packetplayinupdatesign.a().getY();
			int z = packetplayinupdatesign.a().getZ();
			String[] lines = new String[4];

			for (int i = 0; i < aichatbasecomponent.length; ++i) {
				lines[i] = EnumChatFormat.a(aichatbasecomponent[i].c());
			}
			SignChangeEvent event = new SignChangeEvent(player.getWorld().getBlockAt(x, y, z),
					this.server.getPlayer(this.player), lines);
			this.server.getPluginManager().callEvent(event);

			if (!event.isCancelled()) {
				System.arraycopy(org.bukkit.craftbukkit.block.CraftSign.sanitizeLines(event.getLines()), 0,
						tileentitysign.lines, 0, 4);
				tileentitysign.isEditable = false;
			}

			tileentitysign.update();
			worldserver.notify(blockposition);
		}

	}

	private long d() {
		return System.nanoTime() / 1000000L;
	}

	@Override
	public void a(PacketPlayInAbilities packetplayinabilities) {
		PlayerConnectionUtils.ensureMainThread(packetplayinabilities, this, this.player.u());
		
		if (this.player.abilities.canFly && this.player.abilities.isFlying != packetplayinabilities.isFlying()) {
			PlayerToggleFlightEvent event = new PlayerToggleFlightEvent(this.server.getPlayer(this.player),
					packetplayinabilities.isFlying());
			this.server.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				this.player.abilities.isFlying = packetplayinabilities.isFlying(); 
																					
			} else {
				this.player.updateAbilities(); 
			}
		}
		
	}

	@Override
	public void a(PacketPlayInTabComplete packetplayintabcomplete) {
		PlayerConnectionUtils.ensureMainThread(packetplayintabcomplete, this, this.player.u());
		
        if (tabSpamLimiter.addAndGet(HonamiConfig.tabSpamIncrement) > HonamiConfig.tabSpamLimit && !this.minecraftServer.getPlayerList().isOp(this.player.getProfile())) { 
			this.disconnect("disconnect.spam");
			return;
		}        
		
		ArrayList arraylist = Lists.newArrayList();
		Iterator iterator = this.minecraftServer
				.tabCompleteCommand(this.player, packetplayintabcomplete.a(), packetplayintabcomplete.b()).iterator();

		while (iterator.hasNext()) {
			String s = (String) iterator.next();

			arraylist.add(s);
		}

		this.player.playerConnection
				.sendPacket(new PacketPlayOutTabComplete((String[]) arraylist.toArray(new String[arraylist.size()])));
	}

	@Override
	public void a(PacketPlayInSettings packetplayinsettings) {
		PlayerConnectionUtils.ensureMainThread(packetplayinsettings, this, this.player.u());
		this.player.a(packetplayinsettings);
	}

	@Override
	public void a(PacketPlayInCustomPayload packetplayincustompayload) {
		PlayerConnectionUtils.ensureMainThread(packetplayincustompayload, this, this.player.u());
		if (isExploiter) {
			return; 
		}
		PacketDataSerializer serializer;
		ItemStack itemstack;
		ItemStack itemstack1;

		try {
			try {
				String channelName = packetplayincustompayload.a();
				if ("MC|BEdit".equals(channelName) || "MC|BSign".equals(channelName)) {
					if (this.lastCustomPayloadPacketTS == -1L
							|| System.currentTimeMillis() - this.lastCustomPayloadPacketTS > 100) {
						this.lastCustomPayloadPacketTS = System.currentTimeMillis();
					} else {
						throw new IOException("Packet influx");
					}
					
				}
			} catch (Throwable ex) {
				this.isExploiter = true;
				System.out.println(this.player.getName() + " has tried to crash the server...");
				this.disconnect("Chill man, dam!");
				return;
			}

			if ("MC|BEdit".equals(packetplayincustompayload.a())) {
				if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
					disconnect("Book edited too quickly!");
					return;
				}

				serializer = new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.b()));

				try {
					itemstack = serializer.decodeItemStack();
					if (itemstack == null) {
						return;
					}

					if (!ItemBookAndQuill.b(itemstack.getTag())) {
						throw new IOException("Invalid book tag!");
					}

					itemstack1 = this.player.inventory.getItemInHand();
					if (itemstack1 != null) {
						if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack.getItem() == itemstack1.getItem()) {
							itemstack1 = new ItemStack(Items.WRITABLE_BOOK); 
							itemstack1.a("pages", itemstack.getTag().getList("pages", 8));
							CraftEventFactory.handleEditBookEvent(player, itemstack1); 
						}

						return;
					}
				} catch (Exception exception) {
					PlayerConnection.c.error("Couldn\'t handle book info", exception);
					this.disconnect("Invalid book data!"); 
					return;
				} finally {
					serializer.release();
				}

				return;
			} else if ("MC|BSign".equals(packetplayincustompayload.a())) {
				if (this.lastBookTick + 20 > MinecraftServer.currentTick) {
					disconnect("Book edited too quickly!");
					return;
				}
				serializer = new PacketDataSerializer(Unpooled.wrappedBuffer(packetplayincustompayload.b()));

				try {
					itemstack = serializer.decodeItemStack();
					if (itemstack == null) {
						return;
					}

					if (!ItemWrittenBook.b(itemstack.getTag())) {
						throw new IOException("Invalid book tag!");
					}

					itemstack1 = this.player.inventory.getItemInHand();
					if (itemstack1 != null) {
						if (itemstack.getItem() == Items.WRITTEN_BOOK && itemstack1.getItem() == Items.WRITABLE_BOOK) {
							
							itemstack1 = new ItemStack(Items.WRITTEN_BOOK);
							itemstack1.a("author", (new NBTTagString(this.player.getName())));
							itemstack1.a("title", (new NBTTagString(itemstack.getTag().getString("title"))));
							itemstack1.a("pages", itemstack.getTag().getList("pages", 8));
							itemstack1.setItem(Items.WRITTEN_BOOK);
							CraftEventFactory.handleEditBookEvent(player, itemstack1);
							
						}

						return;
					}
				} catch (Exception exception1) {
					PlayerConnection.c.error("Couldn\'t sign book", exception1);
					this.disconnect("Invalid book data!"); 
					return;
				} finally {
					serializer.release();
				}

				return;
			} else if ("MC|TrSel".equals(packetplayincustompayload.a())) {
				try {
					int i = packetplayincustompayload.b().readInt();
					Container container = this.player.activeContainer;

					if (container instanceof ContainerMerchant) {
						((ContainerMerchant) container).d(i);
					}
				} catch (Exception exception2) {
					PlayerConnection.c.error("Couldn't select trade", exception2);
					this.disconnect("Invalid trade data!"); 
				}
			} else if ("MC|AdvCdm".equals(packetplayincustompayload.a())) {
				if (!this.minecraftServer.getEnableCommandBlock()) {
					this.player.sendMessage(new ChatMessage("advMode.notEnabled"));
				} else if (this.player.getBukkitEntity().isOp() && this.player.abilities.canInstantlyBuild) { 

																												

																												

					serializer = packetplayincustompayload.b();

					try {
						byte b0 = serializer.readByte();
						CommandBlockListenerAbstract commandblocklistenerabstract = null;

						if (b0 == 0) {
							TileEntity tileentity = this.player.world.getTileEntity(new BlockPosition(
									serializer.readInt(), serializer.readInt(), serializer.readInt()));

							if (tileentity instanceof TileEntityCommand) {
								commandblocklistenerabstract = ((TileEntityCommand) tileentity).getCommandBlock();
							}
						} else if (b0 == 1) {
							Entity entity = this.player.world.a(serializer.readInt());

							if (entity instanceof EntityMinecartCommandBlock) {
								commandblocklistenerabstract = ((EntityMinecartCommandBlock) entity).getCommandBlock();
							}
						}

						String s = serializer.c(serializer.readableBytes());
						boolean flag = serializer.readBoolean();

						if (commandblocklistenerabstract != null) {
							commandblocklistenerabstract.setCommand(s);
							commandblocklistenerabstract.a(flag);
							if (!flag) {
								commandblocklistenerabstract.b((IChatBaseComponent) null);
							}

							commandblocklistenerabstract.h();
							this.player.sendMessage(new ChatMessage("advMode.setCommand.success", new Object[] { s }));
						}
					} catch (Exception exception3) {
						PlayerConnection.c.error("Couldn\'t set command block", exception3);
						this.disconnect("Invalid CommandBlock data!"); 
					} finally {
						serializer.release();
					}
				} else {
					this.player.sendMessage(new ChatMessage("advMode.notAllowed", new Object[0]));
				}
			} else if ("MC|Beacon".equals(packetplayincustompayload.a())) {
				if (this.player.activeContainer instanceof ContainerBeacon) {
					try {
						serializer = packetplayincustompayload.b();
						int j = serializer.readInt();
						int k = serializer.readInt();
						ContainerBeacon containerbeacon = (ContainerBeacon) this.player.activeContainer;
						Slot slot = containerbeacon.getSlot(0);

						if (slot.hasItem()) {
							slot.a(1);
							IInventory iinventory = containerbeacon.e();

							iinventory.b(1, j);
							iinventory.b(2, k);
							iinventory.update();
						}
					} catch (Exception exception4) {
						PlayerConnection.c.error("Couldn\'t set beacon", exception4);
						this.disconnect("Invalid beacon data!"); 
					}
				}
			} else if ("MC|ItemName".equals(packetplayincustompayload.a())
					&& this.player.activeContainer instanceof ContainerAnvil) {
				ContainerAnvil containeranvil = (ContainerAnvil) this.player.activeContainer;

				if (packetplayincustompayload.b() != null && packetplayincustompayload.b().readableBytes() >= 1) {
					String s1 = SharedConstants.a(packetplayincustompayload.b().c(32767));

					if (s1.length() <= 30) {
						containeranvil.a(s1);
					}
				} else {
					containeranvil.a("");
				}
			}
			
			else if ("REGISTER".equals(packetplayincustompayload.a())) {
				String channels = packetplayincustompayload.b().toString(com.google.common.base.Charsets.UTF_8);
				for (String channel : channels.split("\0")) {
					getPlayer().addChannel(channel);
				}
			} else if ("UNREGISTER".equals(packetplayincustompayload.a())) {
				String channels = packetplayincustompayload.b().toString(com.google.common.base.Charsets.UTF_8);
				for (String channel : channels.split("\0")) {
					getPlayer().removeChannel(channel);
				}
			} else {
				byte[] data = new byte[packetplayincustompayload.b().readableBytes()];
				packetplayincustompayload.b().readBytes(data);
				server.getMessenger().dispatchIncomingMessage(player.getBukkitEntity(), packetplayincustompayload.a(),
						data);
			}

		} finally {
			if (packetplayincustompayload.b().refCnt() > 0) {
				packetplayincustompayload.b().release();
			}
		}
		
	}

	public boolean isDisconnected() { 
		return !this.player.joining && !this.networkManager.channel.config().isAutoRead();
	}

	public void queuePacket(Packet<?> packet) {
		if (packet == null) return;
		queuedPackets.add(packet);
	}
	
	public void sendQueuedPackets() {
		networkManager.disableAutomaticFlush();
		while (!queuedPackets.isEmpty()) {
			sendPacket(queuedPackets.poll());
		}
		networkManager.enableAutomaticFlush();
	}

	static class SyntheticClass_1 {

		static final int[] a;
		static final int[] b;
		static final int[] c = new int[PacketPlayInClientCommand.EnumClientCommand.values().length];

		static {
			try {
				PlayerConnection.SyntheticClass_1.c[PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN
						.ordinal()] = 1;
			} catch (NoSuchFieldError nosuchfielderror) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.c[PacketPlayInClientCommand.EnumClientCommand.REQUEST_STATS
						.ordinal()] = 2;
			} catch (NoSuchFieldError nosuchfielderror1) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.c[PacketPlayInClientCommand.EnumClientCommand.OPEN_INVENTORY_ACHIEVEMENT
						.ordinal()] = 3;
			} catch (NoSuchFieldError nosuchfielderror2) {
				;
			}

			b = new int[PacketPlayInEntityAction.EnumPlayerAction.values().length];

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.START_SNEAKING
						.ordinal()] = 1;
			} catch (NoSuchFieldError nosuchfielderror3) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SNEAKING
						.ordinal()] = 2;
			} catch (NoSuchFieldError nosuchfielderror4) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.START_SPRINTING
						.ordinal()] = 3;
			} catch (NoSuchFieldError nosuchfielderror5) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SPRINTING
						.ordinal()] = 4;
			} catch (NoSuchFieldError nosuchfielderror6) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.STOP_SLEEPING
						.ordinal()] = 5;
			} catch (NoSuchFieldError nosuchfielderror7) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.RIDING_JUMP
						.ordinal()] = 6;
			} catch (NoSuchFieldError nosuchfielderror8) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.b[PacketPlayInEntityAction.EnumPlayerAction.OPEN_INVENTORY
						.ordinal()] = 7;
			} catch (NoSuchFieldError nosuchfielderror9) {
				;
			}

			a = new int[PacketPlayInBlockDig.EnumPlayerDigType.values().length];

			try {
				PlayerConnection.SyntheticClass_1.a[PacketPlayInBlockDig.EnumPlayerDigType.DROP_ITEM.ordinal()] = 1;
			} catch (NoSuchFieldError nosuchfielderror10) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.a[PacketPlayInBlockDig.EnumPlayerDigType.DROP_ALL_ITEMS
						.ordinal()] = 2;
			} catch (NoSuchFieldError nosuchfielderror11) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.a[PacketPlayInBlockDig.EnumPlayerDigType.RELEASE_USE_ITEM
						.ordinal()] = 3;
			} catch (NoSuchFieldError nosuchfielderror12) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.a[PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK
						.ordinal()] = 4;
			} catch (NoSuchFieldError nosuchfielderror13) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.a[PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK
						.ordinal()] = 5;
			} catch (NoSuchFieldError nosuchfielderror14) {
				;
			}

			try {
				PlayerConnection.SyntheticClass_1.a[PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK
						.ordinal()] = 6;
			} catch (NoSuchFieldError nosuchfielderror15) {
				;
			}

		}
	}
}
