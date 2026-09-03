package rein.honami.spigot.commands;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.lang.management.GarbageCollectorMXBean;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import rein.honami.spigot.Honami;
import rein.honami.spigot.config.HonamiConfig;
import rein.honami.spigot.knockback.KnockbackConfig;

import net.minecraft.server.MinecraftServer;

public class HonamiCommand extends Command {

	private static final String THEME = ChatColor.translateAlternateColorCodes('&', HonamiConfig.themeColor);
	private static final String PREFIX = THEME + "[Honami] " + ChatColor.RESET;
	private static final String SEPARATOR = ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "                         ";

	public HonamiCommand(String name) {
		super(name);
		this.description = "Honami server management command";
		this.setPermission("honami.command.main");
		this.setUsage("/honami <help|reload|save|gc|stats|debug>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) return false;

		if (args.length == 0) {
			sendHelp(sender);
			return true;
		}

		switch (args[0].toLowerCase()) {
			case "help":
				sendHelp(sender);
				break;
			case "reload":
				handleReload(sender);
				break;
			case "save":
				handleSave(sender);
				break;
			case "gc":
				handleGC(sender);
				break;
			case "stats":
			case "serverstats":
				handleStats(sender);
				break;
			case "debug":
				handleDebug(sender);
				break;
			case "version":
			case "ver":
				handleVersion(sender);
				break;
			case "tps":
				handleTPS(sender);
				break;
			default:
				sendHelp(sender);
				break;
		}
		return true;
	}

	private void sendHelp(CommandSender sender) {
		sender.sendMessage(SEPARATOR);
		sender.sendMessage(THEME + "" + ChatColor.BOLD + "Honami " + ChatColor.GRAY + "v" + MinecraftServer.getServer().getVersion());
		sender.sendMessage(SEPARATOR);
		sender.sendMessage(THEME + "/honami help" + ChatColor.GRAY + " - Show this help menu");
		sender.sendMessage(THEME + "/honami reload" + ChatColor.GRAY + " - Reload all configurations");
		sender.sendMessage(THEME + "/honami save" + ChatColor.GRAY + " - Save all configurations");
		sender.sendMessage(THEME + "/honami gc" + ChatColor.GRAY + " - Force garbage collection");
		sender.sendMessage(THEME + "/honami stats" + ChatColor.GRAY + " - View server statistics");
		sender.sendMessage(THEME + "/honami tps" + ChatColor.GRAY + " - View server TPS");
		sender.sendMessage(THEME + "/honami version" + ChatColor.GRAY + " - View Honami version");
		sender.sendMessage(THEME + "/honami debug" + ChatColor.GRAY + " - Toggle debug mode");
		sender.sendMessage(THEME + "/profiler start [sec]" + ChatColor.GRAY + " - Start profiling");
		sender.sendMessage(THEME + "/profiler stop" + ChatColor.GRAY + " - Stop profiling & view results");
		sender.sendMessage(SEPARATOR);
	}

	private void handleReload(CommandSender sender) {
		try {
			HonamiConfig.init((java.io.File) MinecraftServer.getServer().options.valueOf("honami-settings"));
			KnockbackConfig.init((java.io.File) MinecraftServer.getServer().options.valueOf("knockback-settings"));
			sender.sendMessage(PREFIX + ChatColor.GREEN + "All configurations reloaded successfully.");
		} catch (Exception e) {
			sender.sendMessage(PREFIX + ChatColor.RED + "Failed to reload configurations: " + e.getMessage());
		}
	}

	private void handleSave(CommandSender sender) {
		try {
			KnockbackConfig.save();
			for (World world : Bukkit.getWorlds()) {
				world.save();
			}
			sender.sendMessage(PREFIX + ChatColor.GREEN + "All configurations and worlds saved.");
		} catch (Exception e) {
			sender.sendMessage(PREFIX + ChatColor.RED + "Failed to save: " + e.getMessage());
		}
	}

	private void handleGC(CommandSender sender) {
		Runtime runtime = Runtime.getRuntime();
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = memoryBean.getHeapMemoryUsage();

		long usedBefore = heap.getUsed();
		long freeBefore = runtime.freeMemory();
		long maxBefore = runtime.maxMemory();

		System.gc();
		try { Thread.sleep(200); } catch (InterruptedException ignored) {}

		MemoryUsage heapAfter = memoryBean.getHeapMemoryUsage();
		long usedAfter = heapAfter.getUsed();
		long freeAfter = runtime.freeMemory();
		long maxAfter = runtime.maxMemory();
		long freed = usedBefore - usedAfter;

		sender.sendMessage(SEPARATOR);
		sender.sendMessage(THEME + "" + ChatColor.BOLD + "Garbage Collection Results");
		sender.sendMessage(SEPARATOR);
		sender.sendMessage(ChatColor.YELLOW + "Heap Used: " + ChatColor.WHITE + formatBytes(usedAfter) + " / " + formatBytes(maxAfter));
		sender.sendMessage(ChatColor.YELLOW + "Heap Free: " + ChatColor.WHITE + formatBytes(freeAfter));
		sender.sendMessage(ChatColor.YELLOW + "Max Memory: " + ChatColor.WHITE + formatBytes(maxAfter));
		sender.sendMessage(ChatColor.YELLOW + "Freed by GC: " + ChatColor.GREEN + formatBytes(freed));
		sender.sendMessage(SEPARATOR);

		sender.sendMessage(THEME + "" + ChatColor.BOLD + "Memory Pools");
		sender.sendMessage(SEPARATOR);
		for (java.lang.management.MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
			MemoryUsage usage = pool.getUsage();
			if (usage != null) {
				sender.sendMessage(ChatColor.AQUA + pool.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + formatBytes(usage.getUsed()) + " / " + formatBytes(usage.getMax()));
			}
		}
		sender.sendMessage(SEPARATOR);

		sender.sendMessage(THEME + "" + ChatColor.BOLD + "GC Statistics");
		sender.sendMessage(SEPARATOR);
		for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
			long count = gc.getCollectionCount();
			long time = gc.getCollectionTime();
			if (count > 0) {
				sender.sendMessage(ChatColor.AQUA + gc.getName() + ChatColor.GRAY + ": " + ChatColor.WHITE + count + " collections, " + time + "ms total, " + (time / count) + "ms avg");
			}
		}
		sender.sendMessage(SEPARATOR);
	}

	private void handleStats(CommandSender sender) {
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = memoryBean.getHeapMemoryUsage();
		MemoryUsage nonHeap = memoryBean.getNonHeapMemoryUsage();
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

		sender.sendMessage(SEPARATOR);
		sender.sendMessage(THEME + "" + ChatColor.BOLD + "Server Statistics");
		sender.sendMessage(SEPARATOR);
		sender.sendMessage(ChatColor.YELLOW + "TPS: " + ChatColor.WHITE + String.format("%.1f", getTPS()));
		sender.sendMessage(ChatColor.YELLOW + "Players: " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
		sender.sendMessage(ChatColor.YELLOW + "Worlds: " + ChatColor.WHITE + Bukkit.getWorlds().size());
		sender.sendMessage(ChatColor.YELLOW + "Threads: " + ChatColor.WHITE + threadBean.getThreadCount());
		sender.sendMessage(ChatColor.YELLOW + "Heap Used: " + ChatColor.WHITE + formatBytes(heap.getUsed()) + "/" + formatBytes(heap.getMax()));
		sender.sendMessage(ChatColor.YELLOW + "Non-Heap Used: " + ChatColor.WHITE + formatBytes(nonHeap.getUsed()));
		sender.sendMessage(ChatColor.YELLOW + "Loaded Chunks: " + ChatColor.WHITE + getLoadedChunks());
		sender.sendMessage(ChatColor.YELLOW + "Entities: " + ChatColor.WHITE + getEntityCount());
		sender.sendMessage(SEPARATOR);
	}

	private void handleTPS(CommandSender sender) {
		if (!HonamiConfig.tpsCommandEnabled) {
			sender.sendMessage(PREFIX + ChatColor.RED + "This command is disabled.");
			return;
		}
		double tps = getTPS();
		ChatColor color = tps >= 18.0 ? ChatColor.GREEN : tps >= 15.0 ? ChatColor.YELLOW : ChatColor.RED;
		sender.sendMessage(PREFIX + "TPS: " + color + String.format("%.1f", tps));
	}

	private void handleVersion(CommandSender sender) {
		String version = getClass().getPackage().getImplementationVersion();
		if (version == null) version = "dev";
		sender.sendMessage(PREFIX + "Honami Server v" + version);
		sender.sendMessage(PREFIX + "Based on Honami / PaperSpigot / Spigot / CraftBukkit");
		sender.sendMessage(PREFIX + "Minecraft 1.8.8");
	}

	private void handleDebug(CommandSender sender) {
		HonamiConfig.debugMode = !HonamiConfig.debugMode;
		sender.sendMessage(PREFIX + "Debug mode: " + (HonamiConfig.debugMode ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF"));
	}

	private double getTPS() {
		try {
			return MinecraftServer.getServer().tps1.getAverage();
		} catch (Exception e) {
			return 20.0;
		}
	}

	private int getLoadedChunks() {
		int total = 0;
		for (World world : Bukkit.getWorlds()) {
			total += world.getLoadedChunks().length;
		}
		return total;
	}

	private int getEntityCount() {
		int total = 0;
		for (World world : Bukkit.getWorlds()) {
			for (org.bukkit.entity.Entity entity : world.getEntities()) {
				total++;
			}
		}
		return total;
	}

	private String formatBytes(long bytes) {
		if (bytes < 1024) return bytes + " B";
		if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
		if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
		return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
	}
}
