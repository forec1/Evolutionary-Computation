package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;

public class PassThroughDecoder implements IDecoder<DoubleArraySolution>{

	public double[] decode(DoubleArraySolution x) {
		return x.values;
	}

	public void decode(DoubleArraySolution x, double[] d) {
		for(int i = 0, n = x.values.length; i < n; i++) {
			d[i] = x.values[i];
		}
		
	}

}
