package org.bukkit.event.hanging;

import org.bukkit.entity.Hanging;
import org.bukkit.event.Event;

public abstract class HangingEvent extends Event {
	protected Hanging hanging;

	protected HangingEvent(final Hanging painting) {
		this.hanging = painting;
	}

	public Hanging getEntity() {
		return hanging;
	}
}
