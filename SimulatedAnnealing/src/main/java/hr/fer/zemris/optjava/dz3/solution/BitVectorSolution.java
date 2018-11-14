package hr.fer.zemris.optjava.dz3.solution;

import java.util.Random;

public class BitVectorSolution extends SingleObjectiveSolution {
	
	public byte[] bits;
	
	public BitVectorSolution(int n) {
		bits = new byte[n];
	}
	
	public BitVectorSolution newLikeThis() {
		return new BitVectorSolution(bits.length);
	}
	
	public BitVectorSolution duplicate() {
		BitVectorSolution newSolution = new BitVectorSolution(bits.length);
		for(int i = 0, n = this.bits.length; i < n; i++) {
			newSolution.bits[i] = this.bits[i];
		}
		return newSolution;
	}
	
	public void randomize(Random rand) {
		for (int i = 0, n = bits.length; i < n; i++) {
			bits[i] = (byte) (rand.nextBoolean() ? 1 : 0);
		}
	}
}
