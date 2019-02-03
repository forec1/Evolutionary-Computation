package hr.fer.zemris.generic.ga.cross;

import java.util.List;

import hr.fer.zemris.generic.ga.IntegerArrayGASolution;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class IntegerFlatCrossover implements IGACrossover<int[]>{

	private IRNG rand;
	
	public IntegerFlatCrossover() {
		rand = RNG.getRNG();
	}
	
	@Override
	public void corssover(GASolution<int[]> p1, GASolution<int[]> p2, List<GASolution<int[]>> children) {
		if(!(p1 instanceof IntegerArrayGASolution) || !(p2 instanceof IntegerArrayGASolution)) {
			throw new IllegalArgumentException("Arguments p1 and p2 must be instances of IntegerArrayGASolution!");
		}
		IntegerArrayGASolution parent1 = (IntegerArrayGASolution) p1;
		IntegerArrayGASolution parent2 = (IntegerArrayGASolution) p2;
		int[] data1 = parent1.getData();
		int[] data2 = parent2.getData();
		int n = data1.length;
		int[] childData = new int[n];
		for(int i = 0; i < n; i++) {
			int min = Math.min(data1[i], data2[i]);
			int max = Math.max(data1[i], data2[i]);
			childData[i] = rand.nextInt(min, max + 1);
		}
		if(children.isEmpty()) {
			children.add(new IntegerArrayGASolution(childData));
		} else {
			children.set(0, new IntegerArrayGASolution(childData));
		}
	}
}
