package hr.fer.zemris.optjava.dz10;

import java.util.LinkedList;
import java.util.List;

public class ProblemB implements MOOPProblem{

	private List<double[]> constraints;
	
	public ProblemB() {
		makeConstraints();
	}
	
	private void makeConstraints() {
		constraints = new LinkedList<>();
		for(int i = 0; i < 4; i++) {
			double[] constraint = {-5, 5};
			constraints.add(constraint);
		}
	}
	
	@Override
	public int getNumberOfObjectives() {
		return 4;
	}

	@Override
	public void evaluateSolution(double[] solution, double[] objectives) {
		if(objectives.length < 4) {
			throw new IllegalArgumentException("Length of objectives array must be more than 4");
		}
		for(int i = 0; i < 4; i++) {
			objectives[i] = -Math.pow(solution[i], 2);
		}		
	}

	@Override
	public List<double[]> getConstraints() {
		return constraints;
	}

	@Override
	public int getDimension() {
		return 4;
	}

}
