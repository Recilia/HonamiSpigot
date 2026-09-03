package rein.honami.spigot.tnt;

import java.util.concurrent.ConcurrentHashMap;

public class TNTVelocityCache {

	private static final TNTVelocityCache INSTANCE = new TNTVelocityCache();
	private final ConcurrentHashMap<Long, double[]> cache = new ConcurrentHashMap<>();

	private TNTVelocityCache() {
	}

	public static TNTVelocityCache getInstance() {
		return INSTANCE;
	}

	public double[] getCachedVelocity(int x, int y, int z, String worldName) {
		long key = hashPosition(x, y, z, worldName);
		return cache.get(key);
	}

	public void cacheVelocity(int x, int y, int z, String worldName, double motionX, double motionY, double motionZ) {
		long key = hashPosition(x, y, z, worldName);
		cache.put(key, new double[]{motionX, motionY, motionZ});
	}

	public void clearCache() {
		cache.clear();
	}

	private long hashPosition(int x, int y, int z, String worldName) {
		long result = 31L + x;
		result = 31L * result + z;
		result = 31L * result + (long) (y ^ (y >>> 32));
		result = 31L * result + worldName.hashCode();
		return result;
	}
}
