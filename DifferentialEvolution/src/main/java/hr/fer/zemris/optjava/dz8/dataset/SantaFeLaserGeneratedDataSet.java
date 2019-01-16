package hr.fer.zemris.optjava.dz8.dataset;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SantaFeLaserGeneratedDataSet implements IReadOnlyDataSet{

	private int numberOfSamples;
	private int numberOfInputs;
	List<double[]> samples;
	
	public SantaFeLaserGeneratedDataSet(int numberOfSamples, int numbeOfInputs) {
		this.numberOfSamples = numberOfSamples;
		this.numberOfInputs = numbeOfInputs;
		this.samples = new LinkedList<>();
	}
	
	public void addSample(double[] sample) {
		samples.add(sample);
	}

	@Override
	public int numberOfSamples() {
		return numberOfSamples;
	}

	@Override
	public int numberOfInputs() {
		return numberOfInputs;
	}

	@Override
	public int numberOfOutputs() {
		return 1;
	}

	@Override
	public double[] getSample(int index) {
		return samples.get(index);
	}

	@Override
	public double[] getInputSample(int index) {
		return Arrays.copyOfRange(samples.get(index), 0, numberOfInputs);
	}

	@Override
	public double[] getOutputSample(int index) {
		return Arrays.copyOfRange(samples.get(index), numberOfInputs, numberOfInputs + 1);
	}

}
