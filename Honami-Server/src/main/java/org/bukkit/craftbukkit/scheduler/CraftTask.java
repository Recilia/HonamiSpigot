package org.bukkit.craftbukkit.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import co.aikar.timings.SpigotTimings; 
import co.aikar.timings.Timing; 

public class CraftTask implements BukkitTask, Runnable { 

	private volatile CraftTask next = null;

	private volatile long period;
	private long nextRun;
	public final Runnable task; 
	public Timing timings; 
	private final Plugin plugin;
	private final int id;

	CraftTask() {
		this(null, null, -1, -1);
	}

	CraftTask(final Runnable task) {
		this(null, task, -1, -1);
	}

	CraftTask(final Plugin plugin, final Runnable task, final int id, final long period) {
		this.plugin = plugin;
		this.task = task;
		this.id = id;
		this.period = period;
		timings = task != null ? SpigotTimings.getPluginTaskTimings(this, period) : null; 
	}

	@Override
	public final int getTaskId() {
		return id;
	}

	@Override
	public final Plugin getOwner() {
		return plugin;
	}

	@Override
	public boolean isSync() {
		return true;
	}

	@Override
	public void run() {
		if (timings != null && isSync()) {
			timings.startTiming(); 
		}
		task.run();
		if (timings != null && isSync()) {
			timings.stopTiming(); 
		}
	}

	long getPeriod() {
		return period;
	}

	void setPeriod(long period) {
		this.period = period;
	}

	long getNextRun() {
		return nextRun;
	}

	void setNextRun(long nextRun) {
		this.nextRun = nextRun;
	}

	CraftTask getNext() {
		return next;
	}

	void setNext(CraftTask next) {
		this.next = next;
	}

	Class<? extends Runnable> getTaskClass() {
		return task.getClass();
	}

	@Override
	public void cancel() {
		Bukkit.getScheduler().cancelTask(id);
	}

	boolean cancel0() {
		setPeriod(-2l);
		return true;
	}

}
