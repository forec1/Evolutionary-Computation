package hr.fer.zemris.optjava.dz3.solution;

import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution{
	
	public double[] values;
	
	public DoubleArraySolution(int n) {
		values = new double[n];
	}
	
	public DoubleArraySolution newLikeThis() {
		return new DoubleArraySolution(values.length);
	}
	
	public DoubleArraySolution duplicate() {
		DoubleArraySolution newSoultion = new DoubleArraySolution(this.values.length);
		for(int i = 0, n = this.values.length; i < n; i++) {
			newSoultion.values[i] = this.values[i];
		}
		return newSoultion;
	}
	
	public DoubleArraySolution randomize(Random rand, double[] min, double[] max) {
		for(int i = 0, n = values.length; i < n; i++) {
			values[i] = rand.nextDouble() * (max[i] - min[i]) + min[i];
		}
		return this;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0, n = values.length; i < n; i++) {
			s += "x" + (i+1) + "[" + values[i] + "] ";
		}
		return s;
	}

}
