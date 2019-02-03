package hr.fer.zemris.optjava.rng.rngimpl;

import java.util.Random;

import hr.fer.zemris.optjava.rng.IRNG;

public class RNGRandomImpl implements IRNG{

	private Random rand;
	
	public RNGRandomImpl() {
		rand = new Random();
	}
	
	public double nextDouble() {
		return rand.nextDouble();
	}

	public double nextDouble(double min, double max) {
		return rand.nextDouble() * (max - min) + min;
	}

	public float nextFloat() {
		return rand.nextFloat();
	}

	public float nextFloat(float min, float max) {
		return rand.nextFloat() * (max - min) + min;
	}

	public int nextInt() {
		return rand.nextInt();
	}

	public int nextInt(int min, int max) {
		return rand.nextInt(max - min) + min;  
	}

	public boolean nextBoolean() {
		return rand.nextBoolean();
	}

	public double nextGaussian() {
		return rand.nextGaussian();
	}

}
