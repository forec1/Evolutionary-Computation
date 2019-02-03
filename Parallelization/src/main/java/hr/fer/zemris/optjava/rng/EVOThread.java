package hr.fer.zemris.optjava.rng;

import hr.fer.zemris.optjava.eval.IGAEvaluator;
import hr.fer.zemris.optjava.eval.IGAEvaluatorProvider;
import hr.fer.zemris.optjava.rng.rngimpl.RNGRandomImpl;

	public class EVOThread<T> extends Thread implements IRNGProvider, IGAEvaluatorProvider<T>{
		
		private IRNG rng = new RNGRandomImpl();
		private IGAEvaluator<T> evaluator;
		
		public EVOThread() {}
		
		public EVOThread(Runnable target) {
			super(target);
		}
		
		public EVOThread(String name) {
			super(name);
		}
		
		public EVOThread(ThreadGroup group, Runnable target) {
			super(group, target);
		}
		
		public EVOThread(ThreadGroup group, String name) {
			super(group, name);
		}
		
		public EVOThread(Runnable target, String name) {
			super(target, name);
		}
		
		public EVOThread(ThreadGroup group, Runnable target, String name) {
			super(group, target, name);
		}
		
		public EVOThread(ThreadGroup group, Runnable target, String name, 
				long stackSize) {
			super(group, target, name, stackSize);
		}
		
		@Override
		public IRNG getRNG() {
			return rng;
		}
		
		public void setEvalator(IGAEvaluator<T> evaluator) {
			this.evaluator = evaluator;
		}

		@Override
		public IGAEvaluator<T> getEvaluator() {
			return evaluator;
		}
}