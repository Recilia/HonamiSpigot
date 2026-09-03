/*
 * Copyright (c) 2015. Starlis LLC / dba Empire Minecraft
 *
 * This source code is proprietary software and must not be redistributed without Starlis LLC's approval
 *
 */

package co.aikar.util;

import com.google.common.base.Function;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class LoadingIntMap<V> extends Int2ObjectOpenHashMap<V> {
	private final Function<Integer, V> loader;

	public LoadingIntMap(Function<Integer, V> loader) {
		super(200);
		this.loader = loader;
	}

	@Override
	public V get(int key) {
		V res = super.get(key);
		if (res == null) {
			res = loader.apply(key);
			if (res != null) {
				put(key, res);
			}
		}
		return res;
	}

	public abstract static class Feeder<T> implements Function<T, T> {
		@Override
		public T apply(Object input) {
			return apply();
		}

		public abstract T apply();
	}
}
