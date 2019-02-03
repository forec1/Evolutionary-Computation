package hr.fer.zemris.generic.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

import hr.fer.zemris.generic.ga.cross.IGACrossover;
import hr.fer.zemris.generic.ga.mutate.IGAMutation;
import hr.fer.zemris.generic.ga.sel.IGASelection;
import hr.fer.zemris.optjava.eval.IGAEvaluatorProvider;

public class AllJob<T> implements Runnable{

	private List<GASolution<T>> population;
	private BlockingQueue<List<GASolution<T>>> evaluated;
	private int childrenToCreate;
	private IGACrossover<T> crossover;
	private IGASelection<T> selection;
	private IGAMutation<T> mutation;
	
	public AllJob() {}

	public AllJob(List<GASolution<T>> population, BlockingQueue<List<GASolution<T>>> evaluated, int childrenToCreate,
			IGACrossover<T> crossover, IGASelection<T> selection, IGAMutation<T> mutation) {
		super();
		this.population = population;
		this.evaluated = evaluated;
		this.childrenToCreate = childrenToCreate;
		this.crossover = crossover;
		this.selection = selection;
		this.mutation = mutation;
	}

	@Override
	public void run() {
		int created = 0;
		List<GASolution<T>> createdChildren = new ArrayList<>(childrenToCreate);
		while(created < childrenToCreate) {
			List<GASolution<T>> children = new ArrayList<>(2);
			GASolution<T> p1 = selection.select(population);
			GASolution<T> p2 = selection.select(population);
			crossover.corssover(p1, p2, children);
			if(created + children.size() > childrenToCreate) {
				int toCreate = childrenToCreate - created;
				children = children.stream().limit(toCreate).collect(Collectors.toList());
			}
			mutate(children);
			evaluate(children);
			createdChildren.addAll(children);
			created += children.size();
		}
		evaluated.add(createdChildren);
	}
	
	private void mutate(List<GASolution<T>> children) {
		for(GASolution<T> child : children) {
			mutation.mutate(child);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void evaluate(List<GASolution<T>> children) {
		for(GASolution<T> child : children) {
			((IGAEvaluatorProvider<T>)Thread.currentThread()).getEvaluator().evaluate(child);
		}
	}

}
