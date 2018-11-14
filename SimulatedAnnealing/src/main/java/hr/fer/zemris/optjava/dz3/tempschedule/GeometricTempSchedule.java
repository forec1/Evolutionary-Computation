package hr.fer.zemris.optjava.dz3.tempschedule;

public class GeometricTempSchedule implements ITempSchedule{
	
	private double alpha;
	private double tInitial;
	private double tCurrent;
	private int innerLimit;
	private int outerLimit;
	private int innerCnt;
	private int outerCnt;
	
	public GeometricTempSchedule(double alpha, double tInitial, int innerLimit, int outerLimit) {
		this.alpha = alpha;
		this.tInitial = tInitial;
		this.innerLimit = innerLimit;
		this.outerLimit = outerLimit;
		this.tCurrent = this.tInitial;
		this.innerCnt = this.outerCnt = 0;
	}

	public double getNextTemperature() {
		innerCnt++;
		if(outerCnt == outerLimit) { return -100; }
		if(innerCnt == innerLimit) {
			tCurrent = alpha * tCurrent;
			innerCnt = 0;
			outerCnt++;
		}
		return tCurrent;
	}

	public int getInnerLoopCounter() {
		return innerCnt;
	}

	public int getOuterLoopCounter() {
		return outerCnt;
	}
	

}
