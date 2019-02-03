package hr.fer.zemris.generic.ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import hr.fer.zemris.generic.ga.cross.IGACrossover;
import hr.fer.zemris.generic.ga.mutate.IGAMutation;
import hr.fer.zemris.generic.ga.sel.IGASelection;
import hr.fer.zemris.optjava.eval.EvaluateJob;
import hr.fer.zemris.optjava.eval.IGAEvaluator;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Generic genetic algorithm with parallel evaluation.
 * @author forec
 *
 * @param <T> Data type.
 */
public class ParallelEvaluationGA<T> {

	private BlockingQueue<GASolution<T>> toEvaluate;
	private BlockingQueue<GASolution<T>> evaluated;
	private List<GASolution<T>> population;
	private ISolutionFactory<T> solutionFactory;
	private IGAEvaluator<T> evaluator;
	private IGACrossover<T> crossover;
	private IGASelection<T> selection;
	private IGAMutation<T> mutation;
	private int populationSize;
	private int maxIter;
	private double minFitness;
	private int numOfProcessors;
	
	/**
	 * Number of best solutions to pass on to the next generation.
	 */
	private int b = 2;
	
	public ParallelEvaluationGA(int populationSize, int maxiter, double minFitness, ISolutionFactory<T> solutionFactory
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
		this.toEvaluate = new LinkedBlockingQueue<>();
		this.evaluated = new LinkedBlockingQueue<>();
		this.numOfProcessors = Runtime.getRuntime().availableProcessors();
	}
	
	public GASolution<T> run() {
		initWorkers();
		initPopulation();
		List<GASolution<T>> children = new ArrayList<>(2);
		int iter = 0;
		while(iter <= maxIter) {
			List<GASolution<T>> nextGeneration = new ArrayList<>(populationSize);
			List<GASolution<T>> best = getBest(b);
			printStatus(best.get(0), iter);
			if(checkForEnd(best.get(0))) break;
			addToNextGeneration(best, nextGeneration);
			int numOfChildren = 0;
			while(numOfChildren < populationSize - b) {
				GASolution<T> p1 = selection.select(population);
				GASolution<T> p2 = selection.select(population);
				crossover.corssover(p1, p2, children);
				mutate(children);
				toEvaluate.addAll(children);
				numOfChildren += children.size();
			}
			addEvaluatedChildren(nextGeneration);
			population = nextGeneration;
			++iter;
		}
		poisonWorkers();
		GASolution<T> best = getBest(1).get(0);
		return best;
	}
	
	@SuppressWarnings("unchecked")
	private void poisonWorkers() {
		for(int i = 0; i < numOfProcessors; ++i) {
			toEvaluate.add((GASolution<T>) EvaluateJob.PILL);
		}
	}
	
	private void addEvaluatedChildren(List<GASolution<T>> nextGeneration) {
		try {
			int childrenEvaluated = 0;
			while(childrenEvaluated < populationSize - b) {
				GASolution<T> child = evaluated.take();
				nextGeneration.add(child);
				++childrenEvaluated;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		evaluated.clear();
	}
	
	private void printStatus(GASolution<T> best, int iter) {
		if(iter % 100 == 0)
			System.out.println("iter: " + iter + "best : " + best.fitness);
	}
	
	private void initWorkers() {
		for(int i = 0; i < numOfProcessors; ++i) {
			EVOThread<T> worker = new EVOThread<T>(new EvaluateJob<>(toEvaluate, evaluated));
			worker.setEvalator(evaluator.duplicate());
			worker.start();
		}
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
	
	private void initPopulation() {
		IRNG rand = RNG.getRNG();
		for(int i = 0; i < populationSize; i++) {
			GASolution<T> solution = solutionFactory.getRandomSolution(rand);
			toEvaluate.add(solution);
		}
		int added = 0;
		while(added < populationSize) {
			try {
				GASolution<T> sol = evaluated.take();
				population.add(sol);
				++added;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
