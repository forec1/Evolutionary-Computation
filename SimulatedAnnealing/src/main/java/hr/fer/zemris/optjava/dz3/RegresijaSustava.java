package hr.fer.zemris.optjava.dz3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import hr.fer.zemris.optjava.dz3.algorithm.SimulatedAnnealing;
import hr.fer.zemris.optjava.dz3.decoder.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoder.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.function.ErrorFunction;
import hr.fer.zemris.optjava.dz3.neighborhood.BitVectorNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayNormNeighborhood;
import hr.fer.zemris.optjava.dz3.neighborhood.DoubleArrayUnifNeighborhood;
import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.solution.DoubleArraySolution;
import hr.fer.zemris.optjava.dz3.tempschedule.GeometricTempSchedule;

public class RegresijaSustava {

	public static void main(String[] args) throws IOException {
		if(args.length != 2) {
			System.err.println("Main should recieve 2 command line arguments");
			System.exit(0);
		}
		
		String sPath = args[0];
		BufferedReader br = new BufferedReader(new FileReader(new File(sPath)));
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
		ErrorFunction error = new ErrorFunction(systemMatrix, Y, 20);
		double[] deltas = new double[6];
		double[] mins = new double[6];
		double[] maxs = new double[6];
		Arrays.fill(deltas,	0.1);
		deltas[4] = 0.01;
		Arrays.fill(mins, -5);
		Arrays.fill(maxs, 5);
		if(args[1].equals("decimal")) {
			SimulatedAnnealing<DoubleArraySolution> alg = new SimulatedAnnealing<DoubleArraySolution>(
					new PassThroughDecoder(), 
					new DoubleArrayNormNeighborhood(deltas), 
					new DoubleArraySolution(6).randomize(new Random(System.currentTimeMillis()), mins, maxs), 
					error, 
					new GeometricTempSchedule(0.99, 10000, 2000, 3*1600), 
					true);
			DoubleArraySolution solution = alg.run();
			System.out.println("rjesenje: " + solution);
		} else if(args[1].startsWith("binary")) {
			String[] split = args[1].split(":");
			int numOfBits = Integer.parseInt(split[1]);
			BitVectorSolution startWith = new BitVectorSolution(6*numOfBits);
			startWith.randomize(new Random());
			NaturalBinaryDecoder decoder = new NaturalBinaryDecoder(-3.2, 7.2, numOfBits, 6);
			SimulatedAnnealing<BitVectorSolution> alg = new SimulatedAnnealing<BitVectorSolution>(
					decoder,
					new BitVectorNeighborhood(6), 
					startWith, 
					error, 
					new GeometricTempSchedule(0.99, 15000, 2000, 3*1600), 
					true);
			BitVectorSolution solution = alg.run();
			System.out.print("rj = ");
			for(double d : decoder.decode(solution)) {
				System.out.print("["+d+"] ");
			}
		}
		br.close();
	}

}
