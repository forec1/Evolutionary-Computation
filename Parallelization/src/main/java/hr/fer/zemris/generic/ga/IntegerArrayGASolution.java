package hr.fer.zemris.generic.ga;

public class IntegerArrayGASolution extends GASolution<int[]>{
	
	public IntegerArrayGASolution(int[] data) {
		this.data = data;
	}
	
	@Override
	public GASolution<int[]> duplicate() {
		int[] dataNew = new int[this.data.length];
		for(int i = 0; i < dataNew.length; ++i) {
			dataNew[i] = this.data[i];
		}
		IntegerArrayGASolution dup = new IntegerArrayGASolution(dataNew);
		dup.fitness = this.fitness;
		return dup;
	}

}
