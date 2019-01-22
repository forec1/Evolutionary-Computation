package hr.fer.zemris.optjava.dz10;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NSGAII {

	private MOOPProblem problem;
	private int populationSize;
	private int maxiter;
	private List<Solution> population;
	private List<Solution> childPopulation;
	private List<List<Solution>> fronts;
	private List<Solution> helpList; 
	private List<Solution> union;
	private Random rand;
	private double sigmaShare;
	private double alpha;
	private int method;
	
	public NSGAII(MOOPProblem problem, int populationSize, int maxiter, double sigmaShare, double alpha, int method) {
		this.problem = problem;
		this.populationSize = populationSize;
		this.maxiter = maxiter;
		this.sigmaShare = sigmaShare;
		this.alpha = alpha;
		this.method = method;
		this.population = new LinkedList<>();
		this.childPopulation = new LinkedList<>();
		this.helpList = new LinkedList<>();
		this.fronts = new LinkedList<>();
		this.union = new LinkedList<>();
		this.rand = new Random();
	}
	
	public void run() {
		initPopulation();
		initIteration(population);
		makeFronts(population);
		setFitness(population);
		crossoverAndMutation();
		for(int iter  = 0; iter < maxiter; iter++) {
		    System.out.println("iter: "+ iter);
		    union.clear();
			merge(population, childPopulation, union);
			childPopulation.clear();
			population.clear();
			fronts.clear();
			evaluate(union);
			initIteration(union);		
			makeFronts(union);			
			setFitness(union);
			
			List<Solution> frontL = null;
			for(int i = 0, n = fronts.size(); i < n; i++) {
				List<Solution> front = fronts.get(i);
				if(population.size() + front.size() < populationSize) {
					merge(population, front, population);
				} else {
					frontL = front;
					break;
				}
			}
			if(population.size() < populationSize) {
				crowdingSort(frontL);
				int i = 0;
				while(population.size() < populationSize) {
					population.add(frontL.get(i));
					++i;
				}
			}
			crossoverAndMutation();
			
		}
		evaluate(population);
		System.out.println("Populacija ima " + population.size() + " jedinki.");
		for(int i = 0, n = fronts.size(); i < n; i++) {
		    System.out.println("Fronta " + (i + 1) + " ima " + fronts.get(i).size() + " rješenja.");
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
	
	private void crowdingSort(List<Solution> list) {
		for(Solution sol : list) {
			sol.setD(0.0);
		}
		for(int i = 0, n = list.get(0).getF().length; i < n; i++) {
			int index = i;
			Collections.sort(list, (o1, o2) -> o1.compareByF(o2, index));
			Solution solMax = list.get(0);
			Solution solMin = list.get(list.size() - 1);
			solMax.setD(Double.MAX_VALUE);
			solMin.setD(Double.MAX_VALUE);
			for(int j = 2, m = list.size() - 1; j < m; j++) {
				Solution sol = list.get(j);
				double d = sol.getD();
				double dUpdate = list.get(j + 1).getF()[i] + list.get(j - 1).getF()[i];
				dUpdate = dUpdate / (solMax.getF()[i] - solMin.getF()[i]);
				sol.setD(d + dUpdate);
			}
		}
		Collections.sort(list, (o1, o2) -> o1.compareByD(o2));
	}
	
	private void merge(List<Solution> list1, List<Solution> list2, List<Solution> toList) {
		toList.addAll(list1);
		toList.addAll(list2);
	}
	
	private void crossoverAndMutation() {
		while(childPopulation.size() < populationSize) {
			Solution firstParent = fitnessProportionateSelection();
			Solution secondParent = fitnessProportionateSelection();
			Solution child = crossBLXalpha(firstParent, secondParent, 0.5);
			mutate(child, 0.05);
			if(satisfiesConstraints(child)) {
				childPopulation.add(child);
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
				X[j] = rand.nextDouble() * (constraint[1] - constraint[0]) + constraint[0];
			}
			population.add(new Solution(X));
		}
		evaluate(population);
	}
	
	private void initIteration(List<Solution> list) {
		for(int i = 0, n = list.size(); i < n; i++) {
			Solution sol = list.get(i);
			sol.getS().clear();
			for(int j = 0; j < n; j++) {
				Solution test = list.get(j);
				if(sol.isDominant(test)) {
					sol.addToS(test);
					test.updateN(1);
				}
			}
//			for(int j = 0; j < n; j++) {
//				Solution test = list.get(j);
//				if(!test.equals(sol) && test.isDominant(sol)) {
//					sol.updateN(1);
//				}
//			}
		}
	}
	
	private void setFitness(List<Solution> list) {
		double startFitness = list.size();
		double min = startFitness;
		for(List<Solution> front : fronts) {
			for(Solution sol : front) {
				double nci = calcNCi(sol, list);
				sol.setFitness(startFitness / nci);
				if((startFitness / nci) < min) {
					min = startFitness / nci;
				}
			}
			startFitness = min * 0.9;
		}
	}
	
	private double calcNCi(Solution sol, List<Solution> list) {
		double nci = 0.0;
		for(int i = 0, n = list.size(); i < n; i++) {
//			double d = calcDistance(sol, population.get(i));
			Solution test = list.get(i);
			double d = method == 1 ? calcDistance(() -> sol.getX(), () -> test.getX()) 
					: calcDistance(() -> sol.getF(), () -> test.getF());;
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
	
	private double calcDistance(IMethod m1, IMethod m2) {
		double[] datai = m1.getData();
		double[] dataj = m2.getData();
		double maxDatai = Arrays.stream(datai).max().getAsDouble();
		double maxDataj = Arrays.stream(dataj).max().getAsDouble();
		double minDatai = Arrays.stream(datai).min().getAsDouble();
		double minDataj = Arrays.stream(dataj).min().getAsDouble();
		double max = Math.max(maxDatai, maxDataj);
		double min = Math.min(minDatai, minDataj);
		double d = 0.0;
		for(int i = 0; i < datai.length; i++) {
			d += Math.pow((datai[i] - dataj[i]) / (max - min), 2);
		}
		return Math.sqrt(d);
	}
	
	private void makeFronts(List<Solution> list) {
		helpList.addAll(list);
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
	
	private void evaluate(List<Solution> list) {
		double[] objectives = new double[problem.getNumberOfObjectives()];
		for(Solution sol : list) {
			problem.evaluateSolution(sol.getX(), objectives);
			sol.setF(Arrays.copyOf(objectives, objectives.length));
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
}






















