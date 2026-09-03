package rein.honami.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCombatTagEnterEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Player attacker;
    private boolean cancelled;

    public PlayerCombatTagEnterEvent(Player player, Player attacker) {
        this.player = player;
        this.attacker = attacker;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getAttacker() {
        return attacker;
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
