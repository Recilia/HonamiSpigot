package rein.honami.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ChunkUnloadCommand extends Command {

	public ChunkUnloadCommand(String name) {
		super(name);
		this.description = "Unload unused chunks from memory";
		this.setPermission("honami.command.unload");
		this.setUsage("/chunkunload [world]");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) return false;

		World world;
		if (args.length > 0) {
			world = Bukkit.getWorld(args[0]);
			if (world == null) {
				sender.sendMessage(ChatColor.RED + "World not found: " + args[0]);
				return false;
			}
		} else if (sender instanceof org.bukkit.entity.Player) {
			world = ((org.bukkit.entity.Player) sender).getWorld();
		} else {
			sender.sendMessage(ChatColor.RED + "Usage: /chunkunload <world>");
			return false;
		}

		int before = world.getLoadedChunks().length;
		world.save();
		int after = world.getLoadedChunks().length;

		sender.sendMessage(ChatColor.GREEN + "Chunk operations completed for " + world.getName());
		sender.sendMessage(ChatColor.YELLOW + "Loaded chunks: " + after);
		return true;
	}
}
