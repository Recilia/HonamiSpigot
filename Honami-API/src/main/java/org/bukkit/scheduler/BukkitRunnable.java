package org.bukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class BukkitRunnable implements Runnable {
	private int taskId = -1;

	public synchronized void cancel() throws IllegalStateException {
		Bukkit.getScheduler().cancelTask(getTaskId());
	}

	public synchronized BukkitTask runTask(Plugin plugin) throws IllegalArgumentException, IllegalStateException {
		checkState();
		return setupId(Bukkit.getScheduler().runTask(plugin, (Runnable) this));
	}

	public synchronized BukkitTask runTaskAsynchronously(Plugin plugin)
			throws IllegalArgumentException, IllegalStateException {
		checkState();
		return setupId(Bukkit.getScheduler().runTaskAsynchronously(plugin, (Runnable) this));
	}

	public synchronized BukkitTask runTaskLater(Plugin plugin, long delay)
			throws IllegalArgumentException, IllegalStateException {
		checkState();
		return setupId(Bukkit.getScheduler().runTaskLater(plugin, (Runnable) this, delay));
	}

	public synchronized BukkitTask runTaskLaterAsynchronously(Plugin plugin, long delay)
			throws IllegalArgumentException, IllegalStateException {
		checkState();
		return setupId(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, (Runnable) this, delay));
	}

	public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period)
			throws IllegalArgumentException, IllegalStateException {
		checkState();
		return setupId(Bukkit.getScheduler().runTaskTimer(plugin, (Runnable) this, delay, period));
	}

	public synchronized BukkitTask runTaskTimerAsynchronously(Plugin plugin, long delay, long period)
			throws IllegalArgumentException, IllegalStateException {
		checkState();
		return setupId(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, (Runnable) this, delay, period));
	}

	public synchronized int getTaskId() throws IllegalStateException {
		final int id = taskId;
		if (id == -1) {
			throw new IllegalStateException("Not scheduled yet");
		}
		return id;
	}

	private void checkState() {
		if (taskId != -1) {
			throw new IllegalStateException("Already scheduled as " + taskId);
		}
	}

	private BukkitTask setupId(final BukkitTask task) {
		this.taskId = task.getTaskId();
		return task;
	}
}
