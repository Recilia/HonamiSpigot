package org.spigotmc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;

public class TicksPerSecondCommand extends Command {

	public TicksPerSecondCommand(String name) {
		super(name);
		this.description = "Gets the current ticks per second for the server";
		this.usageMessage = "/tps";
		this.setPermission("bukkit.command.tps");
	}

	@Override
	public boolean execute(CommandSender sender, String currentAlias, String[] args) {
		if (!testPermission(sender)) {
			return true;
		}

		if (!rein.honami.spigot.config.HonamiConfig.tpsCommandEnabled) {
			sender.sendMessage(ChatColor.RED + "This command is disabled.");
			return true;
		}

		double[] tps = org.bukkit.Bukkit.spigot().getTPS();
		String[] tpsAvg = new String[tps.length];

		for (int i = 0; i < tps.length; i++) {
			tpsAvg[i] = format(tps[i]);
		}

		
		int entityCount = 0;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			entityCount = entityCount + world.entityList.size();
		}
		
		int tileEntityCount = 0;
		
		for (WorldServer world : MinecraftServer.getServer().worlds) {
			tileEntityCount = tileEntityCount + world.tileEntityList.size();
		}
		
		sender.sendMessage(ChatColor.DARK_AQUA + "Honami Performance:");
		sender.sendMessage(ChatColor.AQUA + "TPS from last 1m, 5m, 15m: "
				+ org.apache.commons.lang.StringUtils.join(tpsAvg, ", "));
		sender.sendMessage(ChatColor.AQUA + "Current Memory Usage: " + ChatColor.GREEN
				+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024)) + "/"
				+ (Runtime.getRuntime().totalMemory() / (1024 * 1024)) + " mb (Max: "
				+ (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + " mb)");
		sender.sendMessage(ChatColor.AQUA + "Online Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size());
		sender.sendMessage(ChatColor.AQUA + "Entity Count: " + ChatColor.GREEN + entityCount);
		sender.sendMessage(ChatColor.AQUA + "Tile Entity Count: " + ChatColor.GREEN + tileEntityCount);
		sender.sendMessage(ChatColor.AQUA + "Mob AI: " + ChatColor.GREEN + MinecraftServer.getServer().worlds.get(0).nachoSpigotConfig.enableMobAI);
		sender.sendMessage(ChatColor.AQUA + "Milliseconds to Run Last Tick: " + ChatColor.GREEN + Math.round(MinecraftServer.getServer().getLastMspt() * 100.0) / 100.0);
		
		return true;
	}

	private static String format(double tps) 
	{
		return ((tps > 18.0) ? ChatColor.GREEN : (tps > 16.0) ? ChatColor.YELLOW : ChatColor.RED).toString()
				+ ((tps > 21.0) ? "*" : "") + Math.min(Math.round(tps * 100.0) / 100.0, 20.0); 
	}
}
