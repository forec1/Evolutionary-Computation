package hr.fer.zemris.trisat;

/**
 * Predstavlja jednu formulu.
 * @author filip
 *
 */
public class SATFormula {
	
	/**
	 * Broj varijabli formule.
	 */
	private int numberOfVariables;
	
	/**
	 * Klauzule formule.
	 */
	private Clause[] clauses;
	
	/**
	 * Stvara novu formulu.
	 * @param numberOfVariables Broj vraijabli
	 * @param clauses Klauzule formule
	 */
	public SATFormula(int numberOfVariables, Clause[] clauses) {
		this.numberOfVariables = numberOfVariables;
		this.clauses = clauses;
	}
	
	/**
	 * @return Broj vraijabli klauzule
	 */
	public int getNumberOfVariables() {
		return numberOfVariables;
	}
	
	/**
	 * @return Broj klauzula formule
	 */
	public int getNumberOfClauses() {
		return clauses.length;
	}
	
	/**
	 * Vrača klauzulu na poziciji index.
	 * @param index Pozicija klauzule
	 * @return Klauzula na poziciji index
	 */
	public Clause getClause(int index) {
		if(index < 0 || index > clauses.length) {
			throw new IndexOutOfBoundsException("Index can't be less than 0 or greater "
					+ "than " + clauses.length);
		}
		return clauses[index];
	}
	
	/**
	 * Provjerava dali je formula zadovoljena.
	 * @param assignment Rješenje koje treba ispitat
	 * @return true ako rješenje zadovoljava formulu, false inače
	 */
	public boolean isSatisfied(BitVector assignment) {
		for(Clause clause : clauses) {
			if( !(clause.isSatisfied(assignment)) ) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(Clause clause : clauses) {
			s += "(" + clause + ")";
		}
		return s;
	}
}
