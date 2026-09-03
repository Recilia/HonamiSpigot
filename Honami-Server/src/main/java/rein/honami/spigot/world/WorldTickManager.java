package rein.honami.spigot.world;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.aikar.timings.SpigotTimings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;
import rein.honami.spigot.config.HonamiConfig;

public class WorldTickManager {

	private static WorldTickManager worldTickerManagerInstance;

	private final ExecutorService worldExecutor = Executors.newFixedThreadPool(
		Runtime.getRuntime().availableProcessors(),
		r -> { Thread t = new Thread(r, "Honami World Thread"); t.setDaemon(true); return t; }
	);

	public WorldTickManager() {
		worldTickerManagerInstance = this;
	}

	public void tick() {
		tickWorlds();
	}

	private void tickWorlds() {
		SpigotTimings.bukkitSchedulerTimer.startTiming();
		MinecraftServer.getServer().server.getScheduler().mainThreadHeartbeat(MinecraftServer.getServer().at());
		SpigotTimings.bukkitSchedulerTimer.stopTiming();

		if (HonamiConfig.parallelWorldTicking) {
			tickWorldsParallel();
		} else {
			tickWorldsSequential();
		}
	}

	private void tickWorldsSequential() {
		for (int i = 0; i < MinecraftServer.getServer().worlds.size(); i++) {
			WorldServer world = MinecraftServer.getServer().worlds.get(i);
			if (world.ticker == null) {
				world.ticker = new WorldTicker(world);
			}
			world.ticker.run();
		}
	}

	private void tickWorldsParallel() {
		List<WorldServer> overworlds = new ArrayList<>();
		List<WorldServer> netherEnds = new ArrayList<>();

		for (int i = 0; i < MinecraftServer.getServer().worlds.size(); i++) {
			WorldServer world = MinecraftServer.getServer().worlds.get(i);
			if (world.dimension == -1 || world.dimension == 1) {
				netherEnds.add(world);
			} else {
				overworlds.add(world);
			}
		}

		List<CompletableFuture<Void>> futures = new ArrayList<>();
		for (WorldServer world : overworlds) {
			if (world.ticker == null) {
				world.ticker = new WorldTicker(world);
			}
			final WorldTicker ticker = world.ticker;
			futures.add(CompletableFuture.runAsync(ticker::run, worldExecutor));
		}

		CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

		for (WorldServer world : netherEnds) {
			if (world.ticker == null) {
				world.ticker = new WorldTicker(world);
			}
			world.ticker.run();
		}
	}

	public static WorldTickManager getInstance() {
		return worldTickerManagerInstance;
	}
}
