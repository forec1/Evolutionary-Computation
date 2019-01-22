package hr.fer.zemris.optjava.dz10;

import java.util.LinkedList;
import java.util.List;

public class Solution {
	
	/**
	 * Solution
	 */
	private double[] X;
	
	/**
	 * Fitness for each parameter
	 */
	private double[] f;
	
	/**
	 * Number of solutions which dominates over this solution
	 */
	private int n;
	
	/**
	 * 	A list of solutions over which this solution dominates
	 */
	private List<Solution> S;
	
	/**
	 * Crowding distance
	 */
	private double d;
	
	private double fitness;
	
	public Solution(double[] X) {
		this.X = X;
		S = new LinkedList<>();
	}
	
	public void setD(double d) {
		this.d = d;
	}
	
	public double getD() {
		return d;
	}
	
	public double[] getX() {
		return X;
	}
	
	public double[] getF() {
		return f;
	}
	
	public void setF(double[] f) {
		this.f = f;
	}
	
	public List<Solution> getS() {
		return S;
	}
	
	public int getN() {
		return n;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public void updateN(int decision) {
		n = decision > 0 ? n + 1 : n - 1;
	}
	
	public void addToS(Solution solution) {
		S.add(solution);
	}
	
	@Override
	public String toString() {
		return "n = " + n;
	}
	
	public boolean isDominant(Solution test) {
		boolean isDominant = false;
		double[] testF = test.getF();
		for(int i = 0; i < X.length; i++) {
			if(f[i] > testF[i]) {
				isDominant = true;
				break;
			}
		}
		if(isDominant == false) { return isDominant; }
		for(int i = 0; i < X.length; i++) {
			if(f[i] < testF[i]) {
				isDominant = false;
			}
		}
		return isDominant;
	}
	
	public int compareByD(Solution sol1) {
		double d1 = sol1.getD();
		if(d > d1) { return 1; }
		if(d == d1) { return 0; }
		return -1;
	}
	
	public int compareByF(Solution sol1, int i) {
		double[] f1 = sol1.getF();
		if(f[i] > f1[i]) { return 1; }
		if(f[i] == f1[i]) { return 0; }
		return -1;
	}
}
