package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public class NaturalBinaryDecoder extends BitVectorDecoder{
	
	public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		super(mins, maxs, bits, n);
	}

	public NaturalBinaryDecoder(double min, double max, int bit, int n) {
		super(min, max, bit, n);
	}

	@Override
	public double[] decode(BitVectorSolution x) {
		BitVectorSolution solution = x.duplicate();
		double[] decodedSolution = new double[n];
		int offset = 0;
		for(int i = 0; i < this.n; i++) {
			int val = 0;
			for(int j = 0; j < this.bits[i]; j++) {
				val += solution.bits[j + offset] * Math.pow(2, this.bits[i] - j - 1);
			}
			decodedSolution[i] = this.mins[i] + val / (Math.pow(2, this.bits[i]) - 1) * (this.maxs[i]- this.mins[i]);
			offset += this.bits[i];
		}
		
		return decodedSolution;
	}

	@Override
	public void decode(BitVectorSolution x, double[] d) {
		double[] decodedSolution = decode(x);
		for(int i = 0, stop = decodedSolution.length; i < stop; i++) {
			d[i] = decodedSolution[i];
		}
	}

}
