package hr.fer.zemris.optjava.dz4.part1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import hr.fer.zemris.optjava.dz4.function.ErrorFunction;

public class Main {

	public static void main(String[] args) throws IOException {
		if(args.length != 5) {
			System.err.println("Number  of arguments must be 5!");
			System.exit(0);
		}
		int populationSize = Integer.parseInt(args[0]);
		double minError = Double.parseDouble(args[1]);
		int maxNumberOfGenerations = Integer.parseInt(args[2]);
		String selection = args[3];
		double sigma = Double.parseDouble(args[4]);
		ErrorFunction error = parseErrorFunction("./02-zad-prijenosna.txt");
		GeneticAlgorithm alg = new GeneticAlgorithm(
				error, 
				populationSize, 
				selection, 
				minError, 
				maxNumberOfGenerations,
				sigma);
		alg.run();
	}
	
	private static ErrorFunction parseErrorFunction(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		double[][] systemMatrix = new double[20][5];
		double[] Y = new double[20];
		int i = 0;
		while((line = br.readLine()) != null) {
			if(line.startsWith("#")) { continue; }
			line = line.substring(1, line.length()-1);
			String[] split = line.split(", ");
			for(int j = 0; j < 5; j++) {
				systemMatrix[i][j] = Double.parseDouble(split[j]);
			}
			Y[i] = Double.parseDouble(split[5]);
			i++;
		}
		br.close();
		return new ErrorFunction(systemMatrix, Y, 20);
	}

}
