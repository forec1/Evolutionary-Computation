package hr.fer.zemris.optjava.dz7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.alg.Algorithm;
import hr.fer.zemris.optjava.dz7.alg.PSOAlgorithm;
import hr.fer.zemris.optjava.dz7.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz7.dataset.IrisFlowerDataSet;
import hr.fer.zemris.optjava.dz7.function.ITransferFunction;
import hr.fer.zemris.optjava.dz7.function.MeanSquaredError;
import hr.fer.zemris.optjava.dz7.function.SigmoidTransferFunction;
import hr.fer.zemris.optjava.dz7.nn.FFANN;

public class ANNTrainer {

	public static void main(String[] args) throws IOException {
		if(args.length != 5) {
			throw new RuntimeException("Number of arguments must be 5!");
		}
		
		String alg = args[1];
		int d = parseAlgType(alg);
		int n = Integer.parseInt(args[2]);
		double merr = Double.parseDouble(args[3]);
		int maxiter = Integer.parseInt(args[4]);
		
		IReadOnlyDataSet dataset = loadData(args[0]);
		System.out.println("Imamo uzoraka za ucenje: " + dataset.numberOfSamples());
		FFANN ffann = new FFANN(
					new int[] {4,3,3},
					new ITransferFunction[] {
							new SigmoidTransferFunction(),
							new SigmoidTransferFunction(),
							new SigmoidTransferFunction()
							//new LinearTransferFunction()
					},
					dataset);		
		MeanSquaredError error = new MeanSquaredError(ffann);
		Algorithm algorithm = null;
		if(alg.startsWith("pso")) {
			if(d != -1) {
				algorithm = new PSOAlgorithm(n, error, -1, 1, -5, 5, 2.0, 2.0, merr, maxiter, d);
				
			} else {
				algorithm = new PSOAlgorithm(n, error, -1, 1, -5, 5, 2.0, 2.0, merr, maxiter, n / 2);
			}
		}
		double[] solution = algorithm.run();
		printScores(ffann, dataset, solution);

	}
	
	private static int parseAlgType(String alg) {
		String[] split = alg.split("-");
		if(split[1].equals("b")) {
			return Integer.parseInt(split[2]);
		}
		return -1;
	}
	
	public static void printScores(FFANN ffann, IReadOnlyDataSet dataset, double[] solution) {
		double[] clasResults = new double[dataset.numberOfOutputs()];
		int correctCnt = 0;
		for(int i = 0, m = dataset.numberOfSamples(); i < m; i++) {
			double[] inputs = dataset.getInputSample(i);
			double[] outputs = dataset.getOutputSample(i);
			boolean isCorrect = true;
			
			ffann.calcOutputs(inputs, solution, clasResults);
			
			for(int j = 0; j < clasResults.length; j++) {
				clasResults[j] = clasResults[j] >= 0.5 ? 1.0 : 0.0;
				if(clasResults[j] != outputs[j]) {
					isCorrect = false;
				}
			}
			correctCnt = isCorrect ? correctCnt + 1 : correctCnt;
			System.out.print(i + ". calc ");
			for(double clasRes : clasResults) {
				System.out.print((int)clasRes);
			}
			System.out.print(" ~ ");
			for(double output : outputs) {
				System.out.print((int)output);
			}
			System.out.print(" true");
			System.out.println();
			
		}
		
		System.out.println("Eff: " + correctCnt + "/" + dataset.numberOfSamples());
	}
	
	public static IReadOnlyDataSet loadData(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		IrisFlowerDataSet dataset = new IrisFlowerDataSet();
		String line;
		while((line = br.readLine()) != null) {
			dataset.addSample(line);
		}
		br.close();
		return dataset;
	}

}
