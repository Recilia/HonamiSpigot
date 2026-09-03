package rein.honami.spigot.async;

import net.minecraft.server.MinecraftServer;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

public class AsyncUtil {

	public static void run(Runnable runnable) {
		ForkJoinPool.commonPool().execute(runnable);
	}

	public static void run(Runnable runnable, Executor executor) {
		executor.execute(runnable);
	}

	public static void runSyncNextTick(Runnable runnable) {
		MinecraftServer.getServer().processQueue.add(runnable);
	}

	public static void runPostTick(Runnable runnable) {
		MinecraftServer.getServer().priorityProcessQueue.add(runnable);
	}

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static void runSynchronized(Object monitor, Runnable runnable) {
		if (Thread.holdsLock(monitor) ) {
			runnable.run();
		} else {
			synchronized (monitor) {
				runnable.run();
			}
		}
	}

	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static <T> T runSynchronized(Object monitor, Supplier<T> supplier) {
		if (Thread.holdsLock(monitor) ) {
			return supplier.get();
		} else {
			synchronized (monitor) {
				return supplier.get();
			}
		}
	}
}
