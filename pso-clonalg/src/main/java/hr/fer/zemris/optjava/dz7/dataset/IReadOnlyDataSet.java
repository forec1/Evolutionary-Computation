package hr.fer.zemris.optjava.dz7.dataset;

public interface IReadOnlyDataSet {

	public int numberOfSamples();
	public int numberOfInputs();
	public int numberOfOutputs();
	public String getSample(int index);
	public double[] getInputSample(int index);
	public double[] getOutputSample(int index);
}
