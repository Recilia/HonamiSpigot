
package org.bukkit.command;

public interface ProxiedCommandSender extends CommandSender {

	CommandSender getCaller();

	CommandSender getCallee();

}
