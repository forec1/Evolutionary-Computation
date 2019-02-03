package hr.fer.zemris.optjava.eval;

import hr.fer.zemris.generic.ga.GASolution;

public interface IGAEvaluator<T> {
	public void evaluate(GASolution<T> p);
	public IGAEvaluator<T> duplicate();
}
