package hr.fer.zemris.optjava.dz10.test;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.optjava.dz10.alg.GeneticAlgorithm;
import hr.fer.zemris.optjava.dz10.err.MeanSquaredError;
import hr.fer.zemris.optjava.dz10.tree.Tree;

public class Test {
	
	private static double[][] input;
	private static String[] operations;
	private static double[] constRange = null;
	private static int populationSize;
	private static int tournamentSize;
	private static int costEvaluations;
	private static double mutationProbability;
	private static int maxTreeDepth;
	private static int numberOfVariables;
	private static String inputFile = "1D-const.txt";
	private static int stagnationIter = 500;
	
	public static void main(String[] args) {
		
		parseInputFile();
		parseConfFile();
		int maxChildren = 200;
		MeanSquaredError error = new MeanSquaredError(input, numberOfVariables);
		GeneticAlgorithm alg = new GeneticAlgorithm(populationSize, maxTreeDepth, maxTreeDepth, numberOfVariables, costEvaluations, 
				maxChildren, operations, constRange, error, tournamentSize, mutationProbability, stagnationIter);
		Tree sol = alg.run();
		sol.printSymbolic();
		System.out.println();
	}
	
	private static void parseInputFile() {
		List<String> lines = new LinkedList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(new File(inputFile)))) {
			String line;
			while((line = br.readLine()) != null) {
				if(line.startsWith("x")) { 
					numberOfVariables = line.split("\\s+").length - 1;
					continue;
				}
				lines.add(line);
			}
		} catch (IOException e) {
			System.out.println("Ne mogu naci file.");
			System.exit(0);
		}
		
		input = new double[lines.size()][numberOfVariables + 1];
		int j = 0;
		for(String line : lines) {
			String[] split = line.split("\\s+");
			for(int i = 0; i < numberOfVariables + 1; i++) {
				input[j][i] = Double.parseDouble(split[i].trim());
			}
			++j;
		}
	}
	
	private static void parseConfFile() {
		try (BufferedReader br = new BufferedReader(new FileReader(new File("conf.txt")))){
			String line;
			while((line = br.readLine()) != null) {
				if(line.contains("FunctionNodes")) {
					String[] split = line.split(":");
					operations = split[1].split(",");
					for(int i = 0; i < operations.length; i++) {
						operations[i] = operations[i].trim();
					}
				} else if(line.contains("ConstantRange")) {
					String[] split = line.split(":");
					if(split[1].trim().contains("N/A")) {
						continue;
					}
					constRange = new double[2];
					split = split[1].split(",");
					constRange[0] = Double.parseDouble(split[0].trim());
					constRange[1] = Double.parseDouble(split[1].trim());					
				} else if(line.contains("PopulationSize")) {
					String[] split = line.split(":");
					populationSize = Integer.parseInt(split[1].trim());
				} else if(line.contains("TournamentSize")) {
					String[] split = line.split(":");
					tournamentSize = Integer.parseInt(split[1].trim());
				} else if(line.contains("CostEvaluations")) {
					String[] split = line.split(":");
					costEvaluations = Integer.parseInt(split[1].trim());
				} else if(line.contains("MutationProbability")) {
					String[] split = line.split(":");
					mutationProbability = Double.parseDouble(split[1].trim());
				} else if(line.contains("MaxTreeDepth")) {
					String[] split = line.split(":");
					maxTreeDepth = Integer.parseInt(split[1].trim());
				}
			}
		} catch (IOException e) {
			System.out.println("Ne mogu naci file.");
			System.exit(0);
		}
	}
}

