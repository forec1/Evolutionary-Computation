package hr.fer.zemris.generic.ga.cross;

import java.util.List;

import hr.fer.zemris.generic.ga.GASolution;

public interface IGACrossover<T> {

	public void corssover(GASolution<T> p1, GASolution<T> p2, List<GASolution<T>> children);
}
