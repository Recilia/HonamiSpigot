package rein.honami.api.combat;

import org.bukkit.entity.Player;

public interface ICombatProfile {

    String getName();

    boolean isCombatTagged(Player player);

    void tagPlayer(Player player, Player attacker);

    void untagPlayer(Player player);

    long getTagDuration(Player player);

    void setTagDuration(Player player, long duration);

    boolean isAttackCooldownActive(Player player);

    double getAttackCooldown(Player player);

    void setAttackCooldown(Player player, double cooldown);

    boolean isInvulnerable(Player player);

    void setInvulnerable(Player player, boolean invulnerable);

    double getMaxReach();

    void setMaxReach(double reach);
}
