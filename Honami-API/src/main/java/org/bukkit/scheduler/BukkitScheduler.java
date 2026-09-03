package org.bukkit.scheduler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.bukkit.plugin.Plugin;

public interface BukkitScheduler {

	public int scheduleSyncDelayedTask(Plugin plugin, Runnable task, long delay);

	@Deprecated
	public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task, long delay);

	public int scheduleSyncDelayedTask(Plugin plugin, Runnable task);

	@Deprecated
	public int scheduleSyncDelayedTask(Plugin plugin, BukkitRunnable task);

	public int scheduleSyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

	@Deprecated
	public int scheduleSyncRepeatingTask(Plugin plugin, BukkitRunnable task, long delay, long period);

	@Deprecated
	public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task, long delay);

	@Deprecated
	public int scheduleAsyncDelayedTask(Plugin plugin, Runnable task);

	@Deprecated
	public int scheduleAsyncRepeatingTask(Plugin plugin, Runnable task, long delay, long period);

	public <T> Future<T> callSyncMethod(Plugin plugin, Callable<T> task);

	public void cancelTask(int taskId);

	public void cancelTasks(Plugin plugin);

	public void cancelAllTasks();

	public boolean isCurrentlyRunning(int taskId);

	public boolean isQueued(int taskId);

	public List<BukkitWorker> getActiveWorkers();

	public List<BukkitTask> getPendingTasks();

	public BukkitTask runTask(Plugin plugin, Runnable task) throws IllegalArgumentException;

	@Deprecated
	public BukkitTask runTask(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException;

	public BukkitTask runTaskAsynchronously(Plugin plugin, Runnable task) throws IllegalArgumentException;

	@Deprecated
	public BukkitTask runTaskAsynchronously(Plugin plugin, BukkitRunnable task) throws IllegalArgumentException;

	public BukkitTask runTaskLater(Plugin plugin, Runnable task, long delay) throws IllegalArgumentException;

	@Deprecated
	public BukkitTask runTaskLater(Plugin plugin, BukkitRunnable task, long delay) throws IllegalArgumentException;

	public BukkitTask runTaskLaterAsynchronously(Plugin plugin, Runnable task, long delay)
			throws IllegalArgumentException;

	@Deprecated
	public BukkitTask runTaskLaterAsynchronously(Plugin plugin, BukkitRunnable task, long delay)
			throws IllegalArgumentException;

	public BukkitTask runTaskTimer(Plugin plugin, Runnable task, long delay, long period)
			throws IllegalArgumentException;

	@Deprecated
	public BukkitTask runTaskTimer(Plugin plugin, BukkitRunnable task, long delay, long period)
			throws IllegalArgumentException;

	public BukkitTask runTaskTimerAsynchronously(Plugin plugin, Runnable task, long delay, long period)
			throws IllegalArgumentException;

	@Deprecated
	public BukkitTask runTaskTimerAsynchronously(Plugin plugin, BukkitRunnable task, long delay, long period)
			throws IllegalArgumentException;
}
