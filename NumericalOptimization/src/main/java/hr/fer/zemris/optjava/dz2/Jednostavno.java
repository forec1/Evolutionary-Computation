package hr.fer.zemris.optjava.dz2;

import java.util.Random;

import Jama.Matrix;

public class Jednostavno {

	public static void main(String[] args) {
		
		Function1 f1 = new Function1();
		double x1 = (new Random()).nextDouble() * 10 - 5;
		double x2 = (new Random()).nextDouble() * 10 - 5;
		double[][] A = {{x1}, {x2}};
		Matrix gd = new Matrix(A);
		Matrix nm = new Matrix(A);
		
		
		NumOptAlgorithms.gradDescentAlg(f1, 100000, gd);
		NumOptAlgorithms.newtonMethodAlg(f1, 100000, nm);
	}

}
