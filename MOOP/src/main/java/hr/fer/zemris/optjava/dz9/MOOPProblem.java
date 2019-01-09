package hr.fer.zemris.optjava.dz9;

import java.util.List;

public interface MOOPProblem {
	public int getNumberOfObjectives();
	public void evaluateSolution(double[] solution, double[] objectives);
	public List<double[]> getConstraints();
	public int getDimension();
}
