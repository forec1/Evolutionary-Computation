package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

/**
 * Funkcija oblika f(x1, x2) = x1^2 + (x2 - 1)^2
 * @author filip
 *
 */
public class Function1 implements IHFunction{
	
	public int getNumberOfVariables() {
		return 2;
	}

	public double value(Matrix x) {
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		return Math.pow(x1, 2) + Math.pow(x2 - 1, 2);
	}

	public Matrix getGrad(Matrix x) {
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		
		double grad1 = 2 * x1;
		double grad2 = 2 * (x2 - 1);
		double[][] grad = {{grad1}, 
						   {grad2}};
		
		return new Matrix(grad);
	}

	public Matrix getHesseMatrix(Matrix x) {
		double[][] h = {{2, 0},
						{0, 2}};
		return new Matrix(h);
	}

}
