package hr.fer.zemris.optjava.dz7.nn;

import hr.fer.zemris.optjava.dz7.function.ITransferFunction;

public class Neuron {
	
	private ITransferFunction transferFunction;
	private int weightsStartIndex;
	private int inputsStartIndex;
	private int numberOfInputs;
	private int outputIndex;
	private boolean inputLayer;

	public Neuron(ITransferFunction transferFunction, int weightsStartIndex, int inputsStartIndex, int numberOfInputs,
			int outputIndex, boolean inputLayer) {
		super();
		this.transferFunction = transferFunction;
		this.weightsStartIndex = weightsStartIndex;
		this.inputsStartIndex = inputsStartIndex;
		this.numberOfInputs = numberOfInputs;
		this.outputIndex = outputIndex;
		this.inputLayer = inputLayer;
	}

	private double calculateNet(double[] weights, double[] inputsAndOutputs) {
		double net = 0.0;
		for(int i = weightsStartIndex, j = inputsStartIndex; i < weightsStartIndex + numberOfInputs; i++, j++) {
			net += weights[i] * inputsAndOutputs[j];
		}
		net += weights[weightsStartIndex + numberOfInputs];
		return net;
	}
	
	public double[] calcOutput(double[] weights, double[] inputsAndOutputs) {
		if(inputLayer) { return inputsAndOutputs; }
		double net = calculateNet(weights, inputsAndOutputs);
		double output = transferFunction.valueAt(net);
		inputsAndOutputs[outputIndex] = output;
		return inputsAndOutputs;
	}
	
	
}
