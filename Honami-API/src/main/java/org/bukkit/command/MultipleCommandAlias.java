package org.bukkit.command;

public class MultipleCommandAlias extends Command {
	private Command[] commands;

	public MultipleCommandAlias(String name, Command[] commands) {
		super(name);
		this.commands = commands;
	}

	public Command[] getCommands() {
		return commands;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean result = false;

		for (Command command : commands) {
			result |= command.execute(sender, commandLabel, args);
		}

		return result;
	}
}
