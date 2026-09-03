package rein.honami.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RefinePotionAddEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final int effectId;
    private final int amplifier;
    private boolean cancelled;

    public RefinePotionAddEvent(Player player, int effectId, int amplifier) {
        this.player = player;
        this.effectId = effectId;
        this.amplifier = amplifier;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public int getEffectId() {
        return effectId;
    }

    public int getAmplifier() {
        return amplifier;
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
