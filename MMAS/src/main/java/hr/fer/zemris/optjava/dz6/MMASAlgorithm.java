package hr.fer.zemris.optjava.dz6;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MMASAlgorithm {
	
	private int numberOfAnts;
	private int maxiter;
	private double[][] heuristicInfo;
	private double[][] distances;
	private double[][] tauij;
	private double tauMax;
	private double tauMin;
	private int problemSize;
	private List<City> cities;
	private double alpha;
	private double rho;
	private double a;
	private List<Ant> colony;
	private Random rand;
	private int pom = 0;
	
	public MMASAlgorithm(int numberOfAnts, int maxiter, double[][] heuristicInfo, double[][] distances , 
			double tauMax, int problemSize, List<City> cities, double rho, double a, double alpha) {
		this.numberOfAnts = numberOfAnts;
		this.maxiter = maxiter;
		this.heuristicInfo = heuristicInfo;
		this.problemSize = problemSize;
		this.cities = cities;
		this.distances = distances;
		this.tauMax = tauMax;
		this.rho = rho;
		this.a = a;
		this.alpha = alpha;
		this.colony = new LinkedList<Ant>();
		this.rand = new Random();
		this.tauij = new double[problemSize][problemSize];
	}
	
	public Ant run() {
		setTauij();
		int iterCounter = 0;
		int nothingHappensIter = 0;
		int resetToTauMaxIter = maxiter / 10;
		Ant iterationBest = null;
		Ant bestSoFar = null;
		while(iterCounter < maxiter) {
			iterCounter++;
			generateColony();
			iterationBest = colony.get(0);
			for(int i = 0; i < numberOfAnts; i++) {
				Ant currentGoAnt = colony.get(i);
				goAntGo(currentGoAnt);
				colony.get(i).updateTotalDistance(distances);
				if(iterationBest.getTotalDistance() > currentGoAnt.getTotalDistance()) {
					iterationBest = currentGoAnt;
				}
			}
			if(bestSoFar == null) {
				bestSoFar = iterationBest;
				nothingHappensIter = 0;
				setTauMax(bestSoFar.getTotalDistance());
				setTauMin();
			} else if(bestSoFar.getTotalDistance() > iterationBest.getTotalDistance()) {
				bestSoFar = iterationBest;
				tauMax = 1.0 / (rho * bestSoFar.getTotalDistance());
				nothingHappensIter = 0;
				setTauMax(bestSoFar.getTotalDistance());
				setTauMin();
			}
			if(nothingHappensIter >= resetToTauMaxIter) {
				setTauij();
				nothingHappensIter = 0;
			}
			nothingHappensIter++;
			evaporate();
			if(problemSize > 100) {
				double prob = rand.nextDouble();
				if(prob < iterCounter/maxiter) {
					updateTauij(bestSoFar);
				} else {
					updateTauij(iterationBest);
				}
			} else {
				updateTauij(iterationBest);
			}
			System.out.println("iter: " + iterCounter + " > bestSoFar: " + bestSoFar.getTotalDistance());
		}
		return bestSoFar;
	}
	
	private void updateTauij(Ant ant) {
		List<City> route = ant.getRoute();
		for(int i = 0; i < problemSize - 1; i++) {
			int index1 = route.get(i).getIndex();
			int index2 = route.get(i + 1).getIndex();
			if(tauij[index1][index2] + 1.0 / ant.getTotalDistance() < tauMax) {
				tauij[index1][index2] += 1.0 / ant.getTotalDistance();	
				tauij[index2][index1] += 1.0 / ant.getTotalDistance();
			} else {
				tauij[index1][index2] = tauMax;
				tauij[index2][index1] = tauMax;
			}
		}
	}
	
	private void evaporate() {
		for(int i = 0; i < problemSize; i++) {
			for(int j = 0; j < problemSize; j++) {
				if(tauij[i][j] * (1 - rho) > tauMin) {
					tauij[i][j] *= (1 - rho);
				} else {
					tauij[i][j] = tauMin;
				}
				
			}
		}
	}
	
	private void goAntGo(Ant ant) {
		City currentCity = ant.getStartCity();
		for(int i = 0; i < problemSize; i++) {
			List<City> candidates = currentCity.getListOfCandidates();
			if(ant.beenThere(candidates)) {
				currentCity = calculateNextCity(currentCity, cities, ant);
			} else {
				currentCity = calculateNextCity(currentCity, candidates, ant);
			}
			ant.visit(currentCity);
		}
	}
	
	private City calculateNextCity(City city, List<City> candidates, Ant ant) {
		double random = rand.nextDouble();
		double probs = 0.0;
		City nextCity = null;
		double sum = 0.0;
		for(City candidate : candidates) {
			if(candidate.equals(city)) { continue; }
			sum += heuristicInfo[city.getIndex()][candidate.getIndex()] * Math.pow(tauij[city.getIndex()][candidate.getIndex()], alpha);
		}
		boolean breaky = false;
		for(City candidate : candidates) {
			if(candidate.equals(city)) { continue; }
			probs += (heuristicInfo[city.getIndex()][candidate.getIndex()] * Math.pow(tauij[city.getIndex()][candidate.getIndex()], alpha)) / sum;
			if(probs > random && !ant.beenThere(candidate) && !breaky) {
				nextCity = candidate;
				breaky = true;
				break;
			}
		}
		if(!breaky) {
			for(City candidate : candidates) {
				if(!ant.beenThere(candidate)) {
					nextCity = candidate;
					break;
				}
			}
		}
		if(nextCity == null) {
			nextCity = ant.getStartCity();
		}
		return nextCity;
	}
	
	private void generateColony() {
		colony.clear();
		for(int i = 0; i < numberOfAnts; i++) {
			int index = rand.nextInt(problemSize);
			colony.add(new Ant(cities.get(index)));
		}
	}
	
	private void setTauMax(double routeLength) {
		tauMax = 1.0 / (rho * routeLength);
	}
	
	private void setTauMin() {
		tauMin = tauMax / a;
	}
	
	private void setTauij() {
		for(int i = 0; i < problemSize; i++) {
			for(int j = 0; j < problemSize; j++) {
				tauij[i][j] = tauMax;
			}
		}
	}
	
}
