package hr.fer.zemris.generic.ga.mutate;

import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.optjava.rng.IRNG;
import hr.fer.zemris.optjava.rng.RNG;

public class ByteArrayMutationImpl implements IGAMutation<int[]>{

	private int range;
	private IRNG rand;
	
	public ByteArrayMutationImpl(int range) {
		this.range = range < 0 ? -range : range;
		rand = RNG.getRNG();
	}

	@Override
	public void mutate(GASolution<int[]> f) {
		int[] data = f.getData();
		int index = 1;
		for(int i = 0 , n = (data.length - 1) /5; i < n; ++i) {
			if(rand.nextDouble() < 0.05) {
				data[index] = data[index] + rand.nextInt(-range, range + 1) ;
				data[index + 1] = data[index + 1] + rand.nextInt(-range, range + 1);
				data[index + 4] = data[index + 4] + rand.nextInt(-range, range + 1);
			}
			index += 5;
		}	
	}
}
