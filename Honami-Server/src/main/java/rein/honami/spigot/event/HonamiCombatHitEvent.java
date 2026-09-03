package rein.honami.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HonamiCombatHitEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player attacker;
    private final Player victim;
    private final float damage;
    private final boolean isCrit;

    public HonamiCombatHitEvent(Player attacker, Player victim, float damage, boolean isCrit) {
        this.attacker = attacker;
        this.victim = victim;
        this.damage = damage;
        this.isCrit = isCrit;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getVictim() {
        return victim;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isCrit() {
        return isCrit;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
