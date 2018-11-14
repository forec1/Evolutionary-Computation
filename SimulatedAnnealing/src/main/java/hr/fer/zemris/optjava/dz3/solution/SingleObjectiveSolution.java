package hr.fer.zemris.optjava.dz3.solution;

public abstract class SingleObjectiveSolution implements Comparable<SingleObjectiveSolution>{
	
	public double fitness;
	public double value;
	
	public SingleObjectiveSolution() {
		fitness = value = 0.0;
	}

	public int compareTo(SingleObjectiveSolution o) {
		if(Math.abs(this.fitness - o.fitness) < 10e-9) {
			return 0;
		} else if(this.fitness > o.fitness) {
			return 1;
		} else {
			return -1;
		}
	}
	
	public abstract SingleObjectiveSolution duplicate();
	public abstract SingleObjectiveSolution newLikeThis();

}
