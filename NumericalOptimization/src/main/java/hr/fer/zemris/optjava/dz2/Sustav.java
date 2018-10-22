package hr.fer.zemris.optjava.dz2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import Jama.Matrix;

public class Sustav {
	
	public static void main(String[] args) throws IOException {
		if(args.length != 3) {
			System.err.println("Number of arguments is not 3!");
			System.exit(0);
		}
		
		String method = args[0];
		long maxIter = Long.parseLong(args[1]);
		String sPath = args[2];
		
		File file = new File(sPath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		double[][] system = new double[10][10];
		String line;
		int i = 0;
		while((line = br.readLine()) != null) {
			if(line.startsWith("#")) { continue; }
			String[] ns = line.substring(1, line.length()-1).split(", ");
			for(int j = 0; j < 10; j++) {
				system[i][j] = Double.parseDouble(ns[j]);
			}
			i++;
		}
		br.close();
		Matrix systemMatrix = new Matrix(system);
		
	}
}
