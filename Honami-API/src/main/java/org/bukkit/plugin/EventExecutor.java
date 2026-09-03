package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;

public interface EventExecutor {
	public void execute(Listener listener, Event event) throws EventException;
}
