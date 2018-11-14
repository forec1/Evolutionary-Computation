package hr.fer.zemris.optjava.dz3.algorithm;

import hr.fer.zemris.optjava.dz3.decoder.GreyBinaryDecoder;
import hr.fer.zemris.optjava.dz3.decoder.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.solution.BitVectorSolution;

public class Test {

	public static void main(String[] args) {
		
		byte a  = 1;
		byte b = 1;
		System.out.println(a ^ b);
		BitVectorSolution x = new BitVectorSolution(6);
		x.bits[0] = 0;
		x.bits[1] = 0;
		
		for(int i = 2; i < 6; i++) {
			x.bits[i] = 1;
		}
		
		NaturalBinaryDecoder dec = new NaturalBinaryDecoder(new double[] {8, 8},new double[] {0, 0}, new int[] {3, 3}, 2);
		
		double[] sol = dec.decode(x);
		System.out.println(sol[0]);
		System.out.println(sol[1]);
		
		x.bits[0] = x.bits[1] = 0;
		x.bits[4] = x.bits[5] = 0;
		x.bits[2] = x.bits[3] = 1;
		
		GreyBinaryDecoder gdec = new GreyBinaryDecoder(new double[] {7, 7},new double[] {0, 0}, new int[] {3, 3}, 2);
		sol = gdec.decode(x);
		System.out.println(sol[0]);
		System.out.println(sol[1]);
		
	}

}
