package rein.honami.api.combat;

import java.util.Collection;

import org.bukkit.entity.Player;

public interface CombatAPI {

    ICombatProfile getCombatProfile(String name);

    Collection<ICombatProfile> getCombatProfiles();

    void registerCombatProfile(ICombatProfile profile);

    void unregisterCombatProfile(String name);

    ICombatProfile getActiveCombatProfile();

    void setActiveCombatProfile(ICombatProfile profile);

    boolean isCombatTagged(Player player);

    void tagPlayer(Player player, Player attacker);

    void untagPlayer(Player player);

    boolean isAttackCooldownActive(Player player);

    double getAttackCooldown(Player player);

    boolean isInvulnerable(Player player);
}
