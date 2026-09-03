package rein.honami.spigot.pearl;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class MinemenPearlMode {

    private static final ConcurrentLinkedQueue<PendingTeleport> pendingTeleports = new ConcurrentLinkedQueue<>();

    public static void scheduleTeleport(EntityPlayer player, Location destination, int delayTicks) {
        long scheduledTick = MinecraftServer.getServer().at() + delayTicks;
        pendingTeleports.add(new PendingTeleport(player.getUniqueID(), destination, scheduledTick));
    }

    public static void tick() {
        long currentTick = MinecraftServer.getServer().at();
        PendingTeleport pending;
        while ((pending = pendingTeleports.peek()) != null) {
            if (currentTick < pending.scheduledTick) {
                break;
            }
            pendingTeleports.poll();
            Player player = Bukkit.getPlayer(pending.playerUUID);
            if (player == null || !player.isOnline()) {
                continue;
            }
            CraftPlayer craftPlayer = (CraftPlayer) player;
            EntityPlayer nmsPlayer = craftPlayer.getHandle();
            if (nmsPlayer.isSleeping() || nmsPlayer.playerConnection.isDisconnected()) {
                continue;
            }
            nmsPlayer.playerConnection.teleport(pending.destination);
            nmsPlayer.fallDistance = 0.0F;
        }
    }

    public static void clear() {
        pendingTeleports.clear();
    }

    private static class PendingTeleport {
        final UUID playerUUID;
        final Location destination;
        final long scheduledTick;

        PendingTeleport(UUID playerUUID, Location destination, long scheduledTick) {
            this.playerUUID = playerUUID;
            this.destination = destination;
            this.scheduledTick = scheduledTick;
        }
    }
}
