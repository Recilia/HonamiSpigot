package rein.honami.api.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerHealthChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final double oldHealth;
    private final double newHealth;
    private final double maxHealth;
    private boolean cancelled;

    public PlayerHealthChangeEvent(Player player, double oldHealth, double newHealth, double maxHealth) {
        super(player);
        this.oldHealth = oldHealth;
        this.newHealth = newHealth;
        this.maxHealth = maxHealth;
        this.cancelled = false;
    }

    public double getOldHealth() {
        return oldHealth;
    }

    public double getNewHealth() {
        return newHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
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
