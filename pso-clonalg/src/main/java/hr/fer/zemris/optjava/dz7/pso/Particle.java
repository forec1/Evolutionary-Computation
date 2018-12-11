package hr.fer.zemris.optjava.dz7.pso;

import java.util.Random;

import hr.fer.zemris.optjava.dz7.function.IErrorFunction;

public class Particle {

	private double x[];
	private double v[];
	private double pbest;
	private double lbest;
	private double C1;
	private double C2;
	private Random rand;
	private double fitness;
	
	public Particle(double x[], double[] v, double C1, double C2) {
		this.x = x;
		this.v = v;
		this.rand = new Random();
		this.C1 = C1;
		this.C2 = C2;
		this.pbest = -Double.MAX_VALUE;
	}
	
	public void evaluate(IErrorFunction function) {
		fitness = -function.value(x);
		pbest = fitness > pbest ? fitness : pbest;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public double[] getSolution() {
		return x;
	}
	
	public void update(double w) {
		for(int i = 0; i < v.length; i++) {
			v[i] = w * v[i] + C1 * rand.nextDouble() * (pbest - x[i]) + 
					C2 * rand.nextDouble() * (lbest - x[i]);
			x[i] += v[i];
		}
	}
}
