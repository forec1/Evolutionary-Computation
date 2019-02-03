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
import hr.fer.zemris.optjava.eval.IGAEvaluator;
import hr.fer.zemris.optjava.rng.EVOThread;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

/**
 * Generic genetic algorithm.
 * @author forec
 *
 * @param <T> Data type.
 */
public class ParallelGA<T> {

	private BlockingQueue<AllJob<T>> toDo;
	private BlockingQueue<List<GASolution<T>>> evaluated;
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
	private int childrenToCreate = 1;
	
	/**
	 * Number of best solutions to pass on to the next generation.
	 */
	private int b = 2;
	
	public ParallelGA(int populationSize, int maxiter, double minFitness, ISolutionFactory<T> solutionFactory
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
		this.toDo = new LinkedBlockingQueue<>();
		this.evaluated = new LinkedBlockingQueue<>();
		this.numOfProcessors = Runtime.getRuntime().availableProcessors();
	}
	
	public GASolution<T> run() {
		initWorkers();
		initPopulation();
		int iter = 0;
		while(iter <= maxIter) {
			List<GASolution<T>> nextGeneration = new ArrayList<>(populationSize);
			List<GASolution<T>> best = getBest(b);
			if(checkForEnd(best.get(0))) { break; }
			nextGeneration.addAll(best);
			printStatus(best.get(0), iter);
			giveJobs();
			addEvaluatedChildren(nextGeneration);
			population = nextGeneration;
			++iter;
		}
		poisonWorkers();
		GASolution<T> best = getBest(1).get(0);
		return best;
	}
	
	private void addEvaluatedChildren(List<GASolution<T>> nextGeneration) {
		try {
			int childrenEvaluated = 0;
			while(childrenEvaluated < populationSize - b) {
				List<GASolution<T>> children = evaluated.take();
				if(childrenEvaluated + children.size() > populationSize - b) {
					int n = populationSize - b - childrenEvaluated;
					for(int i = 0; i < n; ++i) {
						nextGeneration.add(children.get(i));
					}
				} else {
					nextGeneration.addAll(children);
				}
				childrenEvaluated += children.size();
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
	
	private void giveJobs() {
		int n = populationSize / childrenToCreate;
		for(int i = 0; i < n; ++i) {
			toDo.add(new AllJob<>(population, evaluated, childrenToCreate, crossover, selection, mutation));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void poisonWorkers() {
		for(int i = 0; i < numOfProcessors; ++i) {
			toDo.add((AllJob<T>) Job.PILL);
		}
	}
	
	private void initWorkers() {
		for(int i = 0; i < numOfProcessors; ++i) {
			EVOThread<T> worker = new EVOThread<T>(new Job<>(toDo));
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
	
	private void initPopulation() {
		IRNG rand = RNG.getRNG();
		for(int i = 0; i < populationSize; i++) {
			GASolution<T> solution = solutionFactory.getRandomSolution(rand);
			evaluator.evaluate(solution);
			population.add(solution);
		}
	}
}
