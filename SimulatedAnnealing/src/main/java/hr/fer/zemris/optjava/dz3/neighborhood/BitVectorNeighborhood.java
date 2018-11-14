package hr.fer.zemris.optjava.dz3.neighborhood;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public class BitVectorNeighborhood implements INeighborhood<BitVectorSolution>{

	private int numOfVariables;
	
	public BitVectorNeighborhood(int numOfVariables) {
		this.numOfVariables = numOfVariables;
	}
	
	public BitVectorSolution randomNeighborhoog(BitVectorSolution x) {
		BitVectorSolution newSolution = x.duplicate();
		int bitsPerVariable = x.bits.length / numOfVariables;
		int offset = 0;
		for(int i = 0; i < numOfVariables; i++) {
			int randomIndex = (new Random()).nextInt(bitsPerVariable) + offset;
			newSolution.bits[randomIndex] = (byte) (newSolution.bits[randomIndex] == 0 ? 1 : 0);
			offset += bitsPerVariable;
		}
		return newSolution;
	}
}
