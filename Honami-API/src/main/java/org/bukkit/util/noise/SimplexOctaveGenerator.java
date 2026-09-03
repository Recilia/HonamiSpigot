package org.bukkit.util.noise;

import java.util.Random;

import org.bukkit.World;

public class SimplexOctaveGenerator extends OctaveGenerator {
	private double wScale = 1;

	public SimplexOctaveGenerator(World world, int octaves) {
		this(new Random(world.getSeed()), octaves);
	}

	public SimplexOctaveGenerator(long seed, int octaves) {
		this(new Random(seed), octaves);
	}

	public SimplexOctaveGenerator(Random rand, int octaves) {
		super(createOctaves(rand, octaves));
	}

	@Override
	public void setScale(double scale) {
		super.setScale(scale);
		setWScale(scale);
	}

	public double getWScale() {
		return wScale;
	}

	public void setWScale(double scale) {
		wScale = scale;
	}

	public double noise(double x, double y, double z, double w, double frequency, double amplitude) {
		return noise(x, y, z, w, frequency, amplitude, false);
	}

	public double noise(double x, double y, double z, double w, double frequency, double amplitude,
			boolean normalized) {
		double result = 0;
		double amp = 1;
		double freq = 1;
		double max = 0;

		x *= xScale;
		y *= yScale;
		z *= zScale;
		w *= wScale;

		for (NoiseGenerator octave : octaves) {
			result += ((SimplexNoiseGenerator) octave).noise(x * freq, y * freq, z * freq, w * freq) * amp;
			max += amp;
			freq *= frequency;
			amp *= amplitude;
		}

		if (normalized) {
			result /= max;
		}

		return result;
	}

	private static NoiseGenerator[] createOctaves(Random rand, int octaves) {
		NoiseGenerator[] result = new NoiseGenerator[octaves];

		for (int i = 0; i < octaves; i++) {
			result[i] = new SimplexNoiseGenerator(rand);
		}

		return result;
	}
}
