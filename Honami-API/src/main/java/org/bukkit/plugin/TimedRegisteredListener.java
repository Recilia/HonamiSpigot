package org.bukkit.plugin;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class TimedRegisteredListener extends RegisteredListener {
	private int count;
	private long totalTime;
	private Class<? extends Event> eventClass;
	private boolean multiple = false;

	public TimedRegisteredListener(final Listener pluginListener, final EventExecutor eventExecutor,
			final EventPriority eventPriority, final Plugin registeredPlugin, final boolean listenCancelled) {
		super(pluginListener, eventExecutor, eventPriority, registeredPlugin, listenCancelled);
	}

	@Override
	public void callEvent(Event event) throws EventException {
		if (event.isAsynchronous()) {
			super.callEvent(event);
			return;
		}
		count++;
		Class<? extends Event> newEventClass = event.getClass();
		if (this.eventClass == null) {
			this.eventClass = newEventClass;
		} else if (!this.eventClass.equals(newEventClass)) {
			multiple = true;
			this.eventClass = getCommonSuperclass(newEventClass, this.eventClass).asSubclass(Event.class);
		}
		long start = System.nanoTime();
		super.callEvent(event);
		totalTime += System.nanoTime() - start;
	}

	private static Class<?> getCommonSuperclass(Class<?> class1, Class<?> class2) {
		while (!class1.isAssignableFrom(class2)) {
			class1 = class1.getSuperclass();
		}
		return class1;
	}

	public void reset() {
		count = 0;
		totalTime = 0;
	}

	public int getCount() {
		return count;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public Class<? extends Event> getEventClass() {
		return eventClass;
	}

	public boolean hasMultiple() {
		return multiple;
	}
}
