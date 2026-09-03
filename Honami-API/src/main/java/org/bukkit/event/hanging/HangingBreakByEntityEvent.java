package org.bukkit.event.hanging;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;

public class HangingBreakByEntityEvent extends HangingBreakEvent {
	private final Entity remover;

	public HangingBreakByEntityEvent(final Hanging hanging, final Entity remover) {
		super(hanging, HangingBreakEvent.RemoveCause.ENTITY);
		this.remover = remover;
	}

	public Entity getRemover() {
		return remover;
	}
}
