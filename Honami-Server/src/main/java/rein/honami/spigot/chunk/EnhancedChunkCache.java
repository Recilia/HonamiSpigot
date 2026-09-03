package rein.honami.spigot.chunk;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.server.Chunk;
import rein.honami.spigot.config.HonamiConfig;

public class EnhancedChunkCache {

    private static final ConcurrentHashMap<Long, SoftReference<Chunk>> cache = new ConcurrentHashMap<>();

    public static Chunk getCachedChunk(long chunkKey) {
        if (!HonamiConfig.enhancedChunkCacheEnabled) {
            return null;
        }

        SoftReference<Chunk> ref = cache.get(chunkKey);
        if (ref != null) {
            Chunk chunk = ref.get();
            if (chunk != null) {
                return chunk;
            }
            cache.remove(chunkKey);
        }
        return null;
    }

    public static void cacheChunk(long chunkKey, Chunk chunk) {
        if (!HonamiConfig.enhancedChunkCacheEnabled) {
            return;
        }

        cache.put(chunkKey, new SoftReference<>(chunk));
    }

    public static long getChunkKey(int x, int z) {
        return ((long) x & 0xFFFFFFFFL) | (((long) z & 0xFFFFFFFFL) << 32);
    }

    public static void clearCache() {
        cache.clear();
    }
}
