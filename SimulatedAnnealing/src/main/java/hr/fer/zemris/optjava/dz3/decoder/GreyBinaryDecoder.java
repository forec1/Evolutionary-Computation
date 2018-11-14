package hr.fer.zemris.optjava.dz3.decoder;

import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public class GreyBinaryDecoder extends BitVectorDecoder{

	public GreyBinaryDecoder(double d1, double d2, int i1, int i2) {
		super(d1, d2, i1, i2);
	}

	public GreyBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		super(mins, maxs, bits, n);
	}

	@Override
	public double[] decode(BitVectorSolution x) {
		BitVectorSolution solution = x.duplicate();
		double[] decodedSolution = new double[n];
		int offset = 0;
		for(int i = 0; i < this.n; i++) {
			byte[] greyToBinary = new byte[this.bits[i]];
			greyToBinary[0] = solution.bits[offset];
 			for(int j = 1; j < this.bits[i]; j++) {
				greyToBinary[j] = (byte) (greyToBinary[j-1] ^ solution.bits[offset + j]);
			}
 			int val = 0;
 			for(int j = 0; j < this.bits[i]; j++) {
 				val += greyToBinary[j] * Math.pow(2, this.bits[i] - j - 1);
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
