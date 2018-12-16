package hr.fer.zemris.optjava.dz8.function;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz8.nn.TDNN;

public class MeanSquaredError implements IErrorFunction{
	
	private TDNN tdnn;
	
	public MeanSquaredError(TDNN tdnn) {
		this.tdnn = tdnn;
	}

	public double value(double[] weights) {
		IReadOnlyDataSet dataset = tdnn.getDataset();
		int N = dataset.numberOfSamples();
		int m = dataset.numberOfOutputs();
		double error = 0.0;
		for(int s = 0; s < N; s++) {
			double[] inputs = dataset.getInputSample(s);
			double[] outputTrue = dataset.getOutputSample(s);
			double[] outputCalc = new double[m];
			tdnn.calcOutputs(inputs, weights, outputCalc);
			for(int o = 0; o < m; o++) {
				error += Math.pow(outputTrue[o] - outputCalc[o], 2);
			}
		}
		error = error / N;
		return error;
	}
	
	public int getDimension() {
		return tdnn.getWeightsCount();
	}

}
