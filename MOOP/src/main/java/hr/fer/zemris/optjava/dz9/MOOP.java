package hr.fer.zemris.optjava.dz9;

public class MOOP {

	public static void main(String[] args) {
		
		if(args.length != 6) {
			throw new RuntimeException("Number of arguments must be 6");
		}
		MOOPProblem problem = parseProblem(args[0]);
		int populationSize = Integer.parseInt(args[1]);
		String method = args[2];
		int maxiter = Integer.parseInt(args[3]);
		double sigmaShare = Double.parseDouble(args[4]);
		double alpha = Double.parseDouble(args[5]);
		int methodType = parseMethod(method);
		
		NSGA alg = new NSGA(problem, populationSize, maxiter, sigmaShare, alpha, methodType);
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
