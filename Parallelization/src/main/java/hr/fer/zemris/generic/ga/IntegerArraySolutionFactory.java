package hr.fer.zemris.generic.ga;

import hr.fer.zemris.optjava.rng.IRNG;

public class IntegerArraySolutionFactory implements ISolutionFactory<int[]>{
	
	private int arrayLength;
	
	public IntegerArraySolutionFactory(int arrayLength) {
		this.arrayLength = arrayLength;
	}
	
	public IntegerArrayGASolution getRandomSolution(IRNG rand) {
		int[] data = new int[arrayLength];
		for(int i = 0; i < arrayLength; ++i) {
			data[i] = rand.nextInt(0, 256);
		}
		return new IntegerArrayGASolution(data);
	}
}
