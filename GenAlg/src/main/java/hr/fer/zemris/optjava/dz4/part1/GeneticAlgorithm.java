package hr.fer.zemris.optjava.dz4.part1;

import java.util.Arrays;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.function.IFunction;

public class GeneticAlgorithm {
	
	private IFunction function;
	private DoubleArraySolution[] population;
	private int populationSize;
	private String selection;
	private Random rand;
	private double minError;
	private int maxNumberOfGenerations;
	private double sigma;
	
	public GeneticAlgorithm(IFunction function, int populationSize, String selection, double minError, int maxNumberOfGenerations, double sigma) {
		this.function = function;
		this.population = new DoubleArraySolution[populationSize];
		this.selection = selection;
		this.populationSize = populationSize;
		this.minError = minError;
		this.maxNumberOfGenerations = maxNumberOfGenerations;
		this.sigma = sigma;
		rand = new Random();
		for(int i = 0; i < populationSize; i++) {
			this.population[i] = new DoubleArraySolution(function.getNumberOfVariables());
		}
		for(int i = 0, n = populationSize; i < n; i++) {
			for(int j = 0, m = function.getNumberOfVariables(); j < m; j++) {
				population[i].solution[j] = rand.nextDouble() * 10 - 5;
			}
		}
	}
	
	public void run() {
		DoubleArraySolution[] newPopulation = new DoubleArraySolution[populationSize];
		int numberOfGenerations = 0;
		evaluate();
		printStatus();
		DoubleArraySolution firstParent = null;
		DoubleArraySolution secondParent = null;
		DoubleArraySolution child = null;
		while(!isGood()) {
			DoubleArraySolution[] best = getBest(5);
			for(int k = 0; k < best.length; k++) {
				newPopulation[k] = best[k].duplicate();
			}
			int offset = best.length;
			while(offset < population.length) {
				if(selection.equals("rouletteWheel")) {
					double fitWorst = population[0].fitness;
					for(int k = 0; k < population.length; k++) {
						if(population[k].fitness < fitWorst) {
							fitWorst = population[k].fitness;
						}
					}
					for(int k = 0; k < population.length; k++) {
						population[k].fitness = population[k].fitness - fitWorst;
					}
					firstParent = rouletteWheel();
					secondParent = rouletteWheel();
					child = crossBLXalpha(firstParent, secondParent, 0.05);
					if(numberOfGenerations < maxNumberOfGenerations / 5) {
						child = mutate(child, sigma);
					}
					else if(numberOfGenerations < maxNumberOfGenerations / 4){
						child = mutate(child, sigma/5);
					} else if(numberOfGenerations < maxNumberOfGenerations / 2){
						child = mutate(child, sigma/10);
					} else if(numberOfGenerations < maxNumberOfGenerations / 1.5){
						child = mutate(child, sigma/50);
					} else {
						child = mutate(child, sigma/500);
					}
					newPopulation[offset] = child;
					offset++;
				} else if(selection.startsWith("tournament")) {
					int n = parseTournament();
					firstParent = tournament(n);
					secondParent = tournament(n);
					child = crossBLXalpha(firstParent, secondParent, 0.05);
					if(numberOfGenerations < maxNumberOfGenerations / 5) {
						child = mutate(child, sigma);
					}
					else if(numberOfGenerations < maxNumberOfGenerations / 4){
						child = mutate(child, sigma/5);
					} else if(numberOfGenerations < maxNumberOfGenerations / 2){
						child = mutate(child, sigma/10);
					} else if(numberOfGenerations < maxNumberOfGenerations / 1.5){
						child = mutate(child, sigma/50);
					} else {
						child = mutate(child, sigma/500);
					}
					newPopulation[offset] = child;
					offset++;
				}else {
					throw new IllegalArgumentException("Selection unkown!");
				}
			}
			for(int k = 0; k < population.length; k++) {
				population[k] = newPopulation[k].duplicate();
			}
			evaluate();
			printStatus();
			numberOfGenerations++;
			if(numberOfGenerations >= maxNumberOfGenerations) { 
				System.out.println("Max num of gen!");
				break;
			}
		}
	}
	
	private DoubleArraySolution tournament(int n) {
		int[] indexes = new int[n];
		int genereted = 0;
		while(genereted < n) {
			int temp = rand.nextInt(populationSize);
			if(!contains(indexes, temp)) {
				indexes[genereted++] = temp;
			}
		}
		DoubleArraySolution winner = population[indexes[0]];
		for(int i = 1; i < n; i++) {
			if(population[indexes[i]].fitness > winner.fitness) {
				winner = population[indexes[i]];
			}
		}
		return winner;
	}
	
	private boolean contains(int[] array, int x) {
		for(int a : array) {
			if(a == x) { return true; }
		}
		return false;
	}
	
	private int parseTournament() {
		String[] split = selection.split(":");
		return Integer.parseInt(split[1]);
	}
	
	private boolean isGood() {
		DoubleArraySolution[] best = getBest(1);
		double valAtBest = function.valueAt(best[0].solution);
		if(Math.abs(valAtBest) < minError) {
			System.out.println("Dost dobro!");
			return true;
		}
		return false;
	}
	
	private DoubleArraySolution[] getBest(int numberOfBest) {
		DoubleArraySolution[] best = new DoubleArraySolution[numberOfBest];
		Arrays.sort(population, (o1, o2) ->  {
			if(o1.fitness == o2.fitness) {
				return 0;
			} else if (o1.fitness > o2.fitness) {
				return -11;
			} else {
				return 1;
			}
		});
		for(int k = 0; k < numberOfBest; k++) {
			best[k] = population[k];
		}
		return best;
	}
	
	private void printStatus() {
		DoubleArraySolution[] best = getBest(1);
		StringBuilder sb = new StringBuilder();
		sb.append("x = " + best[0]);
		sb.append("fittnes = " + best[0].fitness);
		System.out.println(sb.toString());
	}
	
	private DoubleArraySolution mutate(DoubleArraySolution x, double s) {
		for(int i = 0, n = x.solution.length; i < n; i++) {
			x.solution[i] += rand.nextGaussian() * s;
		}
		return x;
	}
	
	private DoubleArraySolution crossBLXalpha(DoubleArraySolution firstParent, DoubleArraySolution secondParent, double alpha) {
		DoubleArraySolution child = new DoubleArraySolution(function.getNumberOfVariables());
		for(int i = 0, n = function.getNumberOfVariables(); i < n; i++) {
			double cMin = Math.min(firstParent.solution[i], secondParent.solution[i]);
			double cMax = Math.max(firstParent.solution[i], secondParent.solution[i]);
			double I = cMax - cMin;
			double upperBound = cMax + I * alpha;
			double lowerBound = cMin - I * alpha;
			child.solution[i] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;			
		}
		return child;
	}
	
	private DoubleArraySolution rouletteWheel() {
		double[] len = new double[populationSize];
		double fitnessSum = 0.0;
		for(int i = 0; i < populationSize; i++) {
			fitnessSum += population[i].fitness;
		}
		for(int i = 0; i < len.length; i++) {
			len[i] = population[i].fitness / fitnessSum;
		}
		double[] wheel = new double[len.length];
		wheel[0] = len[0];
		for(int i = 1; i < len.length; i++) {
			wheel[i] = len[i] + wheel[i - 1];
		}
		double wheelIndex = rand.nextDouble();
		int index = 0;
		for(int i = 0; i < wheel.length; i++) {
			if(wheel[i] > wheelIndex) {
				index = i;
				break;
			}
		}
		return population[index];
	}
	
	private void evaluate() {
		for(int i = 0; i < populationSize; i++) {
			population[i].fitness = -1 * function.valueAt(population[i].solution);
		}
	}
}
