package hr.fer.zemris.optjava.dz8.dataset;

public interface IReadOnlyDataSet {

	public int numberOfSamples();
	public int numberOfInputs();
	public int numberOfOutputs();
	public double[] getSample(int index);
	public double[] getInputSample(int index);
	public double[] getOutputSample(int index);
}
