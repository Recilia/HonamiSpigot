package org.bukkit.plugin;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RegisteredListener {
	private final Listener listener;
	private final EventPriority priority;
	private final Plugin plugin;
	private final EventExecutor executor;
	private final boolean ignoreCancelled;

	public RegisteredListener(final Listener listener, final EventExecutor executor, final EventPriority priority,
			final Plugin plugin, final boolean ignoreCancelled) {
		this.listener = listener;
		this.priority = priority;
		this.plugin = plugin;
		this.executor = executor;
		this.ignoreCancelled = ignoreCancelled;
	}

	public Listener getListener() {
		return listener;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public EventPriority getPriority() {
		return priority;
	}

	public void callEvent(final Event event) throws EventException {
		if (event instanceof Cancellable) {
			if (((Cancellable) event).isCancelled() && isIgnoringCancelled()) {
				return;
			}
		}
		executor.execute(listener, event);
	}

	public boolean isIgnoringCancelled() {
		return ignoreCancelled;
	}
}
