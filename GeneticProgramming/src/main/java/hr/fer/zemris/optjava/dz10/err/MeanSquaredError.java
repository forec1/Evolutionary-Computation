package hr.fer.zemris.optjava.dz10.err;

import hr.fer.zemris.optjava.dz10.tree.Tree;

public class MeanSquaredError implements IErrorFunction{

	private double[][] input;
	private int numberOfVars;
	
	public MeanSquaredError(double[][] input, int numberOfVars) {
		super();
		this.input = input;
		this.numberOfVars = numberOfVars;
	}

	@Override
	public void evaluate(Tree tree) {
		double[] guess = guess(tree);
		int N = input.length;
		double error = 0.0;
		for(int i = 0; i < N; i++) {
			error += Math.pow(guess[i] - input[i][numberOfVars], 2);
		}
		tree.setFitness(-error);
	}
	
	public void printEvaluation(Tree tree) {
		System.out.println("x0  f(x)	fg(x)");
		double[] guess = guess(tree);
		for(int i = 0; i < input.length; i++) {
			System.out.println(input[i][0]+ " " + input[i][1] + " " + guess[i]);
		}
	}

	private double[] guess(Tree tree) {
		double[] guess = new double[input.length];
		double[] vars = new double[numberOfVars];
		for(int i = 0; i < input.length; i++) {
			for(int j = 0; j < numberOfVars; j++) {
				vars[j] = input[i][j];
			}
			tree.setTerminalNodes(vars);
			guess[i] = tree.calculate();
		}
		return guess;
	}
}
