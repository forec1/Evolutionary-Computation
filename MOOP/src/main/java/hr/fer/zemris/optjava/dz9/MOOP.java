package hr.fer.zemris.optjava.dz9;

public class MOOP {

	public static void main(String[] args) {
		
		NSGA alg = new NSGA(new ProblemA(),	20, 2000, 0.5, 2);
		alg.run();
	}

}
