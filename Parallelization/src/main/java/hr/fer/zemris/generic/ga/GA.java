package hr.fer.zemris.generic.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import hr.fer.zemris.generic.ga.cross.IGACrossover;
import hr.fer.zemris.generic.ga.mutate.IGAMutation;
import hr.fer.zemris.generic.ga.sel.IGASelection;
import hr.fer.zemris.optjava.eval.IGAEvaluator;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Generic genetic algorithm.
 * @author forec
 *
 * @param <T> Data type.
 */
public class GA<T> {

	private List<GASolution<T>> population;
	private ISolutionFactory<T> solutionFactory;
	private IGAEvaluator<T> evaluator;
	private IGACrossover<T> crossover;
	private IGASelection<T> selection;
	private IGAMutation<T> mutation;
	private int populationSize;
	private int maxIter;
	private double minFitness;
	
	/**
	 * Number of best solutions to pass to the next generation.
	 */
	private int b = 5;
	
	public GA(int populationSize, int maxiter, double minFitness, ISolutionFactory<T> solutionFactory
			,IGAEvaluator<T> evaluator, IGACrossover<T> crossover, IGASelection<T> selection, IGAMutation<T> mutation) {
		this.populationSize = populationSize;
		this.maxIter = maxiter;
		this.minFitness = minFitness;
//		this.b = populationSize / 100;
		this.solutionFactory = solutionFactory;
		this.evaluator = evaluator;
		this.crossover = crossover;
		this.selection = selection;
		this.mutation = mutation;
		this.population = new ArrayList<>(populationSize);
	}
	
	public GASolution<T> run() {
		initPopulation();
		List<GASolution<T>> children = new ArrayList<>(2);
		int iter = 0;
		while(iter <= maxIter) {
			List<GASolution<T>> nextGeneration = new ArrayList<>(populationSize);
			evaluate();
			List<GASolution<T>> best = getBest(b);
//			if(iter % 1000 == 0 || iter == 100 || iter == 200 || iter == 500)
//			System.out.println("iter: " + iter + "best : " + best.get(0).fitness);
			if(checkForEnd(best.get(0))) break;
			addToNextGeneration(best, nextGeneration);
			while(nextGeneration.size() < populationSize) {
				GASolution<T> p1 = selection.select(population);
				GASolution<T> p2 = selection.select(population);
				crossover.corssover(p1, p2, children);
				mutate(children);
				addToNextGeneration(children, nextGeneration);
			}
			population = nextGeneration;
			++iter;
		}
		evaluate();
		GASolution<T> best = getBest(1).get(0);
		return best;
	}
	
	private boolean checkForEnd(GASolution<T> best) {
		return best.fitness > minFitness ? true : false;
	}
	
	private List<GASolution<T>> getBest(int n) {
		List<GASolution<T>> best = null;
		if(n == 1) {
			best = new ArrayList<>(1);
			best.add(Collections.max(population));
		} else {
			best = population.stream().sorted(Collections.reverseOrder()).limit(n).collect(Collectors.toList());
		}
		return best;
	}
	
	private void mutate(List<GASolution<T>> children) {
		for(GASolution<T> child : children) {
			mutation.mutate(child);
		}
	}
	
	private void addToNextGeneration(List<GASolution<T>> children, List<GASolution<T>> nextGeneration) {
		for(GASolution<T> child : children) {
			if(nextGeneration.size() < populationSize) {
				nextGeneration.add(child);
			}
		}
	}
	
	private void evaluate() {
		for(GASolution<T> sol : population) {
			evaluator.evaluate(sol);
		}
	}
	
	private void initPopulation() {
		IRNG rand = RNG.getRNG();
		for(int i = 0; i < populationSize; i++) {
			GASolution<T> solution = solutionFactory.getRandomSolution(rand);
			population.add(solution);
		}
	}
}
