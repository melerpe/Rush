package imported.world.gen;

import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator {

	private NoiseGeneratorPerlin[] perlinGenerators;
	private int tries;

	public NoiseGeneratorOctaves(Random random, int tries) {
		this.tries = tries;
		perlinGenerators = new NoiseGeneratorPerlin[tries];

		for (int j = 0; j < tries; ++j)
			perlinGenerators[j] = new NoiseGeneratorPerlin(random);
	}

	public double a(double d0, double d1) {
		double d2 = 0.0D;
		double d3 = 1.0D;

		for (int i = 0; i < tries; ++i) {
			d2 += perlinGenerators[i].a(d0 * d3, d1 * d3) / d3;
			d3 /= 2.0D;
		}

		return d2;
	}

	public double[] generateNoise(double[] cache, double d0, double d1, double d2, int i, int j, int k, double d3, double d4, double d5) {
		if (cache == null)
			cache = new double[i * j * k];
		else
			for (int l = 0; l < cache.length; ++l)
				cache[l] = 0.0D;

		double d6 = 1.0D;

		for (int i1 = 0; i1 < tries; ++i1) {
			perlinGenerators[i1].generateNoise(cache, d0, d1, d2, i, j, k, d3 * d6, d4 * d6, d5 * d6, d6);
			d6 /= 2.0D;
		}

		return cache;
	}
}
