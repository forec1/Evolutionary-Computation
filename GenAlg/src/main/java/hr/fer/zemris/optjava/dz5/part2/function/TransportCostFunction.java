package hr.fer.zemris.optjava.dz5.part2.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TransportCostFunction implements IFunction{
	
	private int n;
	private int[][] distanceMatrix;
	private int[][] costMatrix;
	
	public TransportCostFunction(String pathToFile) {
		try {
			parse(pathToFile);
		} catch (IOException e) {
			throw new RuntimeException("File not found!");
		}
	}
	
	private void parse(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		String line;
		line = br.readLine();
		n = Integer.parseInt(line.trim());
		distanceMatrix = new int[n][n];
		costMatrix = new int[n][n];
		br.readLine();
		
		//distance matrix
		int lineNumber = 0;
		while(!(line = br.readLine()).isEmpty()) {
			fillMatrixLine(distanceMatrix, line, lineNumber);
			lineNumber++;
		}
		//costMatrix
		lineNumber = 0;
		while((line = br.readLine()) != null && !line.isEmpty()) {
			fillMatrixLine(costMatrix, line, lineNumber);
			lineNumber++;
		}
		br.close();
	}
	
	private void fillMatrixLine(int[][] matrix, String line, int lineNumber) {
		String[] split = line.trim().split("\\s+");
		for(int i = 0; i < n; i++) {
			matrix[lineNumber][i] = Integer.parseInt(split[i].trim());
		}
		lineNumber++;
	}

	@Override
	public long valueAt(int[] x) {
		long value = 0;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				value += costMatrix[i][j] * distanceMatrix[x[i] - 1][x[j] - 1];
			}
		}
		return value;
	}

	@Override
	public int getNumOfVars() {
		return n;
	}
}
