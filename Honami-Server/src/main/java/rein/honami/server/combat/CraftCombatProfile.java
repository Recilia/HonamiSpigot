package rein.honami.server.combat;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import rein.honami.api.combat.ICombatProfile;

public class CraftCombatProfile implements ICombatProfile {

    private final String name;
    private long tagDuration = 15000L;
    private double attackCooldown = 500.0;
    private boolean invulnerable = false;
    private double maxReach = 3.0;

    private final Map<UUID, Long> combatTagged = new ConcurrentHashMap<>();

    public CraftCombatProfile(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isCombatTagged(Player player) {
        Long expiry = combatTagged.get(player.getUniqueId());
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            combatTagged.remove(player.getUniqueId());
            return false;
        }
        return true;
    }

    @Override
    public void tagPlayer(Player player, Player attacker) {
        combatTagged.put(player.getUniqueId(), System.currentTimeMillis() + tagDuration);
    }

    @Override
    public void untagPlayer(Player player) {
        combatTagged.remove(player.getUniqueId());
    }

    @Override
    public long getTagDuration(Player player) {
        return tagDuration;
    }

    @Override
    public void setTagDuration(Player player, long duration) {
        this.tagDuration = duration;
    }

    @Override
    public boolean isAttackCooldownActive(Player player) {
        return false;
    }

    @Override
    public double getAttackCooldown(Player player) {
        return attackCooldown;
    }

    @Override
    public void setAttackCooldown(Player player, double cooldown) {
        this.attackCooldown = cooldown;
    }

    @Override
    public boolean isInvulnerable(Player player) {
        return invulnerable;
    }

    @Override
    public void setInvulnerable(Player player, boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    @Override
    public double getMaxReach() {
        return maxReach;
    }

    @Override
    public void setMaxReach(double reach) {
        this.maxReach = reach;
    }
}
