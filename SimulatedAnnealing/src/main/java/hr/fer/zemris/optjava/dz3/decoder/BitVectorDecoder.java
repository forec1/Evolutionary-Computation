package hr.fer.zemris.optjava.dz3.decoder;

import java.util.Arrays;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {

	protected double[] mins;
	protected double[] maxs;
	protected int[] bits;
	protected int n;
	protected int totalBits;
	
	
	
	public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		this.mins = mins;
		this.maxs = maxs;
		this.bits = bits;
		this.n = n;
		this.totalBits = 0;
		for(int bit : bits) {
			this.totalBits += bit;
		}
		
	}
	
	public BitVectorDecoder(double min, double max, int bit, int n) {
		this.mins = new double[n];
		this.maxs = new double[n];
		this.bits = new int[n];
		this.n = n;
		Arrays.fill(maxs, max);
		Arrays.fill(mins, min);
		Arrays.fill(bits, bit);
		totalBits = bit * n;
	}
	
	public int getTootalBits() {
		return totalBits;
	}
	
	public int getDimensions() {
		return n;
	}
	
	public abstract double[] decode(BitVectorSolution x);
	public abstract void decode(BitVectorSolution x, double[] d);
}
