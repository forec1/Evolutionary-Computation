package hr.fer.zemris.generic.ga.sel;

import java.util.Collections;
import java.util.List;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class RouletteWheelSelection<T> implements IGASelection<T>{

	private IRNG rand;
	private double[] fitness;
	
	public RouletteWheelSelection() {
		rand = RNG.getRNG();
	}
	
	@Override
	public GASolution<T> select(List<GASolution<T>> population) {
		init(population);
		double fitnessSum = 0.0;
		for(int i = 0; i < fitness.length; ++i) {
			fitnessSum += fitness[i];
		}
		double value = rand.nextDouble() * fitnessSum;
		for(int i = 0; i < fitness.length; ++i) {
			value -= fitness[i];
			if(value < 0) {
				return population.get(i).duplicate();
			}
		}
		return population.get(population.size() - 1).duplicate();
	}
	
	private void init(List<GASolution<T>> population) {
		fitness = new double[population.size()];
		double fitWorst = Collections.min(population).fitness;
		for(int i = 0, n = population.size(); i < n; ++i) {
			fitness[i] = population.get(i).fitness - fitWorst;  
		}
	}
	
	@Override
	public String toString() {
		return "roulette wheel selection";
	}
}
