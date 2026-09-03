package rein.honami.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityVisibilityEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Player observer;
    private final Player target;
    private boolean hidden;

    public EntityVisibilityEvent(Player observer, Player target, boolean hidden) {
        this.observer = observer;
        this.target = target;
        this.hidden = hidden;
    }

    public Player getObserver() {
        return observer;
    }

    public Player getTarget() {
        return target;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
