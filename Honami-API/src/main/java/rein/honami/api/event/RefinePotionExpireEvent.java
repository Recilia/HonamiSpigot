package rein.honami.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RefinePotionExpireEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final int effectId;
    private final int amplifier;

    public RefinePotionExpireEvent(Player player, int effectId, int amplifier) {
        this.player = player;
        this.effectId = effectId;
        this.amplifier = amplifier;
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
