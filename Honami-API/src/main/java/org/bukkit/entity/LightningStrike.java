package org.bukkit.entity;

public interface LightningStrike extends Weather {

	public boolean isEffect();

	public class Spigot extends Entity.Spigot {

		public boolean isSilent() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

	}

	Spigot spigot();
}
