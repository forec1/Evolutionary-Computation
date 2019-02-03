package hr.fer.zemris.generic.ga;

import hr.fer.zemris.optjava.rng.IRNG;

public interface ISolutionFactory<T> {
	public GASolution<T> getRandomSolution(IRNG rand);
}
