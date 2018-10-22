package hr.fer.zemris.trisat;

import java.util.Random;

/**
 * Binarni vektor koji predstavlja read-only dodjelu varijabli.
 * @author filip
 *
 */
public class BitVector {

	/**
	 * Binarni vektor.
	 */
	protected boolean[] bits;
	
	/**
	 * Stvara novi BitVector s nasumičnim bitovima.
	 * @param rand
	 * @param numberOfBits broj bitova vektora
	 */
	public BitVector(Random rand, int numberOfBits) {
		bits = new boolean[numberOfBits];
		for(int i = 0; i < numberOfBits; i++) {
			bits[i] = rand.nextBoolean();
		}
	}
	
	/**
	 * Stvara novi BitVector s danim bitovima.
	 * @param bits bitovi vektora
	 */
	public BitVector(boolean ... bits) {
		this.bits = bits;
	}
	
	/**
	 * Stvara novi BitVector gdje su svi bitovu nule.
	 * @param n veličina vektora
	 */
	public BitVector(int n) {
		bits = new boolean[n];
	}
	
	/**
	 * Vrača bit iz binarnog vektora na danoj poziciji.
	 * @param index pozicija bita u vektoru
	 * @return bit na poziciji index
	 */
	public boolean get(int index) {
		if(index < 0 || index > bits.length) {
			throw new IndexOutOfBoundsException("Index can't be less than 0 or greater "
					+ "than " + bits.length);
		}
		return bits[index];
	}
	
	/**
	 * @return duljina binarnog vektora
	 */
	public int getSize() {
		return bits.length;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(boolean bit : bits) {
			if(bit) { s += "1"; }
			else { s += "0"; }
		}
		return s;
	}
	
	/**
	 * Vrača novi MutableBitVektor s istom vrijednosti bitova.
	 */
	public MutableBitVector copy() {
		boolean[] newBits = new boolean[bits.length];
		for(int i = 0; i < bits.length; i++) {
			newBits[i] = bits[i];
		}
		return new MutableBitVector(newBits);
	}
}
