package org.bukkit.command;

import java.util.List;

import org.bukkit.Location;

public interface TabCompleter {

	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args);

	default List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args,
			Location location) {
		return onTabComplete(sender, command, alias, args);
	}
	
}
