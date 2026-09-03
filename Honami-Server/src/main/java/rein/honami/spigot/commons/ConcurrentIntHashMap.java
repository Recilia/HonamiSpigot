package rein.honami.spigot.commons;

import static rein.honami.spigot.async.AsyncUtil.runSynchronized;

import net.minecraft.server.IntHashMap;

public class ConcurrentIntHashMap<V> extends IntHashMap<V> {
	
	public V get(int var1) {
		return runSynchronized(this, () -> super.get(var1));
	}

	public boolean b(int var1) {
		return runSynchronized(this, () -> super.b(var1));
	}

	public void a(int var1, V var2) {
		runSynchronized(this, () -> super.a(var1, var2));
	}

	public V d(int var1) {
		return runSynchronized(this, () -> super.d(var1));
	}

	public void c() {
		runSynchronized(this, super::c);
	}
}
