package rein.honami.spigot.async;

import javafixes.concurrency.ReusableCountLatch;

public class ResettableLatch extends ReusableCountLatch {

	private int initValue;

	public ResettableLatch() {
		this(0);
	}

	public ResettableLatch(int initialCount) {
		super(initialCount);
		this.initValue = initialCount;
	}

	public void reset() {
		reset(initValue);
	}

	public void reset(int count) {
		
		if (getCount() > count) {

			while (getCount() > count) {
				
				decrement();
			}

		} else if (getCount() < count) {

			while (getCount() < count) {
				
				increment();
			}

		}
		
	}

}
