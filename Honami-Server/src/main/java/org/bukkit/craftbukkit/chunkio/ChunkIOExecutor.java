package org.bukkit.craftbukkit.chunkio;

import org.bukkit.craftbukkit.util.AsynchronousExecutor;

import rein.honami.spigot.config.HonamiConfig;

import net.minecraft.server.Chunk;
import net.minecraft.server.ChunkProviderServer;
import net.minecraft.server.ChunkRegionLoader;
import net.minecraft.server.World;

public class ChunkIOExecutor {
	static final int BASE_THREADS = HonamiConfig.chunkThreads;
	static final int PLAYERS_PER_THREAD = HonamiConfig.playersPerThread;

	private static final AsynchronousExecutor<QueuedChunk, Chunk, Runnable, RuntimeException> instance = new AsynchronousExecutor<QueuedChunk, Chunk, Runnable, RuntimeException>(
			new ChunkIOProvider(), BASE_THREADS);

	public static Chunk syncChunkLoad(World world, ChunkRegionLoader loader, ChunkProviderServer provider, int x,
			int z) {
		return instance.getSkipQueue(new QueuedChunk(x, z, loader, world, provider));
	}

	public static void queueChunkLoad(World world, ChunkRegionLoader loader, ChunkProviderServer provider, int x, int z,
			Runnable runnable) {
		instance.add(new QueuedChunk(x, z, loader, world, provider), runnable);
	}

	
	public static void dropQueuedChunkLoad(World world, int x, int z, Runnable runnable) {
		instance.drop(new QueuedChunk(x, z, null, world, null), runnable);
	}

	public static void adjustPoolSize(int players) {
		int size = Math.max(BASE_THREADS, (int) Math.ceil(players / PLAYERS_PER_THREAD));
		instance.setActiveThreads(size);
	}

	public static void tick() {
		instance.finishActive();
	}
}
