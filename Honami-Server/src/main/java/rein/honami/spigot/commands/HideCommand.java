package rein.honami.spigot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideCommand extends Command {

	public HideCommand(String name) {
		super(name);
		this.description = "Toggle player visibility";
		this.setPermission("honami.command.hide");
		this.setUsage("/hide <player>");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) return false;
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "Usage: /hide <player>");
			return false;
		}

		Player observer = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "Player not found: " + args[0]);
			return false;
		}

		if (rein.honami.server.EntityHider.isHiddenFrom(observer, target)) {
			rein.honami.server.EntityHider.showPlayer(observer, target);
			observer.sendMessage(ChatColor.GREEN + "Now showing " + target.getName());
		} else {
			rein.honami.server.EntityHider.hidePlayer(observer, target);
			observer.sendMessage(ChatColor.GREEN + "Now hiding " + target.getName());
		}
		return true;
	}
}
