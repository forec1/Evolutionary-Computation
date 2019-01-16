package hr.fer.zemris.optjava.dz8.alg;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz8.function.IErrorFunction;

public class DEAlgorithm {
	
	private IErrorFunction errorFunction;
	private List<double[]> population;
	private double[] targetVector;
	private double[] baseVector;
	private double[] Xr1;
	private double[] Xr2;
	private double[] bL;
	private double[] bU;
	private double[] fit;
	private double F;
	private double Cr;
	private int D;
	private int populationSize;
	private int maxiter;
	private double merr;
	private Random rand;
	
	public DEAlgorithm(IErrorFunction errorFunction, int populationSize, int maxiter, double merr, 
			double[] bL, double[] bU, double F, double Cr) {
		
		this.errorFunction = errorFunction;
		this.D = errorFunction.getDimension();
		this.bL = bL;
		this.bU = bU;
		this.F = F;
		this.Cr = Cr;
		this.populationSize = populationSize;
		this.maxiter = maxiter;
		this.merr = merr;
		this.population = new LinkedList<>();
		this.rand = new Random();
		this.fit = new double[populationSize];
		initializePopulation();
		evaluatePopulation();
	}

	public double[] run() {
		int iter = 0;
		while(iter < maxiter) {
			List<double[]> newPopulation = new LinkedList<>();
			for(int currentSolutionIndex = 0, n = population.size(); currentSolutionIndex < n; currentSolutionIndex++) {
				targetVector = population.get(currentSolutionIndex);
				int r0, r1, r2;
				do {
					r0 = rand.nextInt(population.size());
				} while(r0 == currentSolutionIndex);
				
				do {
					r1 = rand.nextInt(population.size());
				}while(r1 == r0 || r1 == currentSolutionIndex);
				
				do {
					r2 = rand.nextInt(population.size());
				} while(r2 == r1 || r2 == r0 || r2 == currentSolutionIndex);
				
				baseVector = population.get(r0);
				Xr1 = population.get(r1);
				Xr2 = population.get(r2);
				double[] mutantVector = new double[targetVector.length];
				for(int i = 0; i < mutantVector.length; i++) {
					mutantVector[i] = baseVector[i] + F * (Xr1[i] - Xr2[i]);
				}
				
				double[] trialVector = new double[targetVector.length];
				boolean fromMutant = false;
				for(int i = 0; i < targetVector.length; i++) {
					double probs = rand.nextDouble();
					if(probs > Cr) {
						trialVector[i] = mutantVector[i];
						fromMutant = true;
					} else {
						trialVector[i] = targetVector[i];
					}
				}
				if(!fromMutant) {
					int jRand = rand.nextInt(trialVector.length);
					trialVector[jRand] = mutantVector[jRand];
				}
				double trialVectorFit = -errorFunction.value(trialVector);
				if(trialVectorFit >= fit[currentSolutionIndex]) {
					newPopulation.add(trialVector);
					fit[currentSolutionIndex] = trialVectorFit;
				} else {
					newPopulation.add(targetVector);
				}
			}
			population = newPopulation;
			int bestIndex = getBestIndex();
			if(-fit[bestIndex] < merr) {
				return population.get(bestIndex);
			}
			System.out.println("best: " + (-fit[bestIndex]) + " iter: " + iter);
			iter++;
		}
		int bestIndex = getBestIndex();
		return population.get(bestIndex);
	}
	
	private int getBestIndex() {
		int bestIndex = 0;
		for(int i = 1; i < fit.length; i++) {
			if(fit[i] > fit[bestIndex]) {
				bestIndex = i;
			}
		}
		return bestIndex;
	}
	
	private void evaluatePopulation() {
		for(int i = 0; i < populationSize; i++) {
			fit[i] = -errorFunction.value(population.get(i));
		}
	}
	
	private void initializePopulation() {
		for(int i = 0; i < populationSize; i++) {
			double[] solution = new double[D];
			for(int j = 0; j < D; j++) {
				solution[j] = rand.nextDouble() * (bU[i] - bL[i]) + bL[i];
			}
			population.add(solution);
		}
	}
}
