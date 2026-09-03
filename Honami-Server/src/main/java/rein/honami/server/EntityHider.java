package rein.honami.server;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import rein.honami.spigot.config.HonamiConfig;
import rein.honami.api.event.EntityVisibilityEvent;

import com.google.common.collect.Sets;

public class EntityHider {

    private static final Map<String, Set<String>> hiddenFrom = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> hiddenBy = new ConcurrentHashMap<>();

    public static void hidePlayer(Player observer, Player target) {
        if (!HonamiConfig.entityHiderEnabled) return;
        if (observer.getUniqueId().equals(target.getUniqueId())) return;

        EntityVisibilityEvent event = new EntityVisibilityEvent(observer, target, true);
        Bukkit.getPluginManager().callEvent(event);

        hiddenFrom.computeIfAbsent(observer.getName(), k -> Sets.newConcurrentHashSet())
                .add(target.getName());
        hiddenBy.computeIfAbsent(target.getName(), k -> Sets.newConcurrentHashSet())
                .add(observer.getName());

        observer.hidePlayer(target);
    }

    public static void showPlayer(Player observer, Player target) {
        if (!HonamiConfig.entityHiderEnabled) return;
        if (observer.getUniqueId().equals(target.getUniqueId())) return;

        EntityVisibilityEvent event = new EntityVisibilityEvent(observer, target, false);
        Bukkit.getPluginManager().callEvent(event);

        Set<String> hidden = hiddenFrom.get(observer.getName());
        if (hidden != null) {
            hidden.remove(target.getName());
            if (hidden.isEmpty()) {
                hiddenFrom.remove(observer.getName());
            }
        }

        Set<String> hiddenByList = hiddenBy.get(target.getName());
        if (hiddenByList != null) {
            hiddenByList.remove(observer.getName());
            if (hiddenByList.isEmpty()) {
                hiddenBy.remove(target.getName());
            }
        }

        observer.showPlayer(target);
    }

    public static boolean isHiddenFrom(Player observer, Player target) {
        Set<String> hidden = hiddenFrom.get(observer.getName());
        return hidden != null && hidden.contains(target.getName());
    }

    public static boolean isHiddenBy(Player target, Player observer) {
        Set<String> hiddenByList = hiddenBy.get(target.getName());
        return hiddenByList != null && hiddenByList.contains(observer.getName());
    }

    public static Set<String> getHiddenPlayers(Player observer) {
        Set<String> hidden = hiddenFrom.get(observer.getName());
        return hidden != null ? Sets.newHashSet(hidden) : Sets.newHashSet();
    }

    public static Set<String> getHiddenByPlayers(Player target) {
        Set<String> hiddenByList = hiddenBy.get(target.getName());
        return hiddenByList != null ? Sets.newHashSet(hiddenByList) : Sets.newHashSet();
    }

    public static void clearAll() {
        hiddenFrom.clear();
        hiddenBy.clear();
    }

    public static void clearPlayer(Player player) {
        Set<String> hidden = hiddenFrom.remove(player.getName());
        if (hidden != null) {
            for (String targetName : hidden) {
                Set<String> hiddenByList = hiddenBy.get(targetName);
                if (hiddenByList != null) {
                    hiddenByList.remove(player.getName());
                    if (hiddenByList.isEmpty()) {
                        hiddenBy.remove(targetName);
                    }
                }
            }
        }

        Set<String> hiddenByList = hiddenBy.remove(player.getName());
        if (hiddenByList != null) {
            for (String observerName : hiddenByList) {
                Set<String> observerHidden = hiddenFrom.get(observerName);
                if (observerHidden != null) {
                    observerHidden.remove(player.getName());
                    if (observerHidden.isEmpty()) {
                        hiddenFrom.remove(observerName);
                    }
                }
            }
        }
    }
}
