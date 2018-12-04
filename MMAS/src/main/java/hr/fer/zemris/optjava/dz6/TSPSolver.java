package hr.fer.zemris.optjava.dz6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TSPSolver {
	
	private static File problemFile;
	private static int sizeOfListOfCandidates;
	private static int numberOfAnts;
	private static int maxiter;
	private static double[][] distances;
	private static double[][] heuristicInfo;
	private static int problemSize;
	private static List<City> cities = new LinkedList<City>();
	private static final double beta = 2;
	private static final double alpha = 0.9;
	private static final double rho = 0.02;
	private static double a;
	
	public static void main(String[] args) {
		
		if(args.length != 4) {
			throw new RuntimeException("Broj argumenata treba biti 4!");
		}
		parseArgumetns(args);
		try {
			parseProblemFile();
		} catch (IOException e) {
			throw new RuntimeException("Datoteka s opisom problema nije pronađena!");
		}
		generateHeuristic();
		generateListOfCandidates();
		calculateParamA();
		double tauMax = 1.0 / (rho * getNNRoute());
		MMASAlgorithm alg = new MMASAlgorithm(numberOfAnts, maxiter, heuristicInfo, distances, tauMax, problemSize, cities, rho, a, alpha);
		Ant solution = alg.run();
		System.out.println(solution);
	}
	
	private static void calculateParamA() {
		int n = problemSize;
		double p = 0.8;
		double ni = (n - 1) / (n * (- 1 + Math.pow(p, -1.0 / n)));
		a = ni * n;
	}
	
	private static double getNNRoute() {
		City currentCity = cities.get(0);
		Ant ant = new Ant(currentCity);
		double total = 0.0;
		for(int i = 0; i < problemSize; i++) {
			int minIndex = 0;
			double minDistance = Double.MAX_VALUE;
			for(int j = 0; j < problemSize; j++) {
				if(distances[currentCity.getIndex()][j] < minDistance && !ant.beenThere(cities.get(j))) {
					minIndex = j;
					minDistance = distances[currentCity.getIndex()][j];
				}
			}
			total += distances[currentCity.getIndex()][minIndex];
			currentCity = cities.get(minIndex);
			ant.visit(currentCity);
		}
		return total;
	}
	
	private static void generateHeuristic() {
		distances = new double[problemSize][problemSize];
		heuristicInfo = new double[problemSize][problemSize];
		for(City city : cities) {
			for(int i = 0; i < problemSize; i++) {
				double x = cities.get(i).getxCoord();
				double y = cities.get(i).getyCoord();
				distances[city.getIndex()][i] = city.calculateDistance(x, y);
				heuristicInfo[city.getIndex()][i] = 1.0 / Math.pow(distances[city.getIndex()][i], beta);
			}
		}
	}
	
	private static void generateListOfCandidates() {
		for(City city : cities) {
			List<City> listOfCandidates = new LinkedList<>();
			for(int i = 0; i < problemSize; i++) {
				City candidate = cities.get(i);
				if(!candidate.equals(city)) {
					listOfCandidates.add(candidate);
				}
			}
			Collections.sort(listOfCandidates, (o1, o2) -> {
				double d1 = distances[city.getIndex()][o1.getIndex()];
				double d2 = distances[city.getIndex()][o2.getIndex()];
				if(d1 == d2) { 
					return 0;
				}
				else if(d1 > d2) { 
					return 1;
				}
				else {
					return -1;
				}
			
			});
			listOfCandidates = listOfCandidates.subList(0, sizeOfListOfCandidates);
			city.setListOfCandidates(listOfCandidates);
		}
	}
	
	private static void parseArgumetns(String[] args) {
		problemFile = new File(args[0]);
		sizeOfListOfCandidates = Integer.parseInt(args[1]);
		numberOfAnts = Integer.parseInt(args[2]);
		maxiter = Integer.parseInt(args[3]);
	}
	
	private static void parseProblemFile() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(problemFile));
		String line;
		while((line = br.readLine()) != null) {
			if(line.startsWith("DIMENSION")) {
				problemSize = Integer.parseInt(line.split(":")[1].trim());
			} else if (line.equals("NODE_COORD_SECTION")) {
				while(!(line = br.readLine()).equals("EOF")) {
					String[] split = line.split("\\s");
					int index = Integer.parseInt(split[0]) - 1;
					double xCoord = Double.parseDouble(split[1]);
					double yCoord = Double.parseDouble(split[2]);
					cities.add(new City(index, xCoord, yCoord));
				}
			}
		}
		br.close();
	}
}
