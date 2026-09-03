package rein.honami.server.combat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.World;
import org.bukkit.entity.Player;

import rein.honami.spigot.nacho.knockback.KnockbackProfile;
import rein.honami.api.combat.ICombatProfile;

public class WorldCombatManager {

	private static final Map<String, ICombatProfile> worldProfiles = new ConcurrentHashMap<>();
	private static ICombatProfile globalDefault;

	private static final Map<String, KnockbackProfile> worldKnockbackProfiles = new ConcurrentHashMap<>();
	private static KnockbackProfile globalKnockbackDefault;

	public static void setGlobalDefault(ICombatProfile profile) {
		globalDefault = profile;
	}

	public static ICombatProfile getGlobalDefault() {
		return globalDefault;
	}

	public static void setWorldProfile(String worldName, ICombatProfile profile) {
		worldProfiles.put(worldName.toLowerCase(), profile);
	}

	public static void removeWorldProfile(String worldName) {
		worldProfiles.remove(worldName.toLowerCase());
	}

	public static ICombatProfile getWorldProfile(String worldName) {
		return worldProfiles.get(worldName.toLowerCase());
	}

	public static ICombatProfile getProfileForPlayer(Player player) {
		ICombatProfile worldProfile = worldProfiles.get(player.getWorld().getName().toLowerCase());
		return worldProfile != null ? worldProfile : globalDefault;
	}

	public static ICombatProfile getProfileForWorld(World world) {
		ICombatProfile worldProfile = worldProfiles.get(world.getName().toLowerCase());
		return worldProfile != null ? worldProfile : globalDefault;
	}

	public static boolean hasWorldProfile(String worldName) {
		return worldProfiles.containsKey(worldName.toLowerCase());
	}

	public static Map<String, ICombatProfile> getAllWorldProfiles() {
		return new ConcurrentHashMap<>(worldProfiles);
	}

	public static void setGlobalKnockbackDefault(KnockbackProfile profile) {
		globalKnockbackDefault = profile;
	}

	public static KnockbackProfile getGlobalKnockbackDefault() {
		return globalKnockbackDefault;
	}

	public static void setWorldKnockbackProfile(String worldName, KnockbackProfile profile) {
		worldKnockbackProfiles.put(worldName.toLowerCase(), profile);
	}

	public static void removeWorldKnockbackProfile(String worldName) {
		worldKnockbackProfiles.remove(worldName.toLowerCase());
	}

	public static KnockbackProfile getWorldKnockbackProfile(String worldName) {
		return worldKnockbackProfiles.get(worldName.toLowerCase());
	}

	public static KnockbackProfile getKnockbackProfileForPlayer(Player player) {
		KnockbackProfile worldProfile = worldKnockbackProfiles.get(player.getWorld().getName().toLowerCase());
		return worldProfile != null ? worldProfile : globalKnockbackDefault;
	}

	public static KnockbackProfile getKnockbackProfileForWorld(World world) {
		KnockbackProfile worldProfile = worldKnockbackProfiles.get(world.getName().toLowerCase());
		return worldProfile != null ? worldProfile : globalKnockbackDefault;
	}

	public static boolean hasWorldKnockbackProfile(String worldName) {
		return worldKnockbackProfiles.containsKey(worldName.toLowerCase());
	}

	public static Map<String, KnockbackProfile> getAllWorldKnockbackProfiles() {
		return new ConcurrentHashMap<>(worldKnockbackProfiles);
	}

	public static void clearAll() {
		worldProfiles.clear();
		globalDefault = null;
		worldKnockbackProfiles.clear();
		globalKnockbackDefault = null;
	}
}
