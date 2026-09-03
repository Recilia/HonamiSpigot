package rein.honami.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class KillAllCommand extends Command {

	public KillAllCommand(String name) {
		super(name);
		this.description = "Clear entities in the world";
		this.setPermission("honami.command.killall");
		this.setUsage("/killall [mobs|items|xp|tnt] [world]");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) return false;

		String filter = "all";
		String worldName = null;

		if (args.length > 0) {
			String first = args[0].toLowerCase();
			if (first.equals("mobs") || first.equals("items") || first.equals("xp") || first.equals("tnt") || first.equals("all")) {
				filter = first;
				if (args.length > 1) {
					worldName = args[1];
				}
			} else {
				worldName = args[0];
			}
		}

		World world;
		if (worldName != null) {
			world = Bukkit.getWorld(worldName);
			if (world == null) {
				sender.sendMessage(ChatColor.RED + "World not found: " + worldName);
				return false;
			}
		} else if (sender instanceof Player) {
			world = ((Player) sender).getWorld();
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: /killall [mobs|items|xp|tnt] [world]");
			return false;
		}

		final String finalFilter = filter;
		int count = 0;
		for (org.bukkit.entity.Entity entity : world.getEntities()) {
			if (entity instanceof Player) continue;
			if (!shouldKill(entity, finalFilter)) continue;
			entity.remove();
			count++;
		}

		String label = finalFilter.equals("all") ? "entities" : finalFilter;
		sender.sendMessage(ChatColor.GREEN + "Removed " + count + " " + label + " from " + world.getName());
		return true;
	}

	private boolean shouldKill(org.bukkit.entity.Entity entity, String filter) {
		switch (filter) {
			case "mobs":
				return entity instanceof org.bukkit.entity.LivingEntity;
			case "items":
				return entity instanceof org.bukkit.entity.Item;
			case "xp":
				return entity instanceof org.bukkit.entity.ExperienceOrb;
			case "tnt":
				return entity.getType() == EntityType.PRIMED_TNT;
			default:
				return true;
		}
	}
}
