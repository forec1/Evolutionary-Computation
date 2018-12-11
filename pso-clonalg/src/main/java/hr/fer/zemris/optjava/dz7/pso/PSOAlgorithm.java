package hr.fer.zemris.optjava.dz7.pso;

import java.util.Random;

import hr.fer.zemris.optjava.dz7.function.IErrorFunction;

public class PSOAlgorithm {
	
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
		gbestTest = d >= Math.floor(populationSize / 2) ? true : false;
		
	}
	
	public void run() {
		initializePopulation();
		int iteration = 0;
		while(iteration < maxiter) {
			for(int i = 0; i < populationSize; i++) {
				f[i] = -function.value(x[i]);
				if(iteration == 0) { 
					pbest_f[i] = f[i];
					pbest[i] = x[i];
				}
			}
			for(int i = 0; i < populationSize; i++) {
				if(iteration == 0) { break; }
				if(f[i] > pbest_f[i]) {
					pbest_f[i] = f[i];
					pbest[i] = x[i];
				}
			}
			
			iteration++;
		}
		
	}
	
	private void updateBest() {
		if(gbestTest) {
			for(int i = 0; i < populationSize; i++) {
				if(f[i] > gbest_f) {
					gbest_f = f[i];
					gbest = x[i];
				}
			}
		} else {
			for(int i = 0; i < populationSize; i++) {
				//TODO naci lbest
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
