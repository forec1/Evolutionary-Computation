package hr.fer.zemris.optjava.dz6;

import java.util.LinkedList;
import java.util.List;

public class City {
	private int index;
	private double xCoord;
	private double yCoord;
	private List<City> listOfCandidates;
	
	public City(int index, double xCoord, double yCoord) {
		this.index = index;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.listOfCandidates = new LinkedList<City>();
	}

	public int getIndex() {
		return index;
	}

	public double getxCoord() {
		return xCoord;
	}

	public double getyCoord() {
		return yCoord;
	}
	
	public List<City> getListOfCandidates() {
		return listOfCandidates;
	}
	
	public void setListOfCandidates(List<City> listOfCandidates) {
		this.listOfCandidates = listOfCandidates;
	}

	public double calculateDistance(double x, double y) {
		return Math.sqrt(Math.pow(this.xCoord - x, 2) + Math.pow(this.yCoord - y, 2));
	}
	
	@Override
	public String toString() {
		return Integer.toString(index);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof City)) { return false; }
		City cityToComapre = (City) obj;
		return this.index == cityToComapre.getIndex() ? true : false;
	}
}
