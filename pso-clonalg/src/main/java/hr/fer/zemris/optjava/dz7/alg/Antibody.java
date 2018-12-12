package hr.fer.zemris.optjava.dz7.alg;

import java.util.Arrays;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.function.IErrorFunction;

public class Antibody {
	
	private double[] solution;
	private double affinity;
	
	public Antibody(double[] solution) {
		this.solution = solution;
	}
	
	public Antibody(double[] solution, double affinity) {
		this.solution = solution;
		this.affinity = affinity;
	}
	
	public void affinity(IErrorFunction function) {
		affinity = -function.value(solution);
	}
	
	public double getAffinity() {
		return affinity;
	}
	
	public Antibody duplicate() {
		return new Antibody(Arrays.copyOf(solution, solution.length), affinity);
	}
	
	public void hypermutate(Random rand) {
		for(int i = 0; i < solution.length; i++) {
			solution[i] += rand.nextDouble() * 0.02 - 0.01;
		}
	}
	
	public double[] getSolution() {
		return solution;
	}
}
