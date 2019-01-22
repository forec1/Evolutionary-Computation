package hr.fer.zemris.optjava.dz10;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>minimize: f1(X) = x1</p>
 * <p>minimize: f2(X) = (1 + x2) / x1</p>
 * <p>0.1 <= x1 <= 1 </p>
 * <p>0 <= x2 <= 5 </p>
 * @author filip
 *
 */
public class ProblemA implements MOOPProblem{
	
	private List<double[]> constraints;
	
	public ProblemA() {
		makeConstraints();
	}
	
	private void makeConstraints() {
		constraints = new LinkedList<>();
		double[] x1Constraint = {0.1, 1.0};
		double[] x2Constraint = {0.0, 5.0};
		constraints.add(x1Constraint);
		constraints.add(x2Constraint);
	}
	
	public int getNumberOfObjectives() {
		return 2;
	}

	public void evaluateSolution(double[] solution, double[] objectives) {
		if(objectives.length < 2) {
			throw new IllegalArgumentException("Length of objectives array must be more than 2");
		}
		objectives[0] = -solution[0];
		objectives[1] = -(1 + solution[1]) / solution[0];
	}
	
	public List<double[]> getConstraints() {
		return constraints;
	}

	@Override
	public int getDimension() {
		return 2;
	}

}
