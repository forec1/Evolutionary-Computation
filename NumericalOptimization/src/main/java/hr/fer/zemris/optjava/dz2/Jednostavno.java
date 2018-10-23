package hr.fer.zemris.optjava.dz2;

import java.util.Random;

import Jama.Matrix;

public class Jednostavno {

	public static void main(String[] args) {
		
		String problem = args[0];
		long maxIter = Long.parseLong(args[1]);
		
		double x1, x2;
		if(args.length == 4) {
			x1 = Double.parseDouble(args[2]);
			x2 = Double.parseDouble(args[3]);
		}else {
			x1 = (new Random()).nextDouble() * 10 - 5;
			x2 = (new Random()).nextDouble() * 10 - 5;
		}
		double[][] A = {{x1}, {x2}};
		Matrix x0 = new Matrix(A);
		if(problem.startsWith("1")) {
			Function1 f1 = new Function1();
			if(problem.equals("1a")) {
				NumOptAlgorithms.gradDescentAlg(f1, maxIter, x0, Jednostavno.class);
			} else if (problem.equals("1b")) {
				NumOptAlgorithms.newtonMethodAlg(f1, maxIter, x0, Jednostavno.class);
			}
		} else if(problem.startsWith("2")) {
			Function2 f2 = new Function2();
			if(problem.equals("2a")) {
				NumOptAlgorithms.gradDescentAlg(f2, maxIter, x0, Jednostavno.class);
			} else if(problem.equals("2b")) {
				NumOptAlgorithms.newtonMethodAlg(f2, maxIter, x0, Jednostavno.class);
			}
		}
	}

}