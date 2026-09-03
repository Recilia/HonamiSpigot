package rein.honami.spigot;

import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;

import com.google.common.collect.Sets;
import rein.honami.spigot.async.AsyncKeepAlive;
import rein.honami.spigot.async.pathsearch.SearchHandler;
import rein.honami.spigot.async.thread.CombatThread;
import rein.honami.spigot.commands.ChunkUnloadCommand;
import rein.honami.spigot.commands.DayCommand;
import rein.honami.spigot.commands.HideCommand;
import rein.honami.spigot.commands.HonamiCommand;
import rein.honami.spigot.commands.KillAllCommand;
import rein.honami.spigot.commands.KnockbackCommand;
import rein.honami.spigot.commands.MobAICommand;
import rein.honami.spigot.commands.NightCommand;
import rein.honami.spigot.commands.PingCommand;
import rein.honami.spigot.commands.SetMaxSlotCommand;
import rein.honami.spigot.commands.SpawnMobCommand;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.event.HonamiEventBus;
import rein.honami.spigot.event.InternalEventListener;
import rein.honami.spigot.world.CaveConfig;
import rein.honami.spigot.security.Log4JPatch;
import rein.honami.spigot.knockback.KnockbackConfig;
import rein.honami.spigot.protocol.MovementListener;
import rein.honami.spigot.protocol.PacketListener;
import rein.honami.spigot.anticheat.ArtemisHook;

import net.minecraft.server.MinecraftServer;
import rein.honami.api.combat.CombatAPI;
import rein.honami.api.pearl.PearlConfig;
import rein.honami.server.combat.CraftCombatAPI;
import rein.honami.server.combat.WorldCombatManager;
import rein.honami.server.pearl.CraftPearlConfig;
import rein.honami.spigot.nacho.anticrash.AntiCrash;
import rein.honami.spigot.nacho.async.AsyncExplosions;

public class Honami {

	public static final Logger LOGGER = LogManager.getLogger();
	private static final Logger DEBUG_LOGGER = LogManager.getLogger();
	private static Honami INSTANCE;
	
	private CombatThread knockbackThread;
	private CombatAPI combatAPI;
	private PearlConfig pearlConfig;
	
	private final Set<PacketListener> packetListeners = Sets.newConcurrentHashSet();
	private final Set<MovementListener> movementListeners = Sets.newConcurrentHashSet();

	private Honami() {
		Log4JPatch.patch();
		initCmds();
		
		combatAPI = new CraftCombatAPI();
		pearlConfig = new CraftPearlConfig();

		WorldCombatManager.setGlobalDefault(combatAPI.getActiveCombatProfile());
		WorldCombatManager.setGlobalKnockbackDefault(KnockbackConfig.getCurrentKb());

		if (HonamiConfig.asyncPathSearches && SearchHandler.getInstance() == null) {
			new SearchHandler();
		}
		
		if (HonamiConfig.asyncKnockback) {
			knockbackThread = new CombatThread("Knockback Thread");
		}
		if (HonamiConfig.asyncTnt) {
			AsyncExplosions.initExecutor(HonamiConfig.fixedPoolSize);
		}
		if (HonamiConfig.enableAntiCrash) {
			registerPacketListener(new AntiCrash());
		}

		CaveConfig.init();

		ArtemisHook.init();

		if (HonamiConfig.asyncKeepAliveEnabled) {
			AsyncKeepAlive.init(true, HonamiConfig.asyncKeepAliveInterval);
		}

		if (HonamiConfig.optimizedEventBus) {
			try {
				org.bukkit.plugin.Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
				if (plugins.length > 0) {
					Bukkit.getPluginManager().registerEvents(new InternalEventListener(), plugins[0]);
					Honami.debug("HonamiEventBus internal listener registered.");
				}
			} catch (Exception e) {
				Honami.debug("Failed to register HonamiEventBus listener: " + e.getMessage());
			}
		}
	}

	public void reload() {
		INSTANCE = null;
		init();
	}

	private void initCmds() {
		
		SimpleCommandMap commandMap = MinecraftServer.getServer().server.getCommandMap();
		
		if (HonamiConfig.mobAiCmd) {
			MobAICommand mobAiCommand = new MobAICommand("mobai");
			commandMap.register(mobAiCommand.getName(), "honami", mobAiCommand);
		}
		
		if (HonamiConfig.pingCmd) {
			PingCommand pingCommand = new PingCommand("ping");
			commandMap.register(pingCommand.getName(), "honami", pingCommand);
		}

		SetMaxSlotCommand setMaxSlotCommand = new SetMaxSlotCommand("sms");
		commandMap.register(setMaxSlotCommand.getName(), "honami", setMaxSlotCommand);

		SpawnMobCommand spawnMobCommand = new SpawnMobCommand("spawnmob");
		commandMap.register(spawnMobCommand.getName(), "honami", spawnMobCommand);

		KnockbackCommand knockbackCommand = new KnockbackCommand("kb");
		commandMap.register(knockbackCommand.getName(), "honami", knockbackCommand);

		HonamiCommand honamiCommand = new HonamiCommand("honami");
		commandMap.register(honamiCommand.getName(), "honami", honamiCommand);

		commandMap.register("day", "honami", new DayCommand("day"));
		commandMap.register("night", "honami", new NightCommand("night"));
		commandMap.register("killall", "honami", new KillAllCommand("killall"));
		commandMap.register("chunkunload", "honami", new ChunkUnloadCommand("chunkunload"));
		commandMap.register("hide", "honami", new HideCommand("hide"));
		commandMap.register("profiler", "honami", new rein.honami.spigot.commands.ProfilerCommand("profiler"));
	}

	public static void init() {
		if (INSTANCE == null) {
			INSTANCE = new Honami();
		}
	}

	public CombatThread getKnockbackThread() {
		return knockbackThread;
	}
	
	public CombatAPI getCombatAPI() {
		return combatAPI;
	}
	
	public PearlConfig getPearlConfig() {
		return pearlConfig;
	}
    
	public static void debug(String msg) {
		if (HonamiConfig.debugMode)
			DEBUG_LOGGER.info(msg);
	}
	
	public void registerPacketListener(PacketListener packetListener) {
		this.packetListeners.add(packetListener);
	}

	public void unregisterPacketListener(PacketListener packetListener) {
		this.packetListeners.remove(packetListener);
	}

	public Set<PacketListener> getPacketListeners() {
		return this.packetListeners;
	}

	public void registerMovementListener(MovementListener movementListener) {
		this.movementListeners.add(movementListener);
	}

	public void unregisterMovementListener(MovementListener movementListener) {
		this.movementListeners.remove(movementListener);
	}

	public Set<MovementListener> getMovementListeners() {
		return this.movementListeners;
	}
	
	public static Honami getInstance() {
		return INSTANCE;
	}
}
