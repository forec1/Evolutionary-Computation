package hr.fer.zemris.trisat;

/**
 * Predstavlja verziju rješenja koje se može modificirati.
 * @author filip
 *
 */
public class MutableBitVector extends BitVector{
	
	public MutableBitVector(boolean... bits) {
		super(bits);
	}
	
	public MutableBitVector(int n) {
		super(n);
	}
	
	/**
	 * Postavlja vrijednost bita na value na poziciji index
	 * @param index Pozicija bit
	 * @param value Nova vrijednost bita
	 */
	public void set(int index, boolean value) {
		if(index < 0 || index > bits.length) {
			throw new IndexOutOfBoundsException("Index can't be less than 0 or greater "
					+ "than " + bits.length);
		}
		super.bits[index] = value;
	}
}
