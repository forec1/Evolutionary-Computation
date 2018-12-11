package hr.fer.zemris.optjava.dz7.dataset;

import java.util.ArrayList;
import java.util.List;

public class IrisFlowerDataSet implements IReadOnlyDataSet{
	
	private List<String> samples;
	
	public IrisFlowerDataSet() {
		this.samples = new ArrayList<String>();
	}
	
	public void addSample(String sample) {
		samples.add(sample);
	}
	
	public int numberOfSamples() {
		return samples.size();
	}

	public int numberOfInputs() {
		return 4;
	}

	public int numberOfOutputs() {
		return 3;
	}

	public String getSample(int index) {
		return samples.get(index);
	}

	public double[] getInputSample(int index) {
		String input = samples.get(index).split(":")[0];
		input = input.replace("(", "");
		input = input.replace(")", "");
		String[] stringSizes = input.split(",");
		double[] sizes = new double[stringSizes.length];
		for(int i = 0, n = stringSizes.length; i < n; i++) {
			sizes[i] = Double.parseDouble(stringSizes[i]);
		}
		return sizes;
	}

	public double[] getOutputSample(int index) {
		String output = samples.get(index).split(":")[1];
		output = output.replace("(", "");
		output = output.replace(")", "");
		String[] stringFlags = output.split(",");
		double[] flags = new double[stringFlags.length];
		for(int i = 0, n = stringFlags.length; i < n; i++) {
			flags[i] = Double.parseDouble(stringFlags[i]);
		}
		return flags;
	}

}
