package io.papermc.paper.network;

import io.netty.channel.Channel;
import net.kyori.adventure.key.Key;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ChannelInitializeListenerHolder {

    private static final Map<Key, ChannelInitializeListener> LISTENERS = new HashMap<>();
    private static final Map<Key, ChannelInitializeListener> IMMUTABLE_VIEW = Collections.unmodifiableMap(LISTENERS);

    private ChannelInitializeListenerHolder() {
    }

    public static boolean hasListener(@NonNull Key key) {
        return LISTENERS.containsKey(key);
    }

    public static void addListener(@NonNull Key key, @NonNull ChannelInitializeListener listener) {
        LISTENERS.put(key, listener);
    }

    public static @Nullable ChannelInitializeListener removeListener(@NonNull Key key) {
        return LISTENERS.remove(key);
    }

    public static @NonNull Map<Key, ChannelInitializeListener> getListeners() {
        return IMMUTABLE_VIEW;
    }

    public static void callListeners(@NonNull Channel channel) {
        for (ChannelInitializeListener listener : LISTENERS.values()) {
            listener.afterInitChannel(channel);
        }
    }
}