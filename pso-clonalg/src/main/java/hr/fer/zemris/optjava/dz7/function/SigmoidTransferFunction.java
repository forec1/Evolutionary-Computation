package hr.fer.zemris.optjava.dz7.function;

public class SigmoidTransferFunction implements ITransferFunction{

	public double valueAt(double net) {
		return 1.0 / (1.0 + Math.exp(-net));
	}
}
