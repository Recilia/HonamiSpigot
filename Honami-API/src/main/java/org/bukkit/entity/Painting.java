package org.bukkit.entity;

import org.bukkit.Art;
import org.bukkit.event.painting.PaintingBreakEvent;

public interface Painting extends Hanging {

	public Art getArt();

	public boolean setArt(Art art);

	public boolean setArt(Art art, boolean force);
}
