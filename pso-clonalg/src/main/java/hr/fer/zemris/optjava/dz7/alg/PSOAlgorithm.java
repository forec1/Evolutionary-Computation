package hr.fer.zemris.optjava.dz7.alg;

import java.util.Random;

import hr.fer.zemris.optjava.dz7.function.IErrorFunction;

public class PSOAlgorithm implements Algorithm{
	
	private double[][] x;
	private double[][] v;
	private double[] f;
	private double[] pbest_f;
	private double[][] pbest;
	private double[] lbest_f;
	private double[][] lbest;
	private double gbest_f;
	private double[] gbest;
	private int populationSize;
	private IErrorFunction function;
	private double xmin;
	private double xmax;
	private double vmin;
	private double vmax;
	private Random rand;
	private double C1;
	private double C2;
	private double merr;
	private int maxiter;
	private int d;
	private boolean gbestTest;
	private double w;

	
	public PSOAlgorithm(int populationSize, IErrorFunction function, double xmin, double xmax, 
			double vmin, double vmax, double C1, double C2, double merr, int maxiter, int d) {
		this.rand = new Random();
		this.populationSize = populationSize;
		this.function = function;
		this.xmax = xmax;
		this.xmin = xmin;
		this.vmax = vmax;
		this.vmin = vmin;
		this.C1 = C1;
		this.C2 = C2;
		this.merr = merr;
		this.maxiter = maxiter;
		this.d = d;
		this.gbestTest = d >= Math.floor(populationSize / 2) ? true : false;
		this.w = 3;
		
	}
	
	public double[] run() {
		initializePopulation();
		initializeBest();
		int iteration = 0;
		while(iteration < maxiter) {
			for(int i = 0; i < populationSize; i++) {
				f[i] = -function.value(x[i]);
				if(iteration == 0) { 
					pbest_f[i] = f[i];
					pbest[i] = clone(x[i]);
				}
			}
			System.out.print("");
			for(int i = 0; i < populationSize; i++) {
				if(iteration == 0) { break; }
				if(f[i] > pbest_f[i]) {
					pbest_f[i] = f[i];
					pbest[i] = clone(x[i]);
				}
			}
			if(w > 0) {
				w -= 0.001;
			}
			updateBest();
			if(-gbest_f < merr) {
				return gbest;
			}
			updateVelocityAndPosition();
			iteration++;
			System.out.println("best: " + gbest_f + " iter: " + iteration + " w: " + w);
		}
		return gbest;
		
	}
	
	private static double[] clone(double[] x) {
		double[] clone = new double[x.length];
		for(int i = 0; i < x.length; i++) {
			clone[i] = x[i];
		}
		return clone;
	}
	
	private void initializeBest() {
		if(!gbestTest) {
			for(int i = 0; i < populationSize; i++) {
				lbest_f[i] = - Double.MAX_VALUE;
			}
		}
		gbest_f = - Double.MAX_VALUE;
	}
	
	private void updateVelocityAndPosition() {
		for(int i = 0, dim = function.getDimension(); i < populationSize; i++) {
			for(int j = 0; j < dim; j++) {
				v[i][j] = w * v[i][j] + C1 * rand.nextDouble() * (pbest[i][j] - x[i][j]);
				v[i][j] += gbestTest ? C2 * rand.nextDouble() * (gbest[j] - x[i][j]) : 
					C2 * rand.nextDouble() * (lbest[i][j] - x[i][j]);
				v[i][j] = fromRange(v[i][j], vmin, vmax);
				x[i][j] += v[i][j]; 
			}
		}
	}
	
	private double fromRange(double v, double vmin, double vmax) {
		if(v > vmax) { return vmax; }
		if(v < vmin) { return vmin; }
		return v;
	}
	
	private void updateBest() {
		for(int i = 0; i < populationSize; i++) {
			if(f[i] > gbest_f) {
				gbest_f = f[i];
				gbest = clone(x[i]);
			}
		}
		if(!gbestTest) {
			for(int i = 0; i < populationSize; i++) {
				for(int j = i - d; j < i + d; j++) {
					int index = j < 0 ? populationSize + j : j;
					index = j >= populationSize ? j - populationSize : index;
					if(f[index] > lbest_f[i]) {
						lbest_f[i] = f[index];
						lbest[i] = clone(x[index]);
					}
				}
			}
		}
	}
	
	private void initializePopulation() {
		int dim = function.getDimension();
		x = new double[populationSize][dim];
		v = new double[populationSize][dim];
		f = new double[populationSize];
		pbest_f = new double[populationSize];
		pbest = new double[populationSize][dim];
		if(!gbestTest) {
			lbest_f = new double[populationSize];
			lbest = new double[populationSize][dim];
		} else {
			gbest = new double[dim];
		}
		
		for(int i = 0; i < populationSize; i++) {
			for(int j = 0; j < dim; j++) {
				x[i][j] = rand.nextDouble() * (xmax - xmin + 1) + xmin;
				v[i][j] = rand.nextDouble() * (vmax - vmin + 1) + vmin;
			}
		}
		
	}
}
