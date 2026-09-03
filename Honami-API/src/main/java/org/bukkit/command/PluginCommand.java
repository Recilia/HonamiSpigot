package org.bukkit.command;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public final class PluginCommand extends Command implements PluginIdentifiableCommand {
	private final Plugin owningPlugin;
	private CommandExecutor executor;
	private TabCompleter completer;

	protected PluginCommand(String name, Plugin owner) {
		super(name);
		this.executor = owner;
		this.owningPlugin = owner;
		this.usageMessage = "";
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		boolean success = false;

		if (!owningPlugin.isEnabled()) {
			return false;
		}

		if (!testPermission(sender)) {
			return true;
		}

		try {
			success = executor.onCommand(sender, this, commandLabel, args);
		} catch (Throwable ex) {
			throw new CommandException("Unhandled exception executing command '" + commandLabel + "' in plugin "
					+ owningPlugin.getDescription().getFullName(), ex);
		}

		if (!success && usageMessage.length() > 0) {
			for (String line : usageMessage.replace("<command>", commandLabel).split("\n")) {
				sender.sendMessage(line);
			}
		}

		return success;
	}

	public void setExecutor(CommandExecutor executor) {
		this.executor = executor == null ? owningPlugin : executor;
	}

	public CommandExecutor getExecutor() {
		return executor;
	}

	public void setTabCompleter(TabCompleter completer) {
		this.completer = completer;
	}

	public TabCompleter getTabCompleter() {
		return completer;
	}

	public Plugin getPlugin() {
		return owningPlugin;
	}

	@Override
	public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args)
			throws CommandException, IllegalArgumentException {
		return tabComplete(sender, alias, args, null); 
														
	}

	

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location)
			throws CommandException, IllegalArgumentException {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "Arguments cannot be null");
		Validate.notNull(alias, "Alias cannot be null");

		List<String> completions = null;
		try {
			if (completer != null) {
				completions = completer.onTabComplete(sender, this, alias, args, location); 
																							
			}
			if (completions == null && executor instanceof TabCompleter) {
				completions = ((TabCompleter) executor).onTabComplete(sender, this, alias, args, location); 

																											
			}
		} catch (Throwable ex) {
			StringBuilder message = new StringBuilder();
			message.append("Unhandled exception during tab completion for command '/").append(alias).append(' ');
			for (String arg : args) {
				message.append(arg).append(' ');
			}
			message.deleteCharAt(message.length() - 1).append("' in plugin ")
					.append(owningPlugin.getDescription().getFullName());
			throw new CommandException(message.toString(), ex);
		}

		if (completions == null) {
			return super.tabComplete(sender, alias, args);
		}
		return completions;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(super.toString());
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append(", ").append(owningPlugin.getDescription().getFullName()).append(')');
		return stringBuilder.toString();
	}
}
