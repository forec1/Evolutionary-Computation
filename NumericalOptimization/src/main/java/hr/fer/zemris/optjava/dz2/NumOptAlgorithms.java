package hr.fer.zemris.optjava.dz2;

import Jama.Matrix;

public class NumOptAlgorithms {
	
	/**
	 * Algoritam gradijentnog spusta.
	 * @param function Funkcija
	 * @param maxIter Maksimalan broj iteracija
	 * @param x0 Početna točka
	 * @return Minimum funkcije
	 */
	public static Matrix gradDescentAlg(IFunction function, long maxIter, Matrix x0) {
		Matrix x = x0;
		for(int i = 0; i < maxIter; i++) {
			printVector(x);
			Matrix grad = function.getGrad(x);
			if(isNullVector(grad)) {
				return x;
			}
			double lambda = optimizeLambda(function, x, grad);
			
			// x = x - lambda * grad
			x = x.minus(grad.times(lambda));
			
		}
		return null;
	}
	
	public static Matrix newtonMethodAlg(IHFunction function, long maxIter, Matrix x0) {
		Matrix x = x0;
		for(int i = 0; i < maxIter; i++) {
			printVector(x);
			Matrix grad = function.getGrad(x);
			if(isNullVector(grad)) {
				return x;
			}
			Matrix hesse = function.getHesseMatrix(x);
			//tau = -H(x)^-1 * grad 
			Matrix tau = hesse.inverse().times(grad).times(-1);
			double lambda = optimizeLambda(function, x, grad);
			
			//x = x + lambda * tau
			x = x.plus(tau.times(lambda));
			
		}
		return null;
	}
	
	private static void printVector(Matrix A) {
		for(int i = 0, n = A.getRowDimension(); i < n; i++) {
			System.out.println("x" + i + " = " + A.get(i, 0));
		}
		System.out.println();
	}
	
	private static double optimizeLambda(IFunction function, Matrix x, Matrix grad) {
		double lambda = 0.0;
		double lambdaLower = 0.0;
		double lambdaUpper = 1.0;
		
		double dThetadLambda = derivateTheta(function, grad, x, lambda);
		while(dThetadLambda < 0.0) {
			dThetadLambda = derivateTheta(function, grad, x, lambdaUpper);
			if(dThetadLambda < 0) { lambdaUpper *= 2; }
		}
		while(Math.abs(lambdaUpper - lambdaLower) > 10e-3) {
			lambda = (lambdaLower + lambdaUpper) / 2;
			dThetadLambda = derivateTheta(function, grad, x, lambda);
			if(Math.abs(dThetadLambda) < 10e-3) {
				break;
			} else if (dThetadLambda > 0) {
				lambdaUpper = lambda;
			} else if(dThetadLambda < 0) {
				lambdaLower = lambda;
			}
		}
		return lambda;
	}
	
	private static double derivateTheta(IFunction function, Matrix grad, Matrix x, double lambda) {
		// xShift = x - lambdaUpper * grad
		Matrix xShift = x.minus(grad.times(lambda));		// N x 1
		Matrix gradShift = function.getGrad(xShift);	// N x 1
		
		// (1 x N) * (N x 1) = (1 x 1)
		Matrix dThetadLambda = gradShift.times(-1).transpose().times(grad); //scalar
		return dThetadLambda.get(0, 0);
	}
	
	private static boolean isNullVector(Matrix vector) {
		int n = vector.getRowDimension();
		boolean isNullVector = true;
		for(int i = 0; i < n; i++) {
			if(Math.abs(vector.get(i, 0)) > 10e-5) {
				isNullVector = false;
			}
		}
		return isNullVector;
	}
}
