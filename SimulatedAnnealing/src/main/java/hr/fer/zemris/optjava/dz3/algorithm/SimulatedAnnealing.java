package hr.fer.zemris.optjava.dz3.algorithm;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.decoder.IDecoder;
import hr.fer.zemris.optjava.dz3.function.IFunction;
import hr.fer.zemris.optjava.dz3.neighborhood.INeighborhood;
import hr.fer.zemris.optjava.dz3.solution.SingleObjectiveSolution;
import hr.fer.zemris.optjava.dz3.tempschedule.ITempSchedule;

public class SimulatedAnnealing<T extends SingleObjectiveSolution> implements IOptAlgorithm<SingleObjectiveSolution>{

	private IDecoder<T> decoder;
	private INeighborhood<T> neighborhood;
	private T startWith;
	private IFunction function;
	private boolean minimize;
	private Random rand;
	private ITempSchedule tempSchedule;
	
	
	public SimulatedAnnealing(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith, IFunction function,
			ITempSchedule tempSchedule, boolean minimize) {
		this.decoder = decoder;
		this.neighborhood = neighborhood;
		this.startWith = startWith;
		this.function = function;
		this.minimize = minimize;
		this.tempSchedule = tempSchedule;
		rand = new Random();
	}


	public T run() {
		@SuppressWarnings("unchecked")
		T x = (T) startWith.duplicate();
		double t = tempSchedule.getNextTemperature();
		while(t > 10e-10) {
			T xNeighbor = neighborhood.randomNeighborhoog(x);
			double valueAtX = function.valueAt(decoder.decode(x));
			double valueAtXNeighbor = function.valueAt(decoder.decode(xNeighbor));
			if(tempSchedule.getOuterLoopCounter() % 1000 == 0) {
				System.out.println("iter = " + tempSchedule.getOuterLoopCounter() + " f = " + valueAtX + " za " + x);
				System.out.println("temp = " + t);
			}
			double delta = valueAtXNeighbor - valueAtX;
			delta = minimize ? delta : -delta;
			if(Math.abs(delta) < 10e-6) {
				x = xNeighbor;
			} else {
				double probs = Math.exp(-delta / t);
				if(rand.nextDouble() < probs) {
					x = xNeighbor;
				}
			}
			t = tempSchedule.getNextTemperature();
		}
		System.out.println("f = " + function.valueAt(decoder.decode(x)));
		return x;
	}

}
