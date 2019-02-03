package hr.fer.zemris.generic.ga.cross;

import java.util.List;

import hr.fer.zemris.generic.ga.IntegerArrayGASolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class IntegerSimpleCrossover implements IGACrossover<int[]>{
	

	@Override
	public void corssover(GASolution<int[]> p1, GASolution<int[]> p2, List<GASolution<int[]>> children) {
		if(!(p1 instanceof IntegerArrayGASolution) || !(p2 instanceof IntegerArrayGASolution)) {
			throw new IllegalArgumentException("Arguments p1 and p2 must be instances of IntegerArrayGASolution!");
		}
		IRNG rand = RNG.getRNG();
		IntegerArrayGASolution parent1 = (IntegerArrayGASolution) p1;
		IntegerArrayGASolution parent2 = (IntegerArrayGASolution) p2;
		int[] data1 = parent1.getData();
		int[] data2 = parent2.getData();
		int n = data1.length;
		int[] childData = new int[n];
		int breakIndex = rand.nextInt(1, n);
		int i = 0;
		for(; i < breakIndex; ++i) childData[i] = data1[i];
		for(; i < n; ++i) childData[i] = data2[i];
		if(children.isEmpty()) {
			children.add(new IntegerArrayGASolution(childData));
		} else {
			children.set(0, new IntegerArrayGASolution(childData));
		}
	}
}