package hr.fer.zemris.optjava.dz4.part1;

public class DoubleArraySolution {
	
	public double[] solution;
	public double fitness;
	
	public DoubleArraySolution(int numberOfVariables) {
		solution = new double[numberOfVariables];
	}
	
	public DoubleArraySolution duplicate() {
		DoubleArraySolution duplicate = new DoubleArraySolution(this.solution.length);
		duplicate.fitness = this.fitness;
		for(int i = 0; i < this.solution.length; i++) {
			duplicate.solution[i] = this.solution[i];
		}
		return duplicate;
	}
	
	@Override
	public String toString() {
		String s = "[ ";
		for(double sol : solution) {
			s += sol + " ";
		}
		s += "]";
		return s;
	}
}
