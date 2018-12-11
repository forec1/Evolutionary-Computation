package hr.fer.zemris.optjava.dz7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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
		int n = Integer.parseInt(args[1]);
		double merr = Double.parseDouble(args[2]);
		int maxiter = Integer.parseInt(args[3]);
		
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
		int numberOfWeights = ffann.getWeightsCount();
		double[] weights = new double[numberOfWeights];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = 0.1;
		}
		MeanSquaredError error = new MeanSquaredError(ffann);
		System.out.println("error: " + error.value(weights));

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
