package org.bukkit.util.noise;

public abstract class OctaveGenerator {
	protected final NoiseGenerator[] octaves;
	protected double xScale = 1;
	protected double yScale = 1;
	protected double zScale = 1;

	protected OctaveGenerator(NoiseGenerator[] octaves) {
		this.octaves = octaves;
	}

	public void setScale(double scale) {
		setXScale(scale);
		setYScale(scale);
		setZScale(scale);
	}

	public double getXScale() {
		return xScale;
	}

	public void setXScale(double scale) {
		xScale = scale;
	}

	public double getYScale() {
		return yScale;
	}

	public void setYScale(double scale) {
		yScale = scale;
	}

	public double getZScale() {
		return zScale;
	}

	public void setZScale(double scale) {
		zScale = scale;
	}

	public NoiseGenerator[] getOctaves() {
		return octaves.clone();
	}

	public double noise(double x, double frequency, double amplitude) {
		return noise(x, 0, 0, frequency, amplitude);
	}

	public double noise(double x, double frequency, double amplitude, boolean normalized) {
		return noise(x, 0, 0, frequency, amplitude, normalized);
	}

	public double noise(double x, double y, double frequency, double amplitude) {
		return noise(x, y, 0, frequency, amplitude);
	}

	public double noise(double x, double y, double frequency, double amplitude, boolean normalized) {
		return noise(x, y, 0, frequency, amplitude, normalized);
	}

	public double noise(double x, double y, double z, double frequency, double amplitude) {
		return noise(x, y, z, frequency, amplitude, false);
	}

	public double noise(double x, double y, double z, double frequency, double amplitude, boolean normalized) {
		double result = 0;
		double amp = 1;
		double freq = 1;
		double max = 0;

		x *= xScale;
		y *= yScale;
		z *= zScale;

		for (NoiseGenerator octave : octaves) {
			result += octave.noise(x * freq, y * freq, z * freq) * amp;
			max += amp;
			freq *= frequency;
			amp *= amplitude;
		}

		if (normalized) {
			result /= max;
		}

		return result;
	}
}
