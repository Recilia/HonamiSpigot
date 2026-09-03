package org.bukkit.block;

import org.bukkit.Material;

public interface Jukebox extends BlockState {

	public Material getPlaying();

	public void setPlaying(Material record);

	public boolean isPlaying();

	public boolean eject();
}
