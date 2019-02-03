package hr.fer.zemris.generic.ga;

import java.util.concurrent.BlockingQueue;

public class Job<T> implements Runnable {

	private BlockingQueue<AllJob<T>> toDo;
	public static final AllJob<Object> PILL = new AllJob<>();
	
	public Job(BlockingQueue<AllJob<T>> toDo) {
		this.toDo = toDo;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				AllJob<T> job = toDo.take();
				if(job == PILL) { break; }
				job.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
