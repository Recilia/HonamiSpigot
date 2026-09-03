package org.bukkit.craftbukkit.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.commons.lang.UnhandledException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitWorker;

class CraftAsyncTask extends CraftTask {

	private final LinkedList<BukkitWorker> workers = new LinkedList<BukkitWorker>();
	private final Map<Integer, CraftTask> runners;

	CraftAsyncTask(final Map<Integer, CraftTask> runners, final Plugin plugin, final Runnable task, final int id,
			final long delay) {
		super(plugin, task, id, delay);
		this.runners = runners;
	}

	@Override
	public boolean isSync() {
		return false;
	}

	@Override
	public void run() {
		final Thread thread = Thread.currentThread();
		synchronized (workers) {
			if (getPeriod() == -2) {

				return;
			}
			workers.add(new BukkitWorker() {
				@Override
				public Thread getThread() {
					return thread;
				}

				@Override
				public int getTaskId() {
					return CraftAsyncTask.this.getTaskId();
				}

				@Override
				public Plugin getOwner() {
					return CraftAsyncTask.this.getOwner();
				}
			});
		}
		Throwable thrown = null;
		try {
			super.run();
		} catch (final Throwable t) {
			thrown = t;
			throw new UnhandledException(String.format("Plugin %s generated an exception while executing task %s",
					getOwner().getDescription().getFullName(), getTaskId()), thrown);
		} finally {
			
			synchronized (workers) {
				try {
					final Iterator<BukkitWorker> workers = this.workers.iterator();
					boolean removed = false;
					while (workers.hasNext()) {
						if (workers.next().getThread() == thread) {
							workers.remove();
							removed = true; 
							break;
						}
					}
					if (!removed) {
						throw new IllegalStateException(String.format("Unable to remove worker %s on task %s for %s",
								thread.getName(), getTaskId(), getOwner().getDescription().getFullName()), thrown); 

																													

																													

																													
					}
				} finally {
					if (getPeriod() < 0 && workers.isEmpty()) {

						
						runners.remove(getTaskId());
					}
				}
			}
		}
	}

	LinkedList<BukkitWorker> getWorkers() {
		return workers;
	}

	@Override
	boolean cancel0() {
		synchronized (workers) {
			
			setPeriod(-2l);
			if (workers.isEmpty()) {
				runners.remove(getTaskId());
			}
		}
		return true;
	}
}
