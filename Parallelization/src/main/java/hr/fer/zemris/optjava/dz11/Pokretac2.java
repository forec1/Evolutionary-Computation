package hr.fer.zemris.optjava.dz11;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.IntegerArraySolutionFactory;
import hr.fer.zemris.generic.ga.GASolution;
import hr.fer.zemris.generic.ga.ISolutionFactory;
import hr.fer.zemris.generic.ga.ParallelGA;
//import hr.fer.zemris.generic.ga.cross.IntegerDiscreteCrossover;
import hr.fer.zemris.generic.ga.cross.IGACrossover;
import hr.fer.zemris.generic.ga.cross.IntegerSimpleCrossover;
import hr.fer.zemris.generic.ga.mutate.ByteArrayMutationImpl;
import hr.fer.zemris.generic.ga.mutate.IGAMutation;
import hr.fer.zemris.generic.ga.sel.IGASelection;
import hr.fer.zemris.generic.ga.sel.KTournamentSelection;
//import hr.fer.zemris.generic.ga.sel.RouletteWheelSelection;
import hr.fer.zemris.optjava.eval.Evaluator;
import hr.fer.zemris.optjava.eval.IGAEvaluator;
import hr.fer.zemris.optjava.rng.EVOThread;

public class Pokretac2 {

	public static String pathToInputFile = "src/main/resources/11-kuca-200-133.png";
	
	public static void main(String[] args) {
		if(args.length != 7) {
			System.out.println("Number of arguments must be 7!");
			System.exit(0);
		}
		String pathToInputFile = args[0];
		int numOfRect = Integer.parseInt(args[1]);
		int populationSize = Integer.parseInt(args[2]);
		int maxiter = Integer.parseInt(args[3]);
		double minFit = Double.parseDouble(args[4]);
		String paramTxtPath = args[5];
		String save = args[6];
		
		Runnable job = () -> {
			ISolutionFactory<int[]> solutionFactory = new IntegerArraySolutionFactory(numOfRect*5+1);
			IGAEvaluator<int[]> evaluator = null;
			GrayScaleImage template = null;
			try {
				template = GrayScaleImage.load(new File(pathToInputFile));
				evaluator = new Evaluator(template);
			} catch (IOException e) {
				e.printStackTrace();
			}
//			IGACrossover<int[]> crossover = new IntegerDiscreteCrossover();
			IGACrossover<int[]> crossover = new IntegerSimpleCrossover();
//			IGASelection<int[]> selection = new RouletteWheelSelection<>();
			IGASelection<int[]> selection = new KTournamentSelection<>(10);
			IGAMutation<int[]> mutation = new ByteArrayMutationImpl(10);
			ParallelGA<int[]> alg = new ParallelGA<int[]>(populationSize
					, maxiter, minFit, solutionFactory, evaluator, crossover, selection, mutation);
			long startTime = System.currentTimeMillis();
			GASolution<int[]> best = alg.run();
			long endTime = System.currentTimeMillis();
			double duration = ((double)endTime - startTime)/1000;
			System.out.println("BEST FITNESS: " + best.fitness);
			System.out.println("Duration: " + duration + "s");
			Evaluator a = (Evaluator)evaluator;
			GrayScaleImage test = new GrayScaleImage(template.getWidth(), template.getHeight());
			a.draw(best, test);
			try {
				test.save(new File(save));
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		EVOThread<int[]> thread = new EVOThread<>(job);
		thread.start();
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(paramTxtPath)))) {
			bw.write("broj kvadrata: " + numOfRect + "\n");
			bw.write("velicina populacije: " + populationSize + "\n");
			bw.write("maxiter: " + maxiter + "\n");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
