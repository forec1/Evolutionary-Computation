package hr.fer.zemris.generic.ga;

public class WorkersPoisoningSolution<T> extends GASolution<T> {

	public WorkersPoisoningSolution() {}
	
	@Override
	public GASolution<T> duplicate() {
		return new WorkersPoisoningSolution<>();
	}

}
