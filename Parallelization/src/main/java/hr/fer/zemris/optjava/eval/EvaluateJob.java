package hr.fer.zemris.optjava.eval;

import java.util.concurrent.BlockingQueue;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.WorkersPoisoningSolution;

public class EvaluateJob<T> implements Runnable{

	private BlockingQueue<GASolution<T>> toEvaluate;
	private BlockingQueue<GASolution<T>> evaluated;
	public static final WorkersPoisoningSolution<Object> PILL = new WorkersPoisoningSolution<>();
	
	public EvaluateJob (BlockingQueue<GASolution<T>> toEvaluate, BlockingQueue<GASolution<T>> evaluated) {
		super();
		this.toEvaluate = toEvaluate;
		this.evaluated = evaluated;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while(true) {
			GASolution<T> p = null;
			try {
				p = toEvaluate.take();
				
			} catch (InterruptedException e) {
				System.out.println("Interrupted in taking");
				e.printStackTrace();
			}
			
			if(p == PILL) { break; }
			((IGAEvaluatorProvider<T>)Thread.currentThread()).getEvaluator().evaluate(p);
			
			try {
				evaluated.put(p);
				
			} catch (InterruptedException e) {
				System.out.println("Interrupted in putting");
				e.printStackTrace();
			}
		}
	}
}
