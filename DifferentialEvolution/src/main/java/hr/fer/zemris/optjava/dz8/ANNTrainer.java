package hr.fer.zemris.optjava.dz8;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.alg.PSOAlgorithm;
import hr.fer.zemris.optjava.dz8.alg.DEAlgorithm;
import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz8.dataset.SantaFeLaserGeneratedDataSet;
import hr.fer.zemris.optjava.dz8.function.HyperbolicTangentTransferFunction;
import hr.fer.zemris.optjava.dz8.function.IErrorFunction;
import hr.fer.zemris.optjava.dz8.function.ITransferFunction;
import hr.fer.zemris.optjava.dz8.function.MeanSquaredError;
import hr.fer.zemris.optjava.dz8.nn.ElmanNN;
import hr.fer.zemris.optjava.dz8.nn.NeuralNetwork;
import hr.fer.zemris.optjava.dz8.nn.TDNN;

public class ANNTrainer {

	public static void main(String[] args) throws IOException{
		
		if(args.length != 5) {
			throw new RuntimeException("Number of arguments must be 5!");
		}
		
		String net = args[1];
		int populationSize = Integer.parseInt(args[2]);
		double merr = Double.parseDouble(args[3]);
		int maxiter = Integer.parseInt(args[4]);
		
		int[] networkConfiguration = parseNet(net);
		
		IReadOnlyDataSet dataset = loadData(args[0], 1, 500);
		NeuralNetwork nn = null;
		ITransferFunction[] transferFunctions = new ITransferFunction[networkConfiguration.length];
		for(int i = 0; i < transferFunctions.length; i++) {
			transferFunctions[i] = new HyperbolicTangentTransferFunction();
		}
		if(net.startsWith("tdnn")) {
			nn = new TDNN(networkConfiguration, transferFunctions, dataset);
		} else if (net.startsWith("elman")) {
			nn = new ElmanNN(networkConfiguration, transferFunctions, dataset);
		} else {
			throw new RuntimeException("Invalid net!");
		}
		IErrorFunction error = new MeanSquaredError(nn);
		double[] bL = new double[error.getDimension()];
		double[] bU = new double[error.getDimension()];
		Arrays.fill(bU, 5);
		Arrays.fill(bL, -5);
		double F = 0.5;
		double Cr = 0.7;
		DEAlgorithm alg = new DEAlgorithm(error, populationSize, maxiter, merr, bL, bU, F, Cr);
		double[] solution = alg.run();
		System.out.println("KRAJ");
		printScores(nn, dataset, solution);
	}
	
	private static int[] parseNet(String net) {
		String[] layers = net.split("-")[1].split("x");
		int[] netConfig = new int[layers.length];
		for(int i = 0; i < layers.length; i++) {
			netConfig[i] = Integer.parseInt(layers[i].trim());
		}
		return netConfig;		
	}
	
	public static void printScores(NeuralNetwork nn, IReadOnlyDataSet dataset, double[] solution) {
		double[] clasResults = new double[dataset.numberOfOutputs()];
		for(int i = 0, m = dataset.numberOfSamples(); i < m; i++) {
			double[] inputs = dataset.getInputSample(i);
			double[] outputs = dataset.getOutputSample(i);
			
			nn.calcOutputs(inputs, solution, clasResults);
			System.out.print(i + ". calc ");
			for(double clasRes : clasResults) {
				System.out.print(clasRes);
			}
			System.out.print(" ~ ");
			for(double output : outputs) {
				System.out.print(output);
			}
			System.out.print(" true");
			System.out.println();
			
		}
	}
	
	private static IReadOnlyDataSet loadData(String path, int l, int a) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		List<Double> data = new LinkedList<>();
		String line;
		while((line = br.readLine()) != null) {
			data.add(Double.parseDouble(line.trim()));
		}
		if(a == 0 || a < -1) {
			br.close();
			throw new IndexOutOfBoundsException("Parameter a can't be 0 or less than -1");
		}
		a = a > data.size() ? -1 : a;
		a = a == -1 ? data.size() : a;
		if(l < 1 || l >= a) {
			br.close();
			throw new IndexOutOfBoundsException("Parameter l can't be less than 1 or higher or equals than " + a + "!");
		}
		double[] normalizedData = normalize(data);
		SantaFeLaserGeneratedDataSet dataset = new SantaFeLaserGeneratedDataSet(a-l, l);
		for(int i = 0; i < a - l + 1; i++) {
			dataset.addSample(Arrays.copyOfRange(normalizedData, i, i + l + 1));
		}
		br.close();
		return dataset;
	}
	
	/**
	 * Normalizes the data to the range (-1, +1) and returns them
	 * in a new list.
	 * @param data Data to be normalized.
	 * @return Normalized data.
	 */
	private static double[] normalize(List<Double> data) {
		double[] normalizedData = new double[data.size()];
		double max = data.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
		double min = data.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
		int i = 0;
		for(Double valueBeforeNormalization : data) {
			double valueAfterNormalization = 2 * ((valueBeforeNormalization - min) / (max - min)) - 1;
			normalizedData[i++] = valueAfterNormalization;
		}
		return normalizedData;
	}

}

















