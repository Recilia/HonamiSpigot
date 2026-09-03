package org.bukkit.craftbukkit.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import org.apache.commons.lang.Validate;

public final class AsynchronousExecutor<P, T, C, E extends Throwable> {

	public static interface CallBackProvider<P, T, C, E extends Throwable> extends ThreadFactory {

		T callStage1(P parameter) throws E;

		void callStage2(P parameter, T object) throws E;

		void callStage3(P parameter, T object, C callback) throws E;
	}

	@SuppressWarnings("rawtypes")
	static final AtomicIntegerFieldUpdater STATE_FIELD = AtomicIntegerFieldUpdater
			.newUpdater(AsynchronousExecutor.Task.class, "state");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static boolean set(AsynchronousExecutor.Task $this, int expected, int value) {
		return STATE_FIELD.compareAndSet($this, expected, value);
	}

	class Task implements Runnable {
		static final int PENDING = 0x0;
		static final int STAGE_1_ASYNC = PENDING + 1;
		static final int STAGE_1_SYNC = STAGE_1_ASYNC + 1;
		static final int STAGE_1_COMPLETE = STAGE_1_SYNC + 1;
		static final int FINISHED = STAGE_1_COMPLETE + 1;

		volatile int state = PENDING;
		final P parameter;
		T object;
		final List<C> callbacks = new LinkedList<C>();
		E t = null;

		Task(final P parameter) {
			this.parameter = parameter;
		}

		@Override
		public void run() {
			if (initAsync()) {
				finished.add(this);
			}
		}

		boolean initAsync() {
			if (set(this, PENDING, STAGE_1_ASYNC)) {
				boolean ret = true;

				try {
					init();
				} finally {
					if (set(this, STAGE_1_ASYNC, STAGE_1_COMPLETE)) {
						
					} else {
						
						synchronized (this) {
							if (state != STAGE_1_SYNC) {
								
								this.notifyAll();
							} else {
								
							}
							state = STAGE_1_COMPLETE; 
						}

						ret = false; 
					}
				}

				return ret;
			} else {
				return false;
			}
		}

		void initSync() {
			if (set(this, PENDING, STAGE_1_COMPLETE)) {
				
				init();
			} else if (set(this, STAGE_1_ASYNC, STAGE_1_SYNC)) {

				synchronized (this) {
					if (set(this, STAGE_1_SYNC, PENDING)) { 
															
						while (state != STAGE_1_COMPLETE) {
							try {
								this.wait();
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								throw new RuntimeException("Unable to handle interruption on " + parameter, e);
							}
						}
					} else {
						
					}
				}
			} else {
				
			}
		}

		@SuppressWarnings("unchecked")
		void init() {
			try {
				object = provider.callStage1(parameter);
			} catch (final Throwable t) {
				this.t = (E) t;
			}
		}

		@SuppressWarnings("unchecked")
		T get() throws E {
			initSync();
			if (callbacks.isEmpty()) {

				
				callbacks.add((C) this);
			}
			finish();
			return object;
		}

		void finish() throws E {
			switch (state) {
			default:
			case PENDING:
			case STAGE_1_ASYNC:
			case STAGE_1_SYNC:
				throw new IllegalStateException(
						"Attempting to finish unprepared(" + state + ") task(" + parameter + ")");
			case STAGE_1_COMPLETE:
				try {
					if (t != null) {
						throw t;
					}
					if (callbacks.isEmpty()) {
						return;
					}

					final CallBackProvider<P, T, C, E> provider = AsynchronousExecutor.this.provider;
					final P parameter = this.parameter;
					final T object = this.object;

					provider.callStage2(parameter, object);
					for (C callback : callbacks) {
						if (callback == this) {

							continue;
						}
						provider.callStage3(parameter, object, callback);
					}
				} finally {
					tasks.remove(parameter);
					state = FINISHED;
				}
			case FINISHED:
			}
		}

		boolean drop() {
			if (set(this, PENDING, FINISHED)) {
				
				tasks.remove(parameter);
				return true;
			} else {
				
				return false;
			}
		}
	}

	final CallBackProvider<P, T, C, E> provider;
	final Queue<Task> finished = new ConcurrentLinkedQueue<Task>();
	final Map<P, Task> tasks = new HashMap<P, Task>();
	final ThreadPoolExecutor pool;

	public AsynchronousExecutor(final CallBackProvider<P, T, C, E> provider, final int coreSize) {
		Validate.notNull(provider, "Provider cannot be null");
		this.provider = provider;

		pool = new ThreadPoolExecutor(coreSize, Integer.MAX_VALUE, 60l, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(), provider);
	}

	public void add(P parameter, C callback) {
		Task task = tasks.get(parameter);
		if (task == null) {
			tasks.put(parameter, task = new Task(parameter));
			pool.execute(task);
		}
		task.callbacks.add(callback);
	}

	public boolean drop(P parameter, C callback) throws IllegalStateException {
		final Task task = tasks.get(parameter);
		if (task == null) {
			return true;
		}
		if (!task.callbacks.remove(callback)) {
			throw new IllegalStateException("Unknown " + callback + " for " + parameter);
		}
		if (task.callbacks.isEmpty()) {
			return task.drop();
		}
		return false;
	}

	public T get(P parameter) throws E, IllegalStateException {
		final Task task = tasks.get(parameter);
		if (task == null) {
			throw new IllegalStateException("Unknown " + parameter);
		}
		return task.get();
	}

	public T getSkipQueue(P parameter) throws E {
		return skipQueue(parameter);
	}

	public T getSkipQueue(P parameter, C callback) throws E {
		final T object = skipQueue(parameter);
		provider.callStage3(parameter, object, callback);
		return object;
	}

	public T getSkipQueue(P parameter, C... callbacks) throws E {
		final CallBackProvider<P, T, C, E> provider = this.provider;
		final T object = skipQueue(parameter);
		for (C callback : callbacks) {
			provider.callStage3(parameter, object, callback);
		}
		return object;
	}

	public T getSkipQueue(P parameter, Iterable<C> callbacks) throws E {
		final CallBackProvider<P, T, C, E> provider = this.provider;
		final T object = skipQueue(parameter);
		for (C callback : callbacks) {
			provider.callStage3(parameter, object, callback);
		}
		return object;
	}

	private T skipQueue(P parameter) throws E {
		Task task = tasks.get(parameter);
		if (task != null) {
			return task.get();
		}
		T object = provider.callStage1(parameter);
		provider.callStage2(parameter, object);
		return object;
	}

	public void finishActive() throws E {
		final Queue<Task> finished = this.finished;
		while (!finished.isEmpty()) {
			finished.poll().finish();
		}
	}

	public void setActiveThreads(final int coreSize) {
		pool.setCorePoolSize(coreSize);
	}
}
