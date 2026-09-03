package rein.honami.spigot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DayCommand extends Command {

	public DayCommand(String name) {
		super(name);
		this.description = "Set your personal time to day";
		this.setPermission("honami.command.day");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (!testPermission(sender)) return false;
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
			return false;
		}
		Player player = (Player) sender;
		player.setPlayerTime(6000L, false);
		player.sendMessage(ChatColor.GREEN + "Time set to day.");
		return true;
	}
}
