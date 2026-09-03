package rein.honami.spigot.vanish;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class VanishPatch {

	private static final Set<UUID> VANISHED_PLAYERS = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private static final String VANISH_METADATA_KEY = "honami:vanished";

	public static boolean isVanished(Player player) {
		if (player == null || !player.isOnline()) {
			return false;
		}
		if (VANISHED_PLAYERS.contains(player.getUniqueId())) {
			return true;
		}
		for (MetadataValue meta : player.getMetadata(VANISH_METADATA_KEY)) {
			if (meta.asBoolean()) {
				return true;
			}
		}
		return false;
	}

	public static Set<UUID> getVanishedPlayers() {
		return Collections.unmodifiableSet(VANISHED_PLAYERS);
	}

	public static void setVanished(Player player, boolean vanished) {
		if (player == null) {
			return;
		}
		player.setMetadata(VANISH_METADATA_KEY, new FixedMetadataValue(Bukkit.getPluginManager().getPlugins()[0], vanished));
		if (vanished) {
			VANISHED_PLAYERS.add(player.getUniqueId());
		} else {
			VANISHED_PLAYERS.remove(player.getUniqueId());
		}
	}

	public static boolean shouldShowEntityToPlayer(Player viewer, Entity entity) {
		if (viewer == null || entity == null) {
			return true;
		}
		if (viewer.hasPermission("honami.vanish.see")) {
			return true;
		}
		Player entityOwner = getEntityOwner(entity);
		if (entityOwner != null && isVanished(entityOwner)) {
			return false;
		}
		return true;
	}

	private static Player getEntityOwner(Entity entity) {
		if (entity instanceof Player) {
			return (Player) entity;
		}
		if (entity instanceof Projectile) {
			Projectile projectile = (Projectile) entity;
			if (projectile.getShooter() instanceof Player) {
				return (Player) projectile.getShooter();
			}
		}
		return null;
	}
}
