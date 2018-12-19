package hr.fer.zemris.optjava.dz8.nn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.optjava.dz8.dataset.IReadOnlyDataSet;
import hr.fer.zemris.optjava.dz8.function.ITransferFunction;

public class ElmanNN implements NeuralNetwork{
	
	private int[] networkConfiguration;
	private ITransferFunction[] transferFunctions;
	private IReadOnlyDataSet dataset;
	private int numberOfWeights;
	private List<Neuron> neurons;
	
	public ElmanNN(int[] networkConfiguration, ITransferFunction[] transferFunctions, IReadOnlyDataSet dataset) {
		if(networkConfiguration[0] != dataset.numberOfInputs()) {
			throw new RuntimeException("Input layer must have " + dataset.numberOfInputs() + " neurons");
		}
		if(networkConfiguration[networkConfiguration.length - 1] != dataset.numberOfOutputs()) {
			throw new RuntimeException("Output layer must have " + dataset.numberOfOutputs() + " neurons");
		}
		this.networkConfiguration = networkConfiguration;
		this.transferFunctions = transferFunctions;
		this.dataset = dataset;
		this.neurons = new LinkedList<>();
		calcNumberOfWeights();
		createNetwork();
	}
	
	private void createNetwork() {
		int wOffset = 0;
		int iOffset = 0;
		int oOffset = 0;
		for(int i = 0, n = networkConfiguration.length; i < n; i++) {
			int weightsStartIndex = wOffset;
			int inputsStartIndex = iOffset;
			int numberOfInputs = 0;
			boolean isInputLayer = true;
			ITransferFunction transferFunction = transferFunctions[i];
			//creating layers
			for(int j = 0; j < networkConfiguration[i]; j++) {
				weightsStartIndex = wOffset;
				inputsStartIndex = iOffset;
				isInputLayer = true;
				transferFunction = transferFunctions[i];
				if(i > 1) {
					numberOfInputs = networkConfiguration[i - 1];
					wOffset += numberOfInputs;
					wOffset += 1;	//tezina samo za taj neuron, ako nije u ulaznom sloju
//					transferFunction = transferFunctions[i - 1];
					isInputLayer = false;
				} else if(i == 1) {
					numberOfInputs = networkConfiguration[i - 1] + networkConfiguration[i];
					wOffset += numberOfInputs;
					isInputLayer = false;
				}
				neurons.add(new Neuron(transferFunction, weightsStartIndex, inputsStartIndex,
						numberOfInputs, oOffset, isInputLayer, false));
				oOffset++;				
			}
			//creating context layer
			if(i == 0) {
				for(int j = 0; j < networkConfiguration[1]; j++) {
					neurons.add(new Neuron(transferFunction, 0, 0, 0, oOffset, false, true));
					oOffset++;
				}
			}
			if(i != 0) {
				iOffset += networkConfiguration[i - 1];
			}
		}
	}
	
	public void setContextOutputs(double[] contextOutputs) {
		for(int i = networkConfiguration[0], j = 0; i < networkConfiguration[0] + networkConfiguration[1]; i++, j++) {
			Neuron contextNeuron = neurons.get(i);
			contextNeuron.setOutput(contextOutputs[j]);
		}
		
	}
	
	private boolean isContextUp() {
		for(Neuron neuron : neurons) {
			if(neuron.isContextLayer()) {
				if(neuron.getOutput() != 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void calcOutputs(double[] inputs, double[] weights, double[] outputs) {
		if(weights.length != numberOfWeights) {
			throw new IllegalArgumentException("Number of weights must be " + numberOfWeights + 
					" and not " + weights.length);
		}
		if(!isContextUp()) {
			double[] contextOutput = Arrays.copyOfRange(weights, weights.length - networkConfiguration[1], weights.length);
			setContextOutputs(contextOutput);
		}
		double[] inputsAndOutputs = new double[getNumberOfNeurons()];
		for(int i = 0, n = inputs.length; i < n; i++) {
			inputsAndOutputs[i] = inputs[i];
		}
		
		for(Neuron neuron : neurons) {
			inputsAndOutputs = neuron.calcOutput(weights, inputsAndOutputs);
		}
		
		int start = networkConfiguration[0] + networkConfiguration[1];
		int end = start + networkConfiguration[1];
		for(int i = start, j = networkConfiguration[0]; i < end; i++, j++) {
			Neuron neuron = neurons.get(i);
			Neuron contextNeuron = neurons.get(j);
			contextNeuron.setOutput(inputsAndOutputs[neuron.getOutputIndex()]);
		}
		
		int lastLayer = dataset.numberOfOutputs();
		for(int n = inputsAndOutputs.length, i = n - lastLayer, j = 0; i < n; i++, j++) {
			outputs[j] = inputsAndOutputs[i];
		}
		
	}
	
	@Override
	public void resetContext() {
		for(Neuron neuron : neurons) {
			if(neuron.isContextLayer()) {
				neuron.setOutput(0);
			}
		}
	}
	
	public IReadOnlyDataSet getDataset() {
		return dataset;
	}
	
	private int getNumberOfNeurons() {
		int numOfOutputs = 0;
		for(int inLayer : networkConfiguration) {
			numOfOutputs += inLayer;
		}
		numOfOutputs += networkConfiguration[1];
		return numOfOutputs;
	}
	
	public int getWeightsCount() {
		return numberOfWeights;
	}
	
	private void calcNumberOfWeights() {
		numberOfWeights = 0;
		for(int i = 0, n = networkConfiguration.length - 1; i < n; i++) {
			numberOfWeights += networkConfiguration[i] * networkConfiguration[i + 1];
			numberOfWeights += networkConfiguration[i + 1];
		}
		numberOfWeights += networkConfiguration[1] * networkConfiguration[1];
		numberOfWeights += networkConfiguration[1];
	}
}
