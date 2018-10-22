package hr.fer.zemris.trisat;

/**
 * Predstavlja jednu klauzulu u formuli.
 * @author filip
 *
 */
public class Clause {
	
	/**
	 * Varijable klauzule.
	 */
	private int[] indexes;
	
	/**
	 * Stvara novu klauzulu na osnovu danih varijabli
	 * @param indexes Varijable klauzule
	 */
	public Clause(int[] indexes) {
		this.indexes = indexes;
	}
	
	/**
	 * @return Veličinu klauzule
	 */
	public int getSize() {
		return indexes.length;
	}
	
	/**
	 * Vrača varijablu na danoj poziciji.
	 * @param index Pozicija varijable
	 * @return Varijabla na danoj poziciji
	 */
	public int getLiteral(int index) {
		if(index < 0 || index > indexes.length) {
			throw new IndexOutOfBoundsException("Index can't be less than 0 or greater "
					+ "than " + indexes.length);
		}
		return indexes[index];
	}
	
	/**
	 * Provjerava dali je klauzula zadovoljena
	 * @param assignment Rješenje u obliku binarnog vektora
	 * @return true ako je klauzula zadovoljena, false inače
	 */
	public boolean isSatisfied(BitVector assignment) {
		for(int i = 0; i < indexes.length; i++) {
			boolean indexesValue = false;
			if(indexes[i] > 0) { indexesValue = true; }
			if(assignment.get(Math.abs(indexes[i]) - 1) == indexesValue) { return true; }
		}
		return false;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int index : indexes) {
			s += index + " ";
		}
		return s;
	}
}
