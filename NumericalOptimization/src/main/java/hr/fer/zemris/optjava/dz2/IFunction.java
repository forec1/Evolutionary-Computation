package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

public interface IFunction {
	
	public int getNumberOfVariables();
	public double value(Matrix x);
	public Matrix getGrad(Matrix x);
}
