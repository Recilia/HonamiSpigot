package org.bukkit.help;

import org.bukkit.command.Command;

public interface HelpTopicFactory<TCommand extends Command> {

	public HelpTopic createTopic(TCommand command);
}
