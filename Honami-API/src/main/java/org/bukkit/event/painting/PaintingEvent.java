package org.bukkit.event.painting;

import org.bukkit.Warning;
import org.bukkit.entity.Painting;
import org.bukkit.event.Event;

@Deprecated
@Warning(reason = "This event has been replaced by HangingEvent")
public abstract class PaintingEvent extends Event {
	protected Painting painting;

	protected PaintingEvent(final Painting painting) {
		this.painting = painting;
	}

	public Painting getPainting() {
		return painting;
	}
}
