package hr.fer.zemris.optjava.dz8.function;

public class HyperbolicTangentTransferFunction implements ITransferFunction{

	public double valueAt(double net) {
		return (1 - Math.exp(-net)) / (1 + Math.exp(-net));
	}

}
