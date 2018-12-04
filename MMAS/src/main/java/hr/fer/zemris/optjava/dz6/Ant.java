package hr.fer.zemris.optjava.dz6;

import java.util.LinkedList;
import java.util.List;

public class Ant {
	private List<City> route;
	private double totalDistance;
	
	public Ant(City start) {
		route = new LinkedList<City>();
		route.add(start);
	}
	
	public void updateTotalDistance(double[][] distances) {
		totalDistance = 0.0;
		for(int i = 0, n = route.size() - 1; i < n; i++) {
			City city1 = route.get(i);
			City city2 = route.get(i + 1);
			totalDistance += distances[city1.getIndex()][city2.getIndex()];
		}
		totalDistance += distances[route.get(route.size() - 1).getIndex()][0];
	}
	
	public double getTotalDistance() {
		return totalDistance;
	}
	
	public List<City> getRoute() {
		return route;
	}
	
	public void visit(City city) {
		route.add(city);
	}
	
	public boolean beenThere(City city) {
		return route.contains(city);
	}
	
	public boolean beenThere(List<City> cities) {
		return route.containsAll(cities);
	}
	
	public City getStartCity() {
		return route.get(0);
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0, n = route.size(); i < n; i++) {
			s += route.get(i);
			if(i != n - 1) {
				s += "-";
			}
		}
		return s;
	}
}










