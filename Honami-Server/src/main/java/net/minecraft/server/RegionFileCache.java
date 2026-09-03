package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import rein.honami.spigot.config.HonamiConfig;

public class RegionFileCache {

	private static final LinkedHashMap<File, RegionFile> lruCache = new LinkedHashMap<>(16, 0.75f, true);
	public static final Map<File, RegionFile> a = lruCache; 

	public static synchronized RegionFile a(File file, int i, int j) {
		return a(file, i, j, true);
	}

	public static synchronized RegionFile a(File file, int i, int j, boolean create) {
		
		int maxSize = HonamiConfig.regionFileCacheSize;
		File file1 = new File(file, "region");
		File file2 = new File(file1, "r." + (i >> 5) + "." + (j >> 5) + ".mca");

		RegionFile regionfile = lruCache.get(file2);
		if (regionfile != null) {
			return regionfile;
		}

		if (!create && !file2.exists()) {
			return null;
		} 
		if (!file1.exists()) {
			file1.mkdirs();
		}

		if (lruCache.size() >= maxSize) {
			Iterator<Map.Entry<File, RegionFile>> iterator = lruCache.entrySet().iterator();
			while (iterator.hasNext() && lruCache.size() >= maxSize) {
				Map.Entry<File, RegionFile> entry = iterator.next();
				try {
					if (entry.getValue() != null) {
						entry.getValue().c();
					}
				} catch (IOException ioexception) {
					ioexception.printStackTrace();
				}
				iterator.remove();
			}
		}

		RegionFile regionfile1 = new RegionFile(file2);
		lruCache.put(file2, regionfile1);
		return regionfile1;
	}

	public static synchronized void a() {
		Iterator<Map.Entry<File, RegionFile>> iterator = lruCache.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<File, RegionFile> entry = iterator.next();
			RegionFile regionfile = entry.getValue();

			try {
				if (regionfile != null) {
					regionfile.c();
				}
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
		}

		lruCache.clear();
	}

	public static DataInputStream c(File file, int i, int j) {
		RegionFile regionfile = a(file, i, j);

		return regionfile.a(i & 31, j & 31);
	}

	public static DataOutputStream d(File file, int i, int j) {
		RegionFile regionfile = a(file, i, j);

		return regionfile.b(i & 31, j & 31);
	}
}
