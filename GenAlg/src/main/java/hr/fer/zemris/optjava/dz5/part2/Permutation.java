package hr.fer.zemris.optjava.dz5.part2;

public class Permutation {
	
	public int[] solution;
	public long fitness;
	
	public Permutation(int n) {
		solution = new int[n];
		for (int i = 0; i < solution.length; i++) {
			solution[i] = -1;
		}
	}
	
	public Permutation duplicate() {
		Permutation ret = new Permutation(this.solution.length);
		for(int i = 0; i < this.solution.length; i++) {
			ret.solution[i] = this.solution[i];
		}
		ret.fitness = this.fitness;
		return ret;
	}
	
	@Override
	public String toString() {
		String s = "p = ( ";
		for(int sol : solution) {
			s += sol + " ";
		}
		s += ")";
		return s;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Permutation)) {
			return false;
		}
		Permutation p = (Permutation) obj;
		for(int i = 0; i < this.solution.length; i++) {
			if(this.solution[i] != p.solution[i]) {
				return false;
			}
		}
		return true;
	}
	
}
