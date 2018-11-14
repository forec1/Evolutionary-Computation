package hr.fer.zemris.optjava.dz3.tempschedule;

public interface ITempSchedule {
	
	public double getNextTemperature();
	public int getInnerLoopCounter();
	public int getOuterLoopCounter();
}
