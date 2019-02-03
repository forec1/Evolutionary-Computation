package hr.fer.zemris.generic.ga.sel;

import java.util.List;

import hr.fer.zemris.generic.ga.GASolution;

public interface IGASelection<T> {

	public GASolution<T> select(List<GASolution<T>> population);
}
