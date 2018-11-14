package hr.fer.zemris.optjava.dz3.neighborhood;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

public class DoubleArrayUnifNeighborhood implements INeighborhood<DoubleArraySolution>{

	private double[] deltas;
	Random rand;
	
	public DoubleArrayUnifNeighborhood(double[] deltas) {
		this.deltas = deltas;
		rand = new Random();
	}

	public DoubleArraySolution randomNeighborhoog(DoubleArraySolution x) {
		DoubleArraySolution newSolution = x.duplicate();
		for(int i = 0, n = newSolution.values.length; i < n; i++) {
			//x'[i] = x[i] + unifRand(-deltas[i], deltas[i])
			newSolution.values[i] += rand.nextDouble() * (deltas[i] + deltas[i]) - deltas[i];
		}
		return newSolution;
	}

}
