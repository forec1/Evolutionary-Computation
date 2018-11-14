package hr.fer.zemris.optjava.dz5.part2;

import hr.fer.zemris.optjava.dz5.part2.function.TransportCostFunction;

/**
 * Klasa koja pokreće SASEGASA algoritam. Pri pokretnaju program treba dobiti:
 * put do datoteke, veličinu populacije, početni broj podpopulacija, compFactor, 
 * SuccRatio, maxSelPress i maksimalan broj generacija
 * @author filip
 *
 */
public class Test {

	public static void main(String[] args) {
		if(args.length != 7) {
			System.out.println("Broj argumenata treba biti 7!");
			System.exit(0);
		} 
		
		String path = args[0];
		int populationSize = Integer.parseInt(args[1]);
		int numOfSubpop = Integer.parseInt(args[2]);
		double compFactor = Double.parseDouble(args[3]);
		double succRatio = Double.parseDouble(args[4]);
		double maxSelPress = Double.parseDouble(args[5]);
		int maxGen = Integer.parseInt(args[6]);
		
		TransportCostFunction function = new TransportCostFunction(path);
		GeneticAlgorithm alg = new GeneticAlgorithm(function, populationSize, numOfSubpop, compFactor, succRatio, maxSelPress, maxGen);
		alg.run();

	}
}
