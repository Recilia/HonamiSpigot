package rein.honami.spigot.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.event.Event;

public class HonamiEventBus {

    private static final HonamiEventBus INSTANCE = new HonamiEventBus();
    private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<EventHandler<?>>> listeners = new ConcurrentHashMap<>();

    private HonamiEventBus() {
    }

    public static HonamiEventBus getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void register(Class<T> eventClass, EventHandler<T> handler) {
        listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>())
                .add((EventHandler<?>) handler);
    }

    public <T extends Event> void unregister(Class<T> eventClass, EventHandler<T> handler) {
        CopyOnWriteArrayList<EventHandler<?>> handlers = listeners.get(eventClass);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Event> void fire(T event) {
        CopyOnWriteArrayList<EventHandler<?>> handlers = listeners.get(event.getClass());
        if (handlers != null) {
            for (EventHandler<?> handler : handlers) {
                ((EventHandler<T>) handler).handle(event);
            }
        }
    }

    @FunctionalInterface
    public interface EventHandler<T extends Event> {
        void handle(T event);
    }
}
