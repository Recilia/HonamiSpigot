package org.bukkit.command;

import java.util.List;

public interface CommandMap {

	public void registerAll(String fallbackPrefix, List<Command> commands);

	public boolean register(String label, String fallbackPrefix, Command command);

	public boolean register(String fallbackPrefix, Command command);

	public boolean dispatch(CommandSender sender, String cmdLine) throws CommandException;

	public void clearCommands();

	public Command getCommand(String name);

	public List<String> tabComplete(CommandSender sender, String cmdLine) throws IllegalArgumentException;
}
