package hr.fer.zemris.optjava.dz4.function;

public class ErrorFunction implements IFunction{
	
	private double[][] systemMatrix;
	private double[] Y;
	private int numOfRows;
	
	public ErrorFunction(double[][] systemMatrix, double[] Y, int numOfRows) {
		this.systemMatrix = systemMatrix;
		this.Y = Y;
		this.numOfRows = numOfRows;
	}
	
	public double valueAt(double[] x) {
		double[] val = new double[numOfRows];
		for(int i = 0; i < numOfRows; i++) {
			val[i] += x[0] * systemMatrix[i][0] + x[1] * Math.pow(systemMatrix[i][0], 3) * systemMatrix[i][1] + 
					x[2] * Math.exp(x[3] * systemMatrix[i][2]) * (1 + Math.cos(x[4] * systemMatrix[i][3])) + 
					x[5] * systemMatrix[i][3] * Math.pow(systemMatrix[i][4], 2) - Y[i];
			val[i] = Math.pow(val[i], 2);
		}
		double sum = 0.0;
		for(int i = 0; i < numOfRows; i++) {
			sum += val[i];
		}
		return sum;
	}
	
	public int getNumberOfVariables() {
		return 6;
	}
}
