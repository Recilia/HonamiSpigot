package rein.honami.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.util.Vector;

public class HonamiKnockbackEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player attacker;
    private final Player victim;
    private final Vector velocity;
    private boolean cancelled;

    public HonamiKnockbackEvent(Player attacker, Player victim, Vector velocity) {
        this.attacker = attacker;
        this.victim = victim;
        this.velocity = velocity;
    }

    public Player getAttacker() {
        return attacker;
    }

    public Player getVictim() {
        return victim;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
