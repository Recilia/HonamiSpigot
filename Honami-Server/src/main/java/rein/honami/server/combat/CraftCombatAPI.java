package rein.honami.server.combat;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import rein.honami.api.combat.CombatAPI;
import rein.honami.api.combat.ICombatProfile;

public class CraftCombatAPI implements CombatAPI {

    private final Map<String, ICombatProfile> profiles = new ConcurrentHashMap<>();
    private final Map<UUID, Long> combatTagged = new ConcurrentHashMap<>();
    private final Map<UUID, Long> combatTagDuration = new ConcurrentHashMap<>();
    private final Map<UUID, Double> attackCooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, Boolean> invulnerable = new ConcurrentHashMap<>();
    private ICombatProfile activeProfile;

    public CraftCombatAPI() {
        CraftCombatProfile defaultProfile = new CraftCombatProfile("default");
        defaultProfile.setMaxReach(3.0);
        profiles.put("default", defaultProfile);
        activeProfile = defaultProfile;
    }

    @Override
    public ICombatProfile getCombatProfile(String name) {
        return profiles.get(name);
    }

    @Override
    public Collection<ICombatProfile> getCombatProfiles() {
        return profiles.values();
    }

    @Override
    public void registerCombatProfile(ICombatProfile profile) {
        profiles.put(profile.getName(), profile);
    }

    @Override
    public void unregisterCombatProfile(String name) {
        profiles.remove(name);
    }

    @Override
    public ICombatProfile getActiveCombatProfile() {
        return activeProfile;
    }

    @Override
    public void setActiveCombatProfile(ICombatProfile profile) {
        this.activeProfile = profile;
    }

    @Override
    public boolean isCombatTagged(Player player) {
        Long expiry = combatTagged.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            combatTagged.remove(player.getUniqueId());
            combatTagDuration.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    @Override
    public void tagPlayer(Player player, Player attacker) {
        combatTagged.put(player.getUniqueId(), System.currentTimeMillis() + (activeProfile.getTagDuration(player)));
        combatTagDuration.put(player.getUniqueId(), (long) activeProfile.getTagDuration(player));
    }

    @Override
    public void untagPlayer(Player player) {
        combatTagged.remove(player.getUniqueId());
        combatTagDuration.remove(player.getUniqueId());
    }

    @Override
    public boolean isAttackCooldownActive(Player player) {
        Double expiry = attackCooldowns.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            attackCooldowns.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    @Override
    public double getAttackCooldown(Player player) {
        Double expiry = attackCooldowns.get(player.getUniqueId());
        if (expiry == null) return 0;
        return Math.max(0, expiry - System.currentTimeMillis());
    }

    @Override
    public boolean isInvulnerable(Player player) {
        Boolean invul = invulnerable.get(player.getUniqueId());
        return invul != null && invul;
    }
}
