package hr.fer.zemris.optjava.dz10;

public class MOOP {
	
	private static double sigmaShare = 0.5;
	private static double alpha = 2;
	private static String method = "objective-space";
	
	public static void main(String[] args) {
		
		if(args.length != 3) {
			throw new RuntimeException("Number of arguments must be 3");
		}
		MOOPProblem problem = parseProblem(args[0]);
		int populationSize = Integer.parseInt(args[1]);
		int maxiter = Integer.parseInt(args[2]);
		int methodType = parseMethod(method);
		
		NSGAII alg = new NSGAII(problem, populationSize, maxiter, sigmaShare, alpha, methodType);
		alg.run();
	}
	
	private static int parseMethod(String method) {
		if(method.equals("decision-space")) {
			return 1;
		} else if (method.equals("objective-space")) {
			return 2;
		} else {
			throw new IllegalArgumentException("Unkown method.");
		}
	}
	
	private static MOOPProblem parseProblem(String s) {
		if(s.equals("1")) {
			return new ProblemB();
		} else if (s.equals("2")) {
			return new ProblemA();
		} else {
			throw new IllegalArgumentException("First argument must 1 or 2.");
		}
	}
}
