package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

public class ErrorFunction implements IHFunction{
	
	Matrix systemMatrix;
	
	public ErrorFunction(Matrix systemMatrix) {
		this.systemMatrix = systemMatrix;
	}

	public int getNumberOfVariables() {
		return 10;
	}

	public double value(Matrix x) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Matrix getGrad(Matrix x) {
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix getHesseMatrix(Matrix x) {
		// TODO Auto-generated method stub
		return null;
	}

}
