package hr.fer.zemris.optjava.dz3.algorithm;

import hr.fer.zemris.optjava.dz3.decoder.IDecoder;
import hr.fer.zemris.optjava.dz3.function.IFunction;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solution.SingleObjectiveSolution;

public class GreedyAlgorithm<T extends SingleObjectiveSolution> implements IOptAlgorithm<SingleObjectiveSolution>{
	
	private IDecoder<T> decoder;
	private INeighborhood<T> neighborhood;
	private T startWith;
	private IFunction function;
	private boolean minimize;
	
	public GreedyAlgorithm(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith, IFunction function,
			boolean minimize) {
		this.decoder = decoder;
		this.neighborhood = neighborhood;
		this.startWith = startWith;
		this.function = function;
		this.minimize = minimize;
	}

	@SuppressWarnings("unchecked")
	public T run() {
		T x = (T) startWith.duplicate();
		double val = function.valueAt(decoder.decode(x));
		x.fitness = minimize ? -val : val;
		
		int paramIter = 10000;
		while(paramIter > 0) {
			T xNeighbor = neighborhood.randomNeighborhoog(x);
			double neighborVal = function.valueAt(decoder.decode(xNeighbor));
			xNeighbor.fitness = minimize ? -neighborVal : neighborVal;
			if(xNeighbor.fitness > x.fitness) {
				x = xNeighbor;
			}
			paramIter--;
		}
		return x;
	}

}
