package hr.fer.zemris.generic.ga.sel;

import java.util.List;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class KTournamentSelection<T> implements IGASelection<T>{
	
	private IRNG rand;
	private int k;
	
	public KTournamentSelection(int k) {
		this.k = k;
		this.rand = RNG.getRNG();
	}

	@Override
	public GASolution<T> select(List<GASolution<T>> population) {
		int[] indexes = new int[k];
		int generated = 0;
		int n = population.size();
		while(generated < k) {
			int index = rand.nextInt(0, n);
			if(!contains(indexes, index)) {
				indexes[generated++] = index;
			}
		}
		GASolution<T> winner = population.get(indexes[0]);
		for(int i = 1; i < k; i++) {
			GASolution<T> toCompare = population.get(indexes[i]);
			if(toCompare.fitness > winner.fitness) {
				winner = toCompare;
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
	
	@Override
	public String toString() {
		return k + "-tournament selekcija";
	}
}
