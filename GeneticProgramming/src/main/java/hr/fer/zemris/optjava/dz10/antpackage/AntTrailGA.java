package hr.fer.zemris.optjava.dz10.antpackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.WindowConstants;


public class AntTrailGA {
	
	private static int[][] worldMatrix;
	private static int populationSize;
	private static int tournamentSize = 5;
	private static double mutationProbability = 0.09;
	private static int maxTreeDepth = 7;
	private static int maxIter;
	private static double minFit;
	private static String outputFile;
	private static String worldFilePath = "SantaFeAntTrail.txt";

	public static void main(String[] args) throws Exception{
		parseInputArgs(args);
		parseWorldFile();
		
		World world = new World(worldMatrix);
		GeneticAlgorithm alg = new GeneticAlgorithm(populationSize, maxTreeDepth, maxIter, 200, tournamentSize, mutationProbability, world, 600, minFit);
		Ant ant = alg.run();
		Ant newAnt = ant.duplicate();
		newAnt.setActionsLeft(600);
		output(ant);
		
		AntTrailFrame window = new AntTrailFrame(worldMatrix, newAnt);
        window.setLocation(200, 200);
        window.setSize(700, 700);
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setTitle("Ant Trail Frame");

	}
	
	private static void output(Ant ant) {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outputFile)))) {
			bw.write(ant.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void parseInputArgs(String[] args) {
		if(args.length != 5) {
			System.out.println("Number of args must be 5");
			System.exit(0);
		}
		worldFilePath = args[0];
		maxIter = Integer.parseInt(args[1]);
		populationSize = Integer.parseInt(args[2]);
		minFit = Double.parseDouble(args[3]);
		outputFile = args[4];
		
	}
	
	private static void parseWorldFile() {
		try(BufferedReader br = new BufferedReader(new FileReader(new File(worldFilePath)))) {
			String line;
			int currentRow = 0;
			while((line = br.readLine()) != null) {
				if(currentRow == 0) {
					String[] sizes = line.split("x");
					int rows = Integer.parseInt(sizes[0].trim());
					int columns = Integer.parseInt(sizes[1].trim());
					worldMatrix = new int[rows][columns];
					++currentRow;
				} else {
					char[] c = line.toCharArray();
					for(int column = 0; column < worldMatrix[currentRow - 1].length; column++) {
						worldMatrix[currentRow - 1][column] = c[column] == '1' ? 1 : 0;
					}
					currentRow++;
				}
			}
		} catch (IOException e) {
			System.out.println("Ne mogu naci world file.");
			System.exit(0);
		}
	}

}
