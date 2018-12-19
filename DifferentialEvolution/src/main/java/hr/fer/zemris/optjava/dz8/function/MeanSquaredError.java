package hr.fer.zemris.optjava.dz8.function;

import hr.fer.zemris.optjava.dz8.function.IErrorFunction;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz8.nn.NeuralNetwork;

public class MeanSquaredError implements IErrorFunction{
	
	private NeuralNetwork nn;
	
	public MeanSquaredError(NeuralNetwork nn) {
		this.nn = nn;
	}

	public double value(double[] weights) {
		IReadOnlyDataSet dataset = nn.getDataset();
		int N = dataset.numberOfSamples();
		int m = dataset.numberOfOutputs();
		double error = 0.0;
		for(int s = 0; s < N; s++) {
			double[] inputs = dataset.getInputSample(s);
			double[] outputTrue = dataset.getOutputSample(s);
			double[] outputCalc = new double[m];
			nn.calcOutputs(inputs, weights, outputCalc);
			for(int o = 0; o < m; o++) {
				error += Math.pow(outputTrue[o] - outputCalc[o], 2);
			}
		}
		error = error / N;
		nn.resetContext();
		return error;
	}
	
	public int getDimension() {
		return nn.getWeightsCount();
	}

}
