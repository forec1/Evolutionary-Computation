package hr.fer.zemris.optjava.dz9;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class NSGA {

	private MOOPProblem problem;
	private int populationSize;
	private int maxiter;
	private List<Solution> population;
	private List<Solution> nextPopulation;
	private List<List<Solution>> fronts;
	private List<Solution> helpList; 
	private Random rand;
	private double sigmaShare;
	private double alpha;
	
	public NSGA(MOOPProblem problem, int populationSize, int maxiter, double sigmaShare, double alpha) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.maxiter = maxiter;
		this.sigmaShare = sigmaShare;
		this.alpha = alpha;
		this.population = new LinkedList<>();
		this.nextPopulation = new LinkedList<>();
		this.helpList = new LinkedList<>();
		this.fronts = new LinkedList<>();
		this.rand = new Random();
	}

	public void run() {
		initPopulation();
		for(int iter = 0; iter < maxiter; iter++) {
//			System.out.println(iter);
			initIteration();
			makeFronts();
			setFitness();
			
			while(nextPopulation.size() < populationSize) {
				Solution firstParent = fitnessProportionateSelection();
				Solution secondParent = fitnessProportionateSelection();
				Solution child = crossBLXalpha(firstParent, secondParent, 0.5);
				mutate(child, 0.05);
				if(satisfiesConstraints(child)) {
					nextPopulation.add(child);
				}
			}
			List<Solution> tmp = population;
			population = nextPopulation;
			nextPopulation = tmp;
			nextPopulation.clear();
			evaluate();
			fronts.clear();
		}
		
		initIteration();
		makeFronts();
		
		int i = 1;
		for(List<Solution> front : fronts) {
			System.out.println("Fronta " + i + ". ima " + front.size() + " rješenja");
			i++;
		}
		printResult();
	}
	
	private void printResult() {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File("izlaz-dec.txt")))) {
			for(Solution sol : population) {
				double[] X = sol.getX();
				for(int i = 0; i < X.length; i++) {
					bw.write(X[i] + " ");
				}
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File("izlaz-obj.txt")))) {
			for(Solution sol : population) {
				double[] f = sol.getF();
				for(int i = 0; i < f.length; i++) {
					bw.write((-f[i]) + " ");
				}
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean satisfiesConstraints(Solution sol) {
		List<double[]> constraints = problem.getConstraints();
		boolean satisfies = true;
		double[] X = sol.getX();
		for(int i = 0; i < X.length; i++) {
			satisfies = X[i] < constraints.get(i)[0] ? false : satisfies;
			satisfies = X[i] > constraints.get(i)[1] ? false : satisfies;
		}
		return satisfies;
	}
	
	private void mutate(Solution sol, double s) {
		double[] X = sol.getX();
		for(int i = 0; i < X.length; i++) {
			X[i] += rand.nextGaussian() * s;
		}
	}
	
	private Solution crossBLXalpha(Solution firstParent, Solution secondParent, double alpha) {
		double[] childX = new double[problem.getDimension()];
		double[] firstParentX = firstParent.getX();
		double[] secondParentX = secondParent.getX();
		for(int i = 0, n = problem.getDimension(); i < n; i++) {
			double cMin = Math.min(firstParentX[i], secondParentX[i]);
			double cMax = Math.max(firstParentX[i], secondParentX[i]);
			double I = cMax - cMin;
			double upperBound = cMax + I * alpha;
			double lowerBound = cMin - I * alpha;
			childX[i] = rand.nextDouble() * (upperBound - lowerBound) + lowerBound;			
		}
		return new Solution(childX);
	}
	
	private Solution fitnessProportionateSelection() {
		double fitnessSum = 0.0;
		for(Solution sol : population) {
			fitnessSum += sol.getFitness();
		}
		
		double probs = rand.nextDouble() * fitnessSum;
		for(int i = 0; i < populationSize; i++) {
			probs -= population.get(i).getFitness();
			if(probs < 0) {
				return population.get(i);
			}
		}
		return population.get(populationSize - 1); 
	}
	
	private void evaluate() {
		double[] objectives = new double[problem.getNumberOfObjectives()];
		for(Solution sol : population) {
			problem.evaluateSolution(sol.getX(), objectives);
			sol.setF(Arrays.copyOf(objectives, objectives.length));
		}
	}
	
	private void setFitness() {
		double startFitness = populationSize;
		double min = startFitness;
		for(List<Solution> front : fronts) {
			for(Solution sol : front) {
				double nci = calcNCi(sol);
				sol.setFitness(startFitness / nci);
				if((startFitness / nci) < min) {
					min = startFitness / nci;
				}
			}
			startFitness = min * 0.9;
		}
	}
	
	private double calcNCi(Solution sol) {
		double nci = 0.0;
		for(int i = 0; i < populationSize; i++) {
			double d = calcDistance(sol, population.get(i));
			nci += sh(d);
		}
		return nci;
	}
	
	private double sh(double d) {
		if(d == 0.0) {
			return 1;
		}
		if(d >= sigmaShare) {
			return 0;
		}
		
		return 1.0 - Math.pow(d / sigmaShare, alpha);
	}
	
	private double calcDistance(Solution sol1, Solution sol2) {
		double[] Xi = sol1.getX();
		double[] Xj = sol2.getX();
		double maxXi = Arrays.stream(Xi).max().getAsDouble();
		double maxXj = Arrays.stream(Xj).max().getAsDouble();
		double minXi = Arrays.stream(Xi).min().getAsDouble();
		double minXj = Arrays.stream(Xj).min().getAsDouble();
		double max = Math.max(maxXi, maxXj);
		double min = Math.min(minXi, minXj);
		double d = 0.0;
		for(int i = 0; i < Xi.length; i++) {
			d += Math.pow((Xi[i] - Xj[i]) / (max - min), 2);
		}
		return Math.sqrt(d);
	}
	
	private void makeFronts() {
		helpList.addAll(population);
		while(!helpList.isEmpty()) {
			List<Solution> front = new LinkedList<>();
			for(int i = 0, n = helpList.size(); i < n; i++) {
				Solution sol = helpList.get(i);
				if(sol.getN() == 0) {
					front.add(sol);
				}
			}
			updateNs(front);
			helpList.removeAll(front);
			fronts.add(front);
		}
		helpList.clear();
	}
	
	private void updateNs(List<Solution> solutions) {
		for(Solution sol : solutions) {
			List<Solution> S = sol.getS();
			for(Solution solution : S) {
				solution.updateN(-1);
			}
		}
	}
	
	private void initIteration() {
		for(int i = 0; i < populationSize; i++) {
			Solution sol = population.get(i);
			for(int j = 0; j < populationSize; j++) {
				Solution test = population.get(j);
				if(sol.isDominant(test)) {
					sol.addToS(test);
				}
			}
			for(int j = 0; j < populationSize; j++) {
				Solution test = population.get(j);
				if(!test.equals(sol) && test.isDominant(sol)) {
					sol.updateN(1);
				}
			}
		}
	}
	
	private void initPopulation() {
		int dimension = problem.getDimension();
		List<double[]> constraints = problem.getConstraints();
		
		for(int i = 0; i < populationSize; i++) {
			double[] X = new double[dimension];
			for(int j = 0; j < dimension; j++) {
				double[] constraint = constraints.get(j);
				X[j] = rand.nextDouble() * (constraint[1] + constraint[0]) - constraint[0];
			}
			population.add(new Solution(X));
		}
		evaluate();
	}
}
