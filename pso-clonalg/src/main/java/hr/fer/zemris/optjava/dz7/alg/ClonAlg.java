package hr.fer.zemris.optjava.dz7.alg;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import hr.fer.zemris.optjava.dz7.function.IErrorFunction;

public class ClonAlg implements Algorithm{
	
	private IErrorFunction function;
	private int n;
	private int d;
	private double beta;
	private double mutationRate;
	private List<Antibody> P;
	private List<Antibody> C;
	private List<Antibody> Pr;
	private Random rand;
	private int maxiter;
	private double merr;	
	
	public ClonAlg(IErrorFunction function, int n, int d, double beta, int maxiter,
			double merr, double mutationRate) {
		this.P = new LinkedList<>();
		this.C = new LinkedList<>();
		this.Pr = new LinkedList<>();
		this.function = function;
		this.n = n;
		this.d = d;
		this.beta = beta;
		this.maxiter = maxiter;
		this.merr = merr;
		this.mutationRate = mutationRate;
		this.rand = new Random();
	}

	public double[] run() {
		initialize();
		int iteration = 0;
		while(iteration < maxiter) {
			for(Antibody p : P) {
				p.affinity(function);
			}
			P = select(P);
			System.out.println("best: " + P.get(0).getAffinity() + " iter: " + iteration + "/" + maxiter);
			createClones();
			hypermutate();
			for(Antibody c : C) {
				c.affinity(function);
			}
			P = select(C);
			createNew();
			replace();
			C.clear();
			if(-P.get(0).getAffinity() < merr) {
				return P.get(0).getSolution();
			}
			iteration++;
		}
		P = select(P);
		return P.get(0).getSolution();
	}
	
	private void replace() {
		for(int size = P.size(), i = size - d, j = 0; i < size; i++, j++) {
			P.set(i, Pr.get(j));
		}
	}
	
	private void createNew() {
		int dim = function.getDimension();
		for(int i = 0; i < d; i++) {
			double[] solution = new double[dim];
			for(int j = 0; j < dim; j++) {
				solution[j] = rand.nextDouble() * 2 - 1;
			}
			Pr.add(new Antibody(solution));
		}
	}
	
	private void hypermutate() {
		for(Antibody c : C) {
			double f = c.getAffinity();
			double prob = 1 - Math.exp(f) / mutationRate;
			if(prob > rand.nextDouble()) {
				c.hypermutate(rand);
				c.affinity(function);
			}
		}
	}
	
	private void createClones() {
		for(int i = 0; i < n; i++) {
			int numberOfClones = (int) Math.floor((beta * n) / (i + 1));
			Antibody toClone = P.get(i);
			for(int j = 0; j < numberOfClones; j++) {
				C.add(toClone.duplicate());
			}
		}
	}
	
	private List<Antibody> select(List<Antibody> x) {
		Collections.sort(x, (p1, p2) -> {
			double p1Affinity = p1.getAffinity();
			double p2Affinity = p2.getAffinity();
			if(p1Affinity == p2Affinity) { return 0; }
			else if (p1Affinity > p2Affinity) { return -1; }
			else { return 1; }
		});
		return x.stream().limit(n).collect(Collectors.toList());
	}
	
	private void initialize() {
		int dim = function.getDimension();
		for(int i = 0; i < n; i++) {
			double[] solution = new double[dim];
			for(int j = 0; j < dim; j++) {
				solution[j] = rand.nextDouble() * 2 - 1;
			}
			P.add(new Antibody(solution));
		}
	}

}
