package org.bukkit.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public abstract class Command {
	private String name;
	private String nextLabel;
	private String label;
	private List<String> aliases;
	private List<String> activeAliases;
	private CommandMap commandMap = null;
	protected String description = "";
	protected String usageMessage;
	private String permission;
	private String permissionMessage;
	public co.aikar.timings.Timing timings; 

	public String getTimingName() {
		return getName();
	} 

	protected Command(String name) {
		this(name, "", "/" + name, new ArrayList<>());
	}

	protected Command(String name, String description, String usageMessage, List<String> aliases) {
		this.name = name;
		this.nextLabel = name;
		this.label = name;
		this.description = description;
		this.usageMessage = usageMessage;
		this.aliases = aliases;
		this.activeAliases = new ArrayList<String>(aliases);
	}

	public abstract boolean execute(CommandSender sender, String commandLabel, String[] args);

	@Deprecated
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		Validate.notNull(sender, "Sender cannot be null");
		Validate.notNull(args, "Arguments cannot be null");
		Validate.notNull(alias, "Alias cannot be null");

		if (args.length == 0) {
			return ImmutableList.of();
		}

		String lastWord = args[args.length - 1];

		Player senderPlayer = sender instanceof Player ? (Player) sender : null;

		ArrayList<String> matchedPlayers = new ArrayList<String>();
		for (Player player : sender.getServer().getOnlinePlayers()) {
			String name = player.getName();
			if ((senderPlayer == null || senderPlayer.canSee(player))
					&& StringUtil.startsWithIgnoreCase(name, lastWord)) {
				matchedPlayers.add(name);
			}
		}

		Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
		return matchedPlayers;
	}

	

	public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location)
			throws IllegalArgumentException {

		return tabComplete(sender, alias, args);
	}

	

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		if (!isRegistered()) {
			this.name = name;
			return true;
		}
		return false;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public boolean testPermission(CommandSender target) {
		if (testPermissionSilent(target)) {
			return true;
		}

		if (permissionMessage == null) {
			target.sendMessage(ChatColor.RED
					+ "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
		} else if (permissionMessage.length() != 0) {
			for (String line : permissionMessage.replace("<permission>", permission).split("\n")) {
				target.sendMessage(line);
			}
		}

		return false;
	}

	public boolean testPermissionSilent(CommandSender target) {
		if ((permission == null) || (permission.length() == 0)) {
			return true;
		}

		for (String p : permission.split(";")) {
			if (target.hasPermission(p)) {
				return true;
			}
		}

		return false;
	}

	public String getLabel() {
		return label;
	}

	public boolean setLabel(String name) {
		this.nextLabel = name;
		if (!isRegistered()) {
			this.label = name;
			return true;
		}
		return false;
	}

	public boolean register(CommandMap commandMap) {
		if (allowChangesFrom(commandMap)) {
			this.commandMap = commandMap;
			return true;
		}

		return false;
	}

	public boolean unregister(CommandMap commandMap) {
		if (allowChangesFrom(commandMap)) {
			this.commandMap = null;
			this.activeAliases = new ArrayList<String>(this.aliases);
			this.label = this.nextLabel;
			return true;
		}

		return false;
	}

	private boolean allowChangesFrom(CommandMap commandMap) {
		return (null == this.commandMap || this.commandMap == commandMap);
	}

	public boolean isRegistered() {
		return (null != this.commandMap);
	}

	public List<String> getAliases() {
		return activeAliases;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}

	public String getDescription() {
		return description;
	}

	public String getUsage() {
		return usageMessage;
	}

	public Command setAliases(List<String> aliases) {
		this.aliases = aliases;
		if (!isRegistered()) {
			this.activeAliases = new ArrayList<String>(aliases);
		}
		return this;
	}

	public Command setDescription(String description) {
		this.description = description;
		return this;
	}

	public Command setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
		return this;
	}

	public Command setUsage(String usage) {
		this.usageMessage = usage;
		return this;
	}

	public static void broadcastCommandMessage(CommandSender source, String message) {
		broadcastCommandMessage(source, message, true);
	}

	public static void broadcastCommandMessage(CommandSender source, String message, boolean sendToSource) {
		String result = source.getName() + ": " + message;

		if (source instanceof BlockCommandSender) {
			BlockCommandSender blockCommandSender = (BlockCommandSender) source;

			if (blockCommandSender.getBlock().getWorld().getGameRuleValue("commandBlockOutput")
					.equalsIgnoreCase("false")) {
				Bukkit.getConsoleSender().sendMessage(result);
				return;
			}
		} else if (source instanceof CommandMinecart) {
			CommandMinecart commandMinecart = (CommandMinecart) source;

			if (commandMinecart.getWorld().getGameRuleValue("commandBlockOutput").equalsIgnoreCase("false")) {
				Bukkit.getConsoleSender().sendMessage(result);
				return;
			}
		}

		Set<Permissible> users = Bukkit.getPluginManager()
				.getPermissionSubscriptions(Server.BROADCAST_CHANNEL_ADMINISTRATIVE);
		String colored = ChatColor.GRAY + "" + ChatColor.ITALIC + "[" + result + ChatColor.GRAY + ChatColor.ITALIC
				+ "]";

		if (sendToSource && !(source instanceof ConsoleCommandSender)) {
			source.sendMessage(message);
		}

		for (Permissible user : users) {
			if (user instanceof CommandSender) {
				CommandSender target = (CommandSender) user;

				if (target instanceof ConsoleCommandSender) {
					target.sendMessage(result);
				} else if (target != source) {
					target.sendMessage(colored);
				}
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getName() + '(' + name + ')';
	}
}
