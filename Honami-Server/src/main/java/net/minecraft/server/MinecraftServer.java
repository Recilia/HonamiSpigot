package net.minecraft.server;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.Main;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import rein.honami.spigot.Honami;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.world.WorldTickManager;

import co.aikar.timings.SpigotTimings; 
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.ResourceLeakDetector;

import jline.console.ConsoleReader;
import joptsimple.OptionSet;

import rein.honami.spigot.nacho.async.AsyncExplosions;

import net.openhft.affinity.AffinityLock;

public abstract class MinecraftServer extends IAsyncTaskHandlerReentrant<TickTask> implements Runnable, ICommandListener, IMojangStatistics { 

	public static final Logger LOGGER = LogManager.getLogger();
	public static final File a = new File("usercache.json");
	private static MinecraftServer l;
	public Convertable convertable;
	private final MojangStatisticsGenerator n = new MojangStatisticsGenerator("server", this, az());
	public File universe;
	private final List<IUpdatePlayerListBox> p = Lists.newArrayList();
	protected final ICommandHandler b;
	public final MethodProfiler methodProfiler = new MethodProfiler();
	private ServerConnection q; 
	private final ServerPing r = new ServerPing();
	private String serverIp;
	private int u = -1;

	public int getServerPort() {
		return u;
	} 

	public WorldServer[] worldServer;
	private PlayerList v;
	private boolean isRunning = true;
	private boolean isStopped;
	private int ticks;
	protected final Proxy e;
	public String f;
	public int g;
	private boolean onlineMode;
	private boolean spawnAnimals;
	private boolean spawnNPCs;
	private boolean pvpMode;
	private boolean allowFlight;
	private String motd;
	private int F;
	private int G = 0;
	public final long[] h = new long[100];
	public long[][] i;
	private KeyPair H;
	private String I;
	private String J;
	private boolean demoMode;
	private boolean N;
	private String O = "";
	private String P = "";
	private boolean Q;
	private long R;
	private String S;
	private boolean T;
	private boolean U;
	private final YggdrasilAuthenticationService V;
	private final MinecraftSessionService W;
	private long X = 0L;
	private final GameProfileRepository Y;
	private final UserCache Z;
	
	private Thread serverThread;
	private long ab = getNanoTime(); 

	public List<WorldServer> worlds = Lists.newCopyOnWriteArrayList();
	public org.bukkit.craftbukkit.CraftServer server;
	public OptionSet options;
	public org.bukkit.command.ConsoleCommandSender console;
	public org.bukkit.command.RemoteConsoleCommandSender remoteConsole;
	public ConsoleReader reader;
	public static int currentTick = 0; 
	public final Thread primaryThread;
	public java.util.Queue<Runnable> processQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>();
	public java.util.Queue<Runnable> priorityProcessQueue = new java.util.concurrent.ConcurrentLinkedQueue<Runnable>(); 
	public int autosavePeriod;

    private static final long OVERLOADED_THRESHOLD_NANOS = 20L * TimeUnit.SECONDS.toNanos(1L) / 20L;
    private static final int OVERLOADED_TICKS_THRESHOLD = 20;
    private static final long OVERLOADED_WARNING_INTERVAL_NANOS = 10L * TimeUnit.SECONDS.toNanos(1L);
    private static final int OVERLOADED_TICKS_WARNING_INTERVAL = 100;
    private static final long NANOSECONDS_PER_TICK = TimeUnit.SECONDS.toNanos(1L) / 20L;
    private static final long NANOSECONDS_MILLI_PER_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1L);
    private boolean mayHaveDelayedTasks;
    private long delayedTasksMaxNextTickTime;
    private boolean waitingForNextTick = false;

	
	private double lastMspt;

	public MinecraftServer(OptionSet options, Proxy proxy, File file1) {
		super("Server"); 
		
		io.netty.util.ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED); 

																							
		this.e = proxy;
		MinecraftServer.l = this;

		this.Z = new UserCache(this, file1);
		this.b = this.h();

		this.V = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
		this.W = this.V.createMinecraftSessionService();
		this.Y = this.V.createProfileRepository();

		this.options = options;
		
		if (System.console() == null && System.getProperty("jline.terminal") == null) {
			System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
			Main.useJline = false;
		}

		try {
			reader = new ConsoleReader(System.in, System.out);
			reader.setExpandEvents(false); 
		} catch (Throwable e) {
			try {

				System.setProperty("jline.terminal", "jline.UnsupportedTerminal");
				System.setProperty("user.language", "en");
				Main.useJline = false;
				reader = new ConsoleReader(System.in, System.out);
				reader.setExpandEvents(false);
			} catch (IOException ex) {
				LOGGER.warn((String) null, ex);
			}
		}
		Runtime.getRuntime().addShutdownHook(new org.bukkit.craftbukkit.util.ServerShutdownThread(this));

		this.serverThread = primaryThread = new Thread(this, "Server thread"); 
	}

	public abstract PropertyManager getPropertyManager();

	protected CommandDispatcher h() {
		return new CommandDispatcher();
	}

	protected abstract boolean init() throws IOException;

	protected void a(String s) {
		if (this.getConvertable().isConvertable(s)) {
			MinecraftServer.LOGGER.info("Converting map!");
			this.b("menu.convertingLevel");
			this.getConvertable().convert(s, new IProgressUpdate() {
				private long b = System.currentTimeMillis();

				@Override
				public void a(String s) {
				}

				@Override
				public void a(int i) {
					if (System.currentTimeMillis() - this.b >= 1000L) {
						this.b = System.currentTimeMillis();
						MinecraftServer.LOGGER.info("Converting... " + i + "%");
					}

				}

				@Override
				public void c(String s) {
				}
			});
		}

	}

	protected synchronized void b(String s) {
		this.S = s;
	}

	protected void a(String s, String s1, long i, WorldType worldtype, String s2) {
		this.a(s);
		this.b("menu.loadingLevel");
		this.worldServer = new WorldServer[3];

		int worldCount = 3;

		for (int j = 0; j < worldCount; ++j) {
			WorldServer world;
			byte dimension = 0;

			if (j == 1) {
				if (getAllowNether()) {
					dimension = -1;
				} else {
					continue;
				}
			}

			if (j == 2) {
				if (server.getAllowEnd()) {
					dimension = 1;
				} else {
					continue;
				}
			}

			String worldType = org.bukkit.World.Environment.getEnvironment(dimension).toString().toLowerCase();
			String name = (dimension == 0) ? s : s + "_" + worldType;

			org.bukkit.generator.ChunkGenerator gen = this.server.getGenerator(name);
			WorldSettings worldsettings = new WorldSettings(i, this.getGamemode(), this.getGenerateStructures(),
					this.isHardcore(), worldtype);
			worldsettings.setGeneratorSettings(s2);

			if (j == 0) {
				IDataManager idatamanager = new ServerNBTManager(server.getWorldContainer(), s1, true);
				WorldData worlddata = idatamanager.getWorldData();
				if (worlddata == null) {
					worlddata = new WorldData(worldsettings, s1);
				}
				worlddata.checkName(s1); 
											
				if (this.X()) {
					world = (WorldServer) (new DemoWorldServer(this, idatamanager, worlddata, dimension,
							this.methodProfiler)).b();
				} else {
					world = (WorldServer) (new WorldServer(this, idatamanager, worlddata, dimension,
							this.methodProfiler, org.bukkit.World.Environment.getEnvironment(dimension), gen)).b();
				}

				world.a(worldsettings);
				this.server.scoreboardManager = new org.bukkit.craftbukkit.scoreboard.CraftScoreboardManager(this,
						world.getScoreboard());
			} else {
				String dim = "DIM" + dimension;

				File newWorld = new File(new File(name), dim);
				File oldWorld = new File(new File(s), dim);

				if ((!newWorld.isDirectory()) && (oldWorld.isDirectory())) {
					MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
					MinecraftServer.LOGGER.info(
							"Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your "
									+ worldType + " folder to a new location in order to operate correctly.");
					MinecraftServer.LOGGER.info(
							"We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
					MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");

					if (newWorld.exists()) {
						MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
						MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
					} else if (newWorld.getParentFile().mkdirs()) {
						if (oldWorld.renameTo(newWorld)) {
							MinecraftServer.LOGGER.info("Success! To restore " + worldType
									+ " in the future, simply move " + newWorld + " to " + oldWorld);
							
							try {
								com.google.common.io.Files.copy(new File(new File(s), "level.dat"),
										new File(new File(name), "level.dat"));
							} catch (IOException exception) {
								MinecraftServer.LOGGER.warn("Unable to migrate world data.");
							}
							MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
						} else {
							MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
							MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
						}
					} else {
						MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
						MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
					}
				}

				IDataManager idatamanager = new ServerNBTManager(server.getWorldContainer(), name, true);
				
				WorldData worlddata = idatamanager.getWorldData();
				if (worlddata == null) {
					worlddata = new WorldData(worldsettings, name);
				}
				worlddata.checkName(name); 
											
				world = (WorldServer) new SecondaryWorldServer(this, idatamanager, dimension, this.worlds.get(0),
						this.methodProfiler, worlddata, org.bukkit.World.Environment.getEnvironment(dimension), gen)
								.b();
			}

			this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldInitEvent(world.getWorld()));

			world.addIWorldAccess(new WorldManager(this, world));
			if (!this.T()) {
				world.getWorldData().setGameType(this.getGamemode());
			}

			worlds.add(world);
			getPlayerList().setPlayerFileData(worlds.toArray(new WorldServer[worlds.size()]));
		}

		this.a(this.getDifficulty());
		this.k();
	}

	protected void k() {
		this.b("menu.generatingTerrain");

		
		for (int m = 0; m < worlds.size(); m++) {
			WorldServer worldserver = this.worlds.get(m);
			LOGGER.info("Preparing start region for level " + m + " (Seed: " + worldserver.getSeed() + ")");

			if (!worldserver.getWorld().getKeepSpawnInMemory()) {
				continue;
			}

			BlockPosition blockposition = worldserver.getSpawn();
			long j = az();
			int i = 0;

			for (int k = -192; k <= 192 && this.isRunning(); k += 16) {
				for (int l = -192; l <= 192 && this.isRunning(); l += 16) {
					long i1 = az();

					if (i1 - j > 1000L) {
						this.a_("Preparing spawn area", i * 100 / 625);
						j = i1;
					}

					++i;
					worldserver.chunkProviderServer.getChunkAt(blockposition.getX() + k >> 4,
							blockposition.getZ() + l >> 4);
				}
			}
		}

		for (WorldServer world : this.worlds) {
			this.server.getPluginManager().callEvent(new org.bukkit.event.world.WorldLoadEvent(world.getWorld()));
		}
		
		this.s();
	}

	protected void a(String s, IDataManager idatamanager) {
		File file = new File(idatamanager.getDirectory(), "resources.zip");

		if (file.isFile()) {
			this.setResourcePack("level://" + s + "/" + file.getName(), "");
		}

	}

	public abstract boolean getGenerateStructures();

	public abstract WorldSettings.EnumGamemode getGamemode();

	public abstract EnumDifficulty getDifficulty();

	public abstract boolean isHardcore();

	public abstract int p();

	public abstract boolean q();

	public abstract boolean r();

	protected void a_(String s, int i) {
		this.f = s;
		this.g = i;
		MinecraftServer.LOGGER.info(s + ": " + i + "%");
	}

	protected void s() {
		this.f = null;
		this.g = 0;

		this.server.enablePlugins(org.bukkit.plugin.PluginLoadOrder.POSTWORLD); 
	}

	protected void saveChunks(boolean flag) throws ExceptionWorldConflict { 
		if (!this.N) {
			WorldServer[] aworldserver = this.worldServer;
			int i = aworldserver.length;

			for (int j = 0; j < worlds.size(); ++j) {
				WorldServer worldserver = worlds.get(j);

				if (worldserver != null) {
					if (!flag) {
						MinecraftServer.LOGGER.info("Saving chunks for level \'" + worldserver.getWorldData().getName()
								+ "\'/" + worldserver.worldProvider.getName());
					}

					try {
						worldserver.save(true, (IProgressUpdate) null);
						worldserver.saveLevel(); 
					} catch (ExceptionWorldConflict exceptionworldconflict) {
						MinecraftServer.LOGGER.warn(exceptionworldconflict.getMessage());
					}
				}
			}

		}
	}

	private boolean hasStopped = false;
	private final Object stopLock = new Object();

	public void stop() throws ExceptionWorldConflict, InterruptedException { 

		synchronized (stopLock) {
			if (hasStopped) {
				return;
			}
			hasStopped = true;
		}
		
		if (!this.N) {
			MinecraftServer.LOGGER.info("Stopping server");
			SpigotTimings.stopServer(); 

			if (this.server != null) {
				this.server.disablePlugins();
			}
			
			if (this.aq() != null) {
				this.aq().stopServer();
			}

			if (this.v != null) {
				MinecraftServer.LOGGER.info("Saving players");
				this.v.savePlayers();
				this.v.u();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				} 
			}

			if (this.worldServer != null) {
				MinecraftServer.LOGGER.info("Saving worlds");
				this.saveChunks(false);

			}

			if (this.n.d()) {
				this.n.e();
			}
			
			if (org.spigotmc.SpigotConfig.saveUserCacheOnStopOnly) {
				LOGGER.info("Saving usercache.json");
				this.Z.c();
			}

			AsyncExplosions.stopExecutor(); 
		}
	}

	public String getServerIp() {
		return this.serverIp;
	}

	public void c(String s) {
		this.serverIp = s;
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void safeShutdown() {
		this.isRunning = false;
	}

	private long lastTick = 0; 
	private static final int TPS = 20;
	private static final long SEC_IN_NANO = 1000000000;
	private static final long TICK_TIME = SEC_IN_NANO / TPS;
	private static final long MAX_CATCHUP_BUFFER = TICK_TIME * TPS * 60L;
	private static final int SAMPLE_INTERVAL = 20;
	public final RollingAverage tps1 = new RollingAverage(60);
	public final RollingAverage tps5 = new RollingAverage(60 * 5);
	public final RollingAverage tps15 = new RollingAverage(60 * 15);
    public double[] recentTps = new double[3]; 

	public static class RollingAverage {
	    private final int size;
	    private long time;
	    private java.math.BigDecimal total; 
	    private int index = 0;
	    private final java.math.BigDecimal[] samples; 
	    private final long[] times;

	    RollingAverage(int size) {
	        this.size = size;
	        this.time = size * SEC_IN_NANO;
			
	        this.total = dec(TPS).multiply(dec(SEC_IN_NANO)).multiply(dec(size));
	        this.samples = new java.math.BigDecimal[size];
			
	        this.times = new long[size];
	        for (int i = 0; i < size; i++) {
	            this.samples[i] = dec(TPS); 
	            this.times[i] = SEC_IN_NANO;
	        }
	    }

	    private static java.math.BigDecimal dec(long t) {
	        return new java.math.BigDecimal(t);
	    }
	    public void add(java.math.BigDecimal x, long t) {
		
	        time -= times[index];
	        total = total.subtract(samples[index].multiply(dec(times[index]))); 
	        samples[index] = x;
	        times[index] = t;
	        time += t;
	        total = total.add(x.multiply(dec(t))); 
	        if (++index == size) {
	            index = 0;
	        }
	    }

	    public double getAverage() {
	        return total.divide(dec(time), 30, java.math.RoundingMode.HALF_UP).doubleValue(); 
	    }
	}
	private static final java.math.BigDecimal TPS_BASE = new java.math.BigDecimal(1E9).multiply(new java.math.BigDecimal(SAMPLE_INTERVAL)); 

	private AffinityLock lock = null;

	public AffinityLock getLock() {
		return this.lock;
	}

	@Override
	public void run() {
		
		boolean disableStatistics = false;
		try {
			if (this.init()) {
				
				disableStatistics = true;
				
				if (HonamiConfig.threadAffinity) {
					LOGGER.info(" ");
					LOGGER.info("Enabling Thread Affinity...");
					lock = AffinityLock.acquireLock();
					if (lock.cpuId() != -1) {
						LOGGER.info("CPU " + lock.cpuId() + " locked for server usage.");
						LOGGER.info("This will boost the server's performance if configured properly.");
						LOGGER.info("If not it will most likely decrease performance.");
						LOGGER.info(
								"See https://github.com/OpenHFT/Java-Thread-Affinity#isolcpus for configuration!");
						LOGGER.info(" ");
					} else {
						LOGGER.error("An error occured whilst enabling thread affinity!");
						LOGGER.error(" ");
					}
					
					Honami.debug(AffinityLock.dumpLocks());

				}

				
				this.worldTickerManager = new WorldTickManager();

				this.ab = getNanoTime(); 
				this.r.setMOTD(new ChatComponentText(this.motd));
				this.r.setServerInfo(new ServerPing.ServerData("1.8.8", 47));
				this.a(this.r);

				
				Arrays.fill(recentTps, 20);
                
                long start = getNanoTime(), curTime, tickSection = start;
                lastTick = start - TICK_TIME;

				while (this.isRunning) {
                    
                    long behindTime = (curTime = getNanoTime()) - this.ab;
                    if (behindTime > OVERLOADED_THRESHOLD_NANOS + OVERLOADED_TICKS_THRESHOLD * NANOSECONDS_PER_TICK && this.ab - this.R >= OVERLOADED_WARNING_INTERVAL_NANOS + OVERLOADED_TICKS_WARNING_INTERVAL * NANOSECONDS_PER_TICK) {
                        long behindTicks = behindTime / NANOSECONDS_PER_TICK;
                        if (server.getWarnOnOverload()) { 
                            LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", behindTime / NANOSECONDS_MILLI_PER_MILLISECOND, behindTicks);
                        }
                        this.ab += behindTicks * NANOSECONDS_PER_TICK;
                        this.R = this.ab;
                    }

                    

                    if (++MinecraftServer.currentTick % SAMPLE_INTERVAL == 0) {
						final long diff = curTime - tickSection;
						
						java.math.BigDecimal currentTps = TPS_BASE.divide(new java.math.BigDecimal(diff), 30, java.math.RoundingMode.HALF_UP); 

						tps1.add(currentTps, diff);
						tps5.add(currentTps, diff);
						tps15.add(currentTps, diff);
						
						recentTps[0] = tps1.getAverage();
						recentTps[1] = tps5.getAverage();
						recentTps[2] = tps15.getAverage();
						tickSection = curTime;
						
					}
					lastTick = curTime;

                    this.ab += NANOSECONDS_PER_TICK;

                    this.A();
                    this.mayHaveDelayedTasks = true;
                    this.delayedTasksMaxNextTickTime = Math.max(getNanoTime() + NANOSECONDS_PER_TICK, this.ab);
                    this.waitUntilNextTick();

                    this.Q = true;
				}

			} else {
				this.a((CrashReport) null);
			}
		} catch (Throwable throwable) {
			MinecraftServer.LOGGER.error("Encountered an unexpected exception", throwable);
			
			if (throwable.getCause() != null) {
				MinecraftServer.LOGGER.error("\tCause of unexpected exception was", throwable.getCause());
			}
			
			CrashReport crashreport = null;

			if (throwable instanceof ReportedException) {
				crashreport = this.b(((ReportedException) throwable).a());
			} else {
				crashreport = this.b(new CrashReport("Exception in server tick loop", throwable));
			}

			File file = new File(new File(this.y(), "crash-reports"),
					"crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

			if (crashreport.a(file)) {
				MinecraftServer.LOGGER.error("This crash report has been saved to: " + file.getAbsolutePath());
			} else {
				MinecraftServer.LOGGER.error("We were unable to save this crash report to disk.");
			}

			this.a(crashreport);
		} finally {
			
			if (lock != null) {
				lock.release();
				MinecraftServer.LOGGER.info("Released CPU " + lock.cpuId() + " from server usage.");
			}

			try {
				org.spigotmc.WatchdogThread.doStop();
				this.isStopped = true;
				this.stop();
			} catch (Throwable throwable1) {
				MinecraftServer.LOGGER.error("Exception stopping the server", throwable1);
			} finally {
				
				try {
					reader.getTerminal().restore();
				} catch (Exception ignored) {
				}
				
				this.z();
			}
		}

	}

    public long getNanoTime() {
        return System.nanoTime();
    }

    private boolean haveTime() {
        return this.runningTask() || getNanoTime() < (this.mayHaveDelayedTasks ? this.delayedTasksMaxNextTickTime : this.ab);
    }

    protected void waitUntilNextTick() {
        
        this.waitingForNextTick = true;

        try {
            this.managedBlock(() -> !this.haveTime());
        } finally {
            this.waitingForNextTick = false;
        }
    }

    @Override
    protected void waitForTasks() {
        long waitTime = this.waitingForNextTick ? this.ab - getNanoTime() : IAsyncTaskHandler.BLOCK_TIME_NANOS;
        LockSupport.parkNanos("waiting for tasks", waitTime);
    }

    public TickTask wrapRunnable(final Runnable runnable) {
        return new TickTask(this.ticks, runnable);
    }

    protected boolean shouldRun(final TickTask task) {
        return task.getTick() + 3 < this.ticks || haveTime();
    }

    @Override
    protected boolean pollTask() {
        return this.mayHaveDelayedTasks = super.pollTask();
    }

    @Override
    public boolean scheduleExecutables() {
        return super.scheduleExecutables() ; 
    }

	private void a(ServerPing serverping) {
		File file = this.d("server-icon.png");

		if (file.isFile()) {
			ByteBuf bytebuf = Unpooled.buffer();
			ByteBuf bytebuf1 = null; 

			try {
				BufferedImage bufferedimage = ImageIO.read(file);

				Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
				Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
				ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
				 bytebuf1 = Base64.encode(bytebuf); 

				serverping.setFavicon("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
			} catch (Exception exception) {
				MinecraftServer.LOGGER.error("Couldn\'t load server icon", exception);
			} finally {
				bytebuf.release();
				
				if (bytebuf1 != null) {
					bytebuf1.release();
				}
				
			}
		}

	}

	public File y() {
		return new File(".");
	}

	protected void a(CrashReport crashreport) {
	}

	protected void z() {
	}

	protected void A() throws ExceptionWorldConflict { 
		co.aikar.timings.TimingsManager.FULL_SERVER_TICK.startTiming(); 
        long i = getNanoTime();
		this.server.getPluginManager().callEvent(new com.destroystokyo.paper.event.server.ServerTickStartEvent(this.ticks+1)); 
        
		++this.ticks;

		if (rein.honami.spigot.config.HonamiConfig.minemenPearlMode) {
			rein.honami.spigot.pearl.MinemenPearlMode.tick();
		}

		if (this.T) {
			this.T = false;
			this.methodProfiler.a = true;
			this.methodProfiler.a();
		}

		this.methodProfiler.a("root");
		this.B();
		if (i - this.X >= 5000000000L) {
			this.X = i;
			this.r.setPlayerSample(new ServerPing.ServerPingPlayerSample(this.J(), this.I()));
			GameProfile[] agameprofile = new GameProfile[Math.min(this.I(), 12)];
			int j = MathHelper.nextInt(ThreadLocalRandom.current(), 0, this.I() - agameprofile.length); 

			for (int k = 0; k < agameprofile.length; ++k) {
				agameprofile[k] = this.v.v().get(j + k).getProfile();
			}

			Collections.shuffle(Arrays.asList(agameprofile));
			this.r.b().a(agameprofile);
		}

		if (autosavePeriod > 0 && this.ticks % autosavePeriod == 0) { 
			SpigotTimings.worldSaveTimer.startTiming(); 
			this.methodProfiler.a("save");
			this.v.savePlayers();

			

			
			server.playerCommandState = true;
			for (World world : worlds) {
				world.getWorld().save(false);
			}
			server.playerCommandState = false;

			this.methodProfiler.b();
			SpigotTimings.worldSaveTimer.stopTiming(); 
		}
        
		this.methodProfiler.a("tallying");
		this.h[this.ticks % 100] = getNanoTime() - i;
		this.methodProfiler.b();

		this.methodProfiler.b();
		org.spigotmc.WatchdogThread.tick(); 
        
        long endTime = getNanoTime();
        long remaining = (TICK_TIME - (endTime - lastTick));
        this.lastMspt = ((double) (endTime - lastTick) / 1000000D);
        this.server.getPluginManager().callEvent(new com.destroystokyo.paper.event.server.ServerTickEndEvent(this.ticks, this.lastMspt, remaining));

		rein.honami.spigot.profiler.HonamiProfiler.getInstance().onTick(endTime - i);
		co.aikar.timings.TimingsManager.FULL_SERVER_TICK.stopTiming(); 
	}

	private WorldTickManager worldTickerManager;

	public void B() {
		SpigotTimings.minecraftSchedulerTimer.startTiming(); 
		this.methodProfiler.a("jobs");

		this.runAllTasks(); 
		
		SpigotTimings.minecraftSchedulerTimer.stopTiming(); 

		this.methodProfiler.c("levels");

		
		SpigotTimings.processQueueTimer.startTiming(); 
		while (!processQueue.isEmpty()) {
			processQueue.remove().run();
		}
		SpigotTimings.processQueueTimer.stopTiming(); 

		SpigotTimings.chunkIOTickTimer.startTiming(); 
		org.bukkit.craftbukkit.chunkio.ChunkIOExecutor.tick();
		SpigotTimings.chunkIOTickTimer.stopTiming(); 

		SpigotTimings.timeUpdateTimer.startTiming(); 

		
		int i;

		if ((this.ticks % 20) == 0) {
			for (i = 0; i < this.worlds.size(); ++i) {
				WorldServer world = this.worlds.get(i);

				final boolean doDaylight = world.getGameRules().getBoolean("doDaylightCycle");
				final long dayTime = world.getDayTime();
				long worldTime = world.getTime();
				final PacketPlayOutUpdateTime worldPacket = new PacketPlayOutUpdateTime(worldTime, dayTime, doDaylight);
				for (EntityHuman entityhuman : world.players) {
					if (!(entityhuman instanceof EntityPlayer)) {
						continue;
					}

					if (entityhuman.world == world) {
						EntityPlayer entityplayer = (EntityPlayer) entityhuman;
						long playerTime = entityplayer.getPlayerTime();
						PacketPlayOutUpdateTime packet = (playerTime == dayTime) ? worldPacket
								: new PacketPlayOutUpdateTime(worldTime, playerTime, doDaylight);
						entityplayer.playerConnection.sendPacket(packet); 
					}
				}
			}
		}
		SpigotTimings.timeUpdateTimer.stopTiming(); 

		this.worldTickerManager.tick();

		while (!priorityProcessQueue.isEmpty()) {
			priorityProcessQueue.poll().run();
		}

		this.methodProfiler.c("connection");
		SpigotTimings.connectionTimer.startTiming(); 
		this.aq().c();
		SpigotTimings.connectionTimer.stopTiming(); 
		this.methodProfiler.c("players");
		SpigotTimings.playerListTimer.startTiming(); 
		this.v.tick();
		SpigotTimings.playerListTimer.stopTiming(); 
		this.methodProfiler.c("tickables");

		SpigotTimings.tickablesTimer.startTiming(); 
		for (i = 0; i < this.p.size(); ++i) {
			this.p.get(i).c();
		}
		SpigotTimings.tickablesTimer.stopTiming(); 

		this.methodProfiler.b();
	}

	public boolean getAllowNether() {
		return true;
	}

	public void a(IUpdatePlayerListBox iupdateplayerlistbox) {
		this.p.add(iupdateplayerlistbox);
	}

    public static void main(final OptionSet options) { 
        DispenserRegistry.c();

        try {

            DedicatedServer dedicatedserver = new DedicatedServer(options);

            if (options.has("port")) {
                int port = (Integer) options.valueOf("port");
                if (port > 0) {
                    dedicatedserver.setPort(port);
                }
            }

            if (options.has("universe")) {
                dedicatedserver.universe = (File) options.valueOf("universe");
            }

            if (options.has("world")) {
                dedicatedserver.setWorld((String) options.valueOf("world"));
            }

            dedicatedserver.primaryThread.start();
            
        } catch (Exception exception) {
            MinecraftServer.LOGGER.fatal("Failed to start the minecraft server", exception);
        }

    }

	public void C() {

	}

	public File d(String s) {
		return new File(this.y(), s);
	}

	public void info(String s) {
		MinecraftServer.LOGGER.info(s);
	}

	public void warning(String s) {
		MinecraftServer.LOGGER.warn(s);
	}

	public WorldServer getWorldServer(int i) {
		
		for (WorldServer world : worlds) {
			if (world.dimension == i) {
				return world;
			}
		}
		return worlds.get(0);
		
	}

	public String E() {
		return this.serverIp;
	}

	public int F() {
		return this.u;
	}

	public String G() {
		return this.motd;
	}

	public String getVersion() {
		return "1.8.8";
	}

	public int I() {
		return this.v.getPlayerCount();
	}

	public int J() {
		return this.v.getMaxPlayers();
	}

	public String[] getPlayers() {
		return this.v.f();
	}

	public GameProfile[] L() {
		return this.v.g();
	}

	public boolean isDebugging() {
		return this.getPropertyManager().getBoolean("debug", false); 
	}

	public void g(String s) {
		MinecraftServer.LOGGER.error(s);
	}

	public void h(String s) {
		if (this.isDebugging()) {
			MinecraftServer.LOGGER.info(s);
		}

	}

	public String getServerModName() {
		return HonamiConfig.serverBrandName; 

	}

	public CrashReport b(CrashReport crashreport) {
		crashreport.g().a("Profiler Position", new Callable() {
			public String a() throws Exception {
				return MinecraftServer.this.methodProfiler.a ? MinecraftServer.this.methodProfiler.c()
						: "N/A (disabled)";
			}

			@Override
			public Object call() throws Exception {
				return this.a();
			}
		});
		if (this.v != null) {
			crashreport.g().a("Player Count", new Callable() {
				public String a() {
					return MinecraftServer.this.v.getPlayerCount() + " / " + MinecraftServer.this.v.getMaxPlayers()
							+ "; " + MinecraftServer.this.v.v();
				}

				@Override
				public Object call() throws Exception {
					return this.a();
				}
			});
		}

		return crashreport;
	}

	public List<String> tabCompleteCommand(ICommandListener icommandlistener, String s, BlockPosition blockposition) {

		return server.tabComplete(icommandlistener, s, blockposition); 
		
	}

	public static MinecraftServer getServer() {
		return MinecraftServer.l;
	}

	public boolean O() {
		return true; 
	}

	@Override
	public String getName() {
		return "Server";
	}

	@Override
	public void sendMessage(IChatBaseComponent ichatbasecomponent) {
		MinecraftServer.LOGGER.info(ichatbasecomponent.c());
	}

	@Override
	public boolean a(int i, String s) {
		return true;
	}

	public ICommandHandler getCommandHandler() {
		return this.b;
	}

	public KeyPair Q() {
		return this.H;
	}

	public int R() {
		return this.u;
	}

	public void setPort(int i) {
		this.u = i;
	}

	public String S() {
		return this.I;
	}

	public void i(String s) {
		this.I = s;
	}

	public boolean T() {
		return this.I != null;
	}

	public String U() {
		return this.J;
	}

	public void setWorld(String s) {
		this.J = s;
	}

	public void a(KeyPair keypair) {
		this.H = keypair;
	}

	public void a(EnumDifficulty enumdifficulty) {
		for (WorldServer worldserver : this.worlds) {
			if (worldserver != null) {
				if (worldserver.getWorldData().isHardcore()) {
					worldserver.getWorldData().setDifficulty(EnumDifficulty.HARD);
					worldserver.setSpawnFlags(true, true);
				} else if (this.T()) {
					worldserver.getWorldData().setDifficulty(enumdifficulty);
					worldserver.setSpawnFlags(worldserver.getDifficulty() != EnumDifficulty.PEACEFUL, true);
				} else {
					worldserver.getWorldData().setDifficulty(enumdifficulty);
					worldserver.setSpawnFlags(this.getSpawnMonsters(), this.spawnAnimals);
				}
			}
		}

	}

	protected boolean getSpawnMonsters() {
		return true;
	}

	public boolean X() {
		return this.demoMode;
	}

	public void b(boolean flag) {
		this.demoMode = flag;
	}

	public void c(boolean flag) {
		boolean M = flag;
	}

	public Convertable getConvertable() {
		return this.convertable;
	}

	public void aa() {
		this.N = true;
		this.getConvertable().d();

		for (WorldServer worldserver : this.worlds) {
			if (worldserver != null) {
				worldserver.saveLevel();
			}
		}

		this.getConvertable().e(this.worlds.get(0).getDataManager().g()); 
		this.safeShutdown();
	}

	public String getResourcePack() {
		return this.O;
	}

	public String getResourcePackHash() {
		return this.P;
	}

	public void setResourcePack(String s, String s1) {
		this.O = s;
		this.P = s1;
	}

	@Override
	public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
		mojangstatisticsgenerator.a("whitelist_enabled", Boolean.FALSE);
		mojangstatisticsgenerator.a("whitelist_count", 0);
		if (this.v != null) {
			mojangstatisticsgenerator.a("players_current", this.I());
			mojangstatisticsgenerator.a("players_max", this.J());
			mojangstatisticsgenerator.a("players_seen", this.v.getSeenPlayers().length);
		}

		mojangstatisticsgenerator.a("uses_auth", this.onlineMode);
		mojangstatisticsgenerator.a("gui_state", this.as() ? "enabled" : "disabled");
		mojangstatisticsgenerator.a("run_time", (az() - mojangstatisticsgenerator.g()) / 60L * 1000L);
		mojangstatisticsgenerator.a("avg_tick_ms", (int) (MathHelper.a(this.h) * 1.0E-6D));
		int i = 0;

		if (this.worldServer != null) {
			
			for (WorldServer worldserver : this.worlds) {
				if (worldserver != null) {
					
					WorldData worlddata = worldserver.getWorldData();

					mojangstatisticsgenerator.a("world[" + i + "][dimension]",
							worldserver.worldProvider.getDimension());
					mojangstatisticsgenerator.a("world[" + i + "][mode]", worlddata.getGameType());
					mojangstatisticsgenerator.a("world[" + i + "][difficulty]", worldserver.getDifficulty());
					mojangstatisticsgenerator.a("world[" + i + "][hardcore]", worlddata.isHardcore());
					mojangstatisticsgenerator.a("world[" + i + "][generator_name]", worlddata.getType().name());
					mojangstatisticsgenerator.a("world[" + i + "][generator_version]",
							worlddata.getType().getVersion());
					mojangstatisticsgenerator.a("world[" + i + "][height]", this.F);
					mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", worldserver.N().getLoadedChunks());
					++i;
				}
			}
		}

		mojangstatisticsgenerator.a("worlds", i);
	}

	@Override
	public void b(MojangStatisticsGenerator mojangstatisticsgenerator) {
		mojangstatisticsgenerator.b("singleplayer", this.T());
		mojangstatisticsgenerator.b("server_brand", this.getServerModName());
		mojangstatisticsgenerator.b("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
		mojangstatisticsgenerator.b("dedicated", this.ae());
	}

	@Override
	public boolean getSnooperEnabled() {
		return false;
	}

	public abstract boolean ae();

	public boolean getOnlineMode() {
		return server.getOnlineMode(); 
	}

	public void setOnlineMode(boolean flag) {
		this.onlineMode = flag;
	}

	public boolean getSpawnAnimals() {
		return this.spawnAnimals;
	}

	public void setSpawnAnimals(boolean flag) {
		this.spawnAnimals = flag;
	}

	public boolean getSpawnNPCs() {
		return this.spawnNPCs;
	}

	public abstract boolean ai();

	public abstract ServerConnection.EventGroupType getTransport();

	public void setSpawnNPCs(boolean flag) {
		this.spawnNPCs = flag;
	}

	public boolean getPVP() {
		return this.pvpMode;
	}

	public void setPVP(boolean flag) {
		this.pvpMode = flag;
	}

	public boolean getAllowFlight() {
		return this.allowFlight;
	}

	public void setAllowFlight(boolean flag) {
		this.allowFlight = flag;
	}

	public abstract boolean getEnableCommandBlock();

	public String getMotd() {
		return this.motd;
	}

	public void setMotd(String s) {
		this.motd = s;
	}

	public int getMaxBuildHeight() {
		return this.F;
	}

	public void c(int i) {
		this.F = i;
	}

	public boolean isStopped() {
		return this.isStopped;
	}

	public PlayerList getPlayerList() {
		return this.v;
	}

	public void a(PlayerList playerlist) {
		this.v = playerlist;
	}

	public void setGamemode(WorldSettings.EnumGamemode worldsettings_enumgamemode) {
		
		for (int i = 0; i < this.worlds.size(); ++i) {
			getServer().worlds.get(i).getWorldData().setGameType(worldsettings_enumgamemode);
		}

	}

	public ServerConnection getServerConnection() {
		return this.q;
	}

	public ServerConnection aq() {
		return this.q == null ? this.q = new ServerConnection(this) : this.q; 
	}

	public boolean as() {
		return false;
	}

	public abstract String a(WorldSettings.EnumGamemode worldsettings_enumgamemode, boolean flag);

	public int at() {
		return this.ticks;
	}

	public void au() {
		this.T = true;
	}

	@Override
	public BlockPosition getChunkCoordinates() {
		return BlockPosition.ZERO;
	}

	@Override
	public Vec3D d() {
		return new Vec3D(0.0D, 0.0D, 0.0D);
	}

	@Override
	public World getWorld() {
		return this.worlds.get(0); 
	}

	@Override
	public Entity f() {
		return null;
	}

	public int getSpawnProtection() {
		return 16;
	}

	public boolean a(World world, BlockPosition blockposition, EntityHuman entityhuman) {
		return false;
	}

	public void setForceGamemode(boolean flag) {
		this.U = flag;
	}

	public boolean getForceGamemode() {
		return this.U;
	}

	public Proxy ay() {
		return this.e;
	}
	
	public static long az() {
		return System.currentTimeMillis();
	}

	public int getIdleTimeout() {
		return this.G;
	}

	public void setIdleTimeout(int i) {
		this.G = i;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		return new ChatComponentText(this.getName());
	}

	public boolean aB() {
		return true;
	}

	public MinecraftSessionService aD() {
		return this.W;
	}

	public GameProfileRepository getGameProfileRepository() {
		return this.Y;
	}

	public UserCache getUserCache() {
		return this.Z;
	}

	public ServerPing aG() {
		return this.r;
	}

	public void aH() {
		this.X = 0L;
	}

	public Entity a(UUID uuid) {
		WorldServer[] aworldserver = this.worldServer;
		int i = aworldserver.length;

		for (int j = 0; j < worlds.size(); ++j) {
			WorldServer worldserver = worlds.get(j);

			if (worldserver != null) {
				Entity entity = worldserver.getEntity(uuid);

				if (entity != null) {
					return entity;
				}
			}
		}

		return null;
	}

	@Override
	public boolean getSendCommandFeedback() {
		return getServer().worlds.get(0).getGameRules().getBoolean("sendCommandFeedback");
	}

	@Override
	public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i) {
	}

	public int aI() {
		return 29999984;
	}

 

	public int aK() {
		return 256;
	}

	public long aL() {
		return this.ab;
	}

	public Thread aM() {
		return this.serverThread;
	}

	public Honami getHonami() {
		return Honami.getInstance();
	}

	public double getLastMspt() {
		return this.lastMspt;
	}
}
