
package rein.honami.spigot.random;

import java.util.Random;

import javax.annotation.concurrent.ThreadSafe;

@Deprecated
@ThreadSafe 
public strictfp class FastRandom extends Random implements Cloneable {
	
	private static final long serialVersionUID = 1L;

	protected long seed;

	public FastRandom() {
		this(System.nanoTime());
	}

	public FastRandom(long seed) {
		this.seed = seed;
	}

	public synchronized long getSeed() {
		return seed;
	}

	public synchronized void setSeed(long seed) {
		this.seed = seed;
		super.setSeed(seed);
	}

	public FastRandom clone() {
		return new FastRandom(getSeed());
	}

	@Override
	protected int next(int nbits) {
		long x = seed;
		x ^= (x << 21);
		x ^= (x >>> 35);
		x ^= (x << 4);
		seed = x;
		x &= ((1L << nbits) - 1);
		
		return (int) x;
	}

	synchronized public void setSeed(int[] array) {
		if (array.length == 0)
			throw new IllegalArgumentException("Array length must be greater than zero");
		setSeed(array.hashCode());
	}
}
