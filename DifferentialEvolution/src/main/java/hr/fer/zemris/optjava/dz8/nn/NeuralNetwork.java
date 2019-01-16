package hr.fer.zemris.optjava.dz8.nn;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataSet;

public interface NeuralNetwork {
	
	public void calcOutputs(double[] inputs, double[] weights, double[] outputs);
	public IReadOnlyDataSet getDataset();
	public int getWeightsCount();
	public void resetContext();
}
