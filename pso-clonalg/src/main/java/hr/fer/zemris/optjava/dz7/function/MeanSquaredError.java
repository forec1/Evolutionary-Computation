package hr.fer.zemris.optjava.dz7.function;

import hr.fer.zemris.optjava.dz7.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz7.nn.FFANN;

public class MeanSquaredError implements IErrorFunction{
	
	private FFANN ffann;
	
	public MeanSquaredError(FFANN ffan) {
		this.ffann = ffan;
	}

	public double value(double[] weights) {
		IReadOnlyDataSet dataset = ffann.getDataset();
		int N = dataset.numberOfSamples();
		int m = dataset.numberOfOutputs();
		double error = 0.0;
		for(int s = 0; s < N; s++) {
			double[] inputs = dataset.getInputSample(s);
			double[] outputTrue = dataset.getOutputSample(s);
			double[] outputCalc = new double[m];
			ffann.calcOutputs(inputs, weights, outputCalc);
			for(int o = 0; o < m; o++) {
				error += Math.pow(outputTrue[o] - outputCalc[o], 2);
			}
		}
		error = error / N;
		return error;
	}
	
	public int getDimension() {
		return ffann.getWeightsCount();
	}

}
