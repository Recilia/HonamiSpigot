package org.bukkit.entity;

import java.util.List;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.Vector;

public interface Entity extends Metadatable, CommandSender {

	public Location getLocation();

	public Location getLocation(Location loc);

	public void setVelocity(Vector velocity);

	public Vector getVelocity();

	public boolean isOnGround();

	public World getWorld();

	public boolean teleport(Location location);

	public boolean teleport(Location location, TeleportCause cause);

	public boolean teleport(Entity destination);

	public boolean teleport(Entity destination, TeleportCause cause);

	public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z);

	public int getEntityId();

	public int getFireTicks();

	public int getMaxFireTicks();

	public void setFireTicks(int ticks);

	public void remove();

	public boolean isDead();

	public boolean isValid();

	public Server getServer();

	public abstract Entity getPassenger();

	public abstract boolean setPassenger(Entity passenger);

	public abstract boolean isEmpty();

	public abstract boolean eject();

	public float getFallDistance();

	public void setFallDistance(float distance);

	@Deprecated 
	public void setLastDamageCause(EntityDamageEvent event);

	@Deprecated 
	public EntityDamageEvent getLastDamageCause();

	public UUID getUniqueId();

	public int getTicksLived();

	public void setTicksLived(int value);

	public void playEffect(EntityEffect type);

	public EntityType getType();

	public boolean isInsideVehicle();

	public boolean leaveVehicle();

	public Entity getVehicle();

	public void setCustomName(String name);

	public String getCustomName();

	public void setCustomNameVisible(boolean flag);

	public boolean isCustomNameVisible();

	public class Spigot {

		public boolean isInvulnerable() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	Spigot spigot();

	
	public void setInvulnerable(boolean invulnerable);
	
}
