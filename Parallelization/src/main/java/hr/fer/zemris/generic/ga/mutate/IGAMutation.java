package hr.fer.zemris.generic.ga.mutate;

import hr.fer.zemris.generic.ga.GASolution;

public interface IGAMutation<T> {

	public void mutate(GASolution<T> f);
}
