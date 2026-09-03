package rein.honami.spigot.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.entity.Player;

public class InternalEventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        HonamiEventBus.getInstance().fire(new HonamiPlayerJoinEvent(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        HonamiEventBus.getInstance().fire(new HonamiPlayerQuitEvent(event.getPlayer()));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            boolean isCrit = attacker.getFallDistance() > 0.0F && !attacker.isOnGround();
            HonamiEventBus.getInstance().fire(new HonamiCombatHitEvent(attacker, victim, (float) event.getDamage(), isCrit));
        }
    }
}
