package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

public interface IHFunction extends IFunction{
	
	/**
	 * Calculates Hesse-Matrix for given input. Input x 
	 * must be a vector with dimensions (N x 1) where N is 
	 * function's number of variables. 
	 * @param x Input vector
	 * @return Hesse-Matrix for given input
	 */
	public Matrix getHesseMatrix(Matrix x);

}
