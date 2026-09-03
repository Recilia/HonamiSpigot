package org.bukkit;

import java.util.Date;
import java.util.UUID;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;

public interface OfflinePlayer extends ServerOperator, AnimalTamer, ConfigurationSerializable {

	public boolean isOnline();

	public String getName();

	public UUID getUniqueId();

	public boolean isBanned();

	@Deprecated
	public void setBanned(boolean banned);

	public boolean isWhitelisted();

	public void setWhitelisted(boolean value);

	public Player getPlayer();

	public long getFirstPlayed();

	public long getLastPlayed();

	public boolean hasPlayedBefore();

	public Location getBedSpawnLocation();

}
