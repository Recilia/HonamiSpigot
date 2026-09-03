package rein.honami.spigot.async;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import net.minecraft.server.PacketPlayOutKeepAlive;

public class AsyncKeepAlive {

	private static final Map<UUID, Long> pendingKeepAlives = new ConcurrentHashMap<>();
	private static ScheduledExecutorService executor;
	private static boolean enabled = false;
	private static int intervalTicks = 100;
	private static long timeoutMs = 30000;

	public static void init(boolean enable, int interval) {
		enabled = enable;
		intervalTicks = interval;

		if (enabled) {
			if (executor == null || executor.isShutdown()) {
				executor = Executors.newSingleThreadScheduledExecutor(r -> {
					Thread t = new Thread(r, "Honami Async KeepAlive");
					t.setDaemon(true);
					return t;
				});
			}
			startTask();
		} else {
			shutdown();
		}
	}

	private static void startTask() {
		if (executor == null || executor.isShutdown()) return;

		long intervalMs = intervalTicks * 50L;
		executor.scheduleAtFixedRate(() -> {
			try {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.isDead() || !player.isOnline()) continue;

					UUID uuid = player.getUniqueId();
					Long lastSent = pendingKeepAlives.get(uuid);

					if (lastSent != null && System.currentTimeMillis() - lastSent > timeoutMs) {
						Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugins()[0], () -> {
							if (player.isOnline()) {
								player.kickPlayer("Timed out");
							}
						});
						pendingKeepAlives.remove(uuid);
						continue;
					}

					int keepAliveId = (int) (System.currentTimeMillis() & Integer.MAX_VALUE);
					pendingKeepAlives.put(uuid, System.currentTimeMillis());

					CraftPlayer craftPlayer = (CraftPlayer) player;
					if (craftPlayer.getHandle() != null && craftPlayer.getHandle().playerConnection != null) {
						PacketPlayOutKeepAlive packet = new PacketPlayOutKeepAlive(keepAliveId);
						craftPlayer.getHandle().playerConnection.sendPacket(packet);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, intervalMs, intervalMs, TimeUnit.MILLISECONDS);
	}

	public static void handleResponse(Player player) {
		if (!enabled) return;
		pendingKeepAlives.remove(player.getUniqueId());
	}

	public static void playerJoin(Player player) {
		pendingKeepAlives.remove(player.getUniqueId());
	}

	public static void playerQuit(Player player) {
		pendingKeepAlives.remove(player.getUniqueId());
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void shutdown() {
		if (executor != null && !executor.isShutdown()) {
			executor.shutdownNow();
		}
		pendingKeepAlives.clear();
	}
}
