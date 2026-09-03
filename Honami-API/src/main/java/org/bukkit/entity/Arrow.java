package org.bukkit.entity;

public interface Arrow extends Projectile {

	public int getKnockbackStrength();

	public void setKnockbackStrength(int knockbackStrength);

	public boolean isCritical();

	public void setCritical(boolean critical);

	public class Spigot extends Entity.Spigot {

		public double getDamage() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public void setDamage(double damage) {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	Spigot spigot();
}
