package hr.fer.zemris.optjava.dz5.part2;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz5.part2.function.IFunction;

public class GeneticAlgorithm {
	
	private IFunction function;
	private int functionNumOfVars;
	private Permutation[] population;
	private int populationSize;
	private int numberOfSubpopulations;
	private double compFactor;
	private Random rand;
	private double succRatio;
	private double maxSelPress;
	private int maxGen;
	
	public GeneticAlgorithm(IFunction function, int populationSize, int numberOfSubpopulation, double compFactor, double succRatio,
			double maxSelPress, int maxGen) {
		this.function = function;
		this.functionNumOfVars = function.getNumOfVars();
		this.populationSize = populationSize;
		this.numberOfSubpopulations = numberOfSubpopulation;
		this.compFactor = compFactor;
		this.population = new Permutation[populationSize];
		this.succRatio = succRatio;
		this.maxSelPress = maxSelPress;
		this.maxGen = maxGen;
		this.rand = new Random();
		for(int i = 0; i < populationSize; i++) {
			population[i] = new Permutation(functionNumOfVars);
		}
	}
	
	public void run() {
		randomize();
		while(numberOfSubpopulations > 0) {
			List<Permutation> solutions = new LinkedList<>();
			int sizeOfSubPop = populationSize / numberOfSubpopulations;
			int offset = 0;
			for(int i = 0; i < numberOfSubpopulations; i++) {
				int endIndex = offset + sizeOfSubPop > populationSize ? populationSize : offset + sizeOfSubPop;
				Permutation[] myPopulation = Arrays.copyOfRange(population, offset, endIndex);
				offset += sizeOfSubPop;
				solutions.add(offspringSelectionAlg(myPopulation));
				shiftPopulation(3);
			}
			for(int i = 0; i < numberOfSubpopulations; i++) {
				System.out.println("SubPop: " + i + " | Solution: " + solutions.get(i));
			}
			numberOfSubpopulations--;
		}
	}
	
	private void shiftPopulation(int n) {
		Permutation[] firstCut = Arrays.copyOfRange(population, 0, populationSize - n);
		Permutation[] secondCut = Arrays.copyOfRange(population, populationSize - n, populationSize);
		for(int i = 0; i < n; i++) {
			population[i] = secondCut[i];
		}
		for(int i = n; i < populationSize; i++) {
			population[i] = firstCut[i - n];
		}
 	}
	
	private Permutation offspringSelectionAlg(Permutation[] myPopulation) {
		Permutation[] newPopulation = new Permutation[myPopulation.length];
		Permutation[] pool = new Permutation[(int)((1-succRatio)*myPopulation.length)];
		Permutation firstParent;
		Permutation secondParent;
		Permutation[] children;
		int numOfGen = 0;
		while(true) {
			int added = 0;
			int numOfSuccChildren = 0;
			int poolIndex = 0;
			int numOfChildren = 0;
			Permutation best = getBest(myPopulation);
			newPopulation[0] = best;
			added++; numOfSuccChildren++; numOfChildren++;
			while(added < populationSize) {
				firstParent = tournament(3);
				secondParent = tournament(3);
				children = orderCrossover(firstParent, secondParent);
				children[0] = exchangeMutation(children[0]);
				children[1] = exchangeMutation(children[1]);
				if(succRatio(numOfSuccChildren) < succRatio) {
					
					if(isSuccessful(firstParent, secondParent, children[0])) {
						if(added >= myPopulation.length) { break; }
						if(add(newPopulation, children[0], added)) { 
							added++; 
							numOfSuccChildren++; 
							numOfChildren++;
						}
					} else { 
						if(poolIndex >= pool.length) { poolIndex = 0; }
						if(add(pool, children[1], poolIndex)) { poolIndex++; numOfChildren++; }
					}
					
					if(isSuccessful(firstParent, secondParent, children[1])) {
						if(added >= myPopulation.length) { break; }
						if(add(newPopulation, children[1], added)) { 
							added++; 
							numOfSuccChildren++;
							numOfChildren++;
						}
					} else { 
						if(poolIndex >= pool.length) { poolIndex = 0; }
						if(add(pool, children[1], poolIndex)) { poolIndex++; numOfChildren++; }
					}
					
					if(checkConvergence(numOfChildren)) { return getBest(myPopulation); }
				//load pool	
				} else {
					poolIndex = 0;
					while(pool[poolIndex] != null) {
						if(add(newPopulation, pool[poolIndex], added)) { added++; }
						poolIndex++;
						if(poolIndex >= pool.length) { break; }
					}
					if(added != newPopulation.length) {
						while(true) {
							firstParent = tournament(3);
							secondParent = tournament(3);
							children = orderCrossover(firstParent, secondParent);
							if(added >= newPopulation.length) { break; }
							if(add(newPopulation, children[0], added)) { added++; }
							if(added >= newPopulation.length) { break; }
							if(add(newPopulation, children[1], added)) { added++; } 
						}
					}
					break;
				}
			}
			System.out.format("Succ children = %2d | Total children = %3d | ", numOfSuccChildren, numOfChildren);
			nextGeneration(newPopulation, myPopulation);
			evaluate();
			printStatus(myPopulation);
			numOfGen++;
			if(numOfGen >= maxGen) { return getBest(myPopulation); }
		}
	}
	
	private Permutation getBest(Permutation[] myPopulation) {
		Arrays.sort(myPopulation, (o1, o2) -> {
			if(o1.fitness == o2.fitness) {
				return 0;
			} else if(o1.fitness > o2.fitness) {
				return -1;
			} else {
				return 1;
			}
		});
		return myPopulation[0];
	}
	
	private boolean add(Permutation[] p, Permutation child, int index) {
		if(index >= p.length) { return false; }
		if(!containsPermutation(p, child)) {
			p[index] = child;
			return true;
		}
		return false;
	}
	
	private boolean containsPermutation(Permutation[] permutations, Permutation x) {
		for(Permutation permutation : permutations) {
			if(permutation == null) { break; }
			if(permutation.equals(x)) { return true; }
		}
		return false;
	}
	
	private Permutation exchangeMutation(Permutation p) {
		int index1, index2;
		do {
			index1 = rand.nextInt(functionNumOfVars);
			index2 = rand.nextInt(functionNumOfVars);
		} while(index1 == index2);
		int temp = p.solution[index1];
		p.solution[index1] = p.solution[index2];
		p.solution[index2] = temp;
		return p;
	}
	
	private void randomize() {
		List<Integer> list = new LinkedList<>();
		for(int i = 1; i <= functionNumOfVars; i++) {
			list.add(i);
		}
		for(int i = 0; i < populationSize; i++) {
			Collections.shuffle(list);
			for(int j = 0; j < functionNumOfVars; j++) {
				population[i].solution[j] = list.get(j);
			}
			population[i].fitness = -1 * function.valueAt(population[i].solution);
		}
	}
	
	private void nextGeneration(Permutation[] newPopulation, Permutation[] myPopulation) {
		for(int i = 0; i < myPopulation.length; i++) {
			myPopulation[i] = newPopulation[i];
		}
	}
	
	private boolean checkConvergence(int numOfChildren) {
		//System.out.println("children = " + numOfChildren + " max = " + maxSelPress * populationSize);
		return numOfChildren > maxSelPress * populationSize ? true : false;
	}
	
	private Permutation[] orderCrossover(Permutation firstParent, Permutation secondParent) {
		Permutation[] children = new Permutation[2];
		int pos1, pos2;
		do {
			pos1 = rand.nextInt(functionNumOfVars);
			pos2 = rand.nextInt(functionNumOfVars);
		} while (pos2 <= pos1);
		children[0] = new Permutation(functionNumOfVars);
		children[1] = new Permutation(functionNumOfVars);
		for(int i = pos1; i < pos2; i++) {
			children[0].solution[i] = firstParent.solution[i];
			children[1].solution[i] = secondParent.solution[i];
		}
		int setIndex = pos2;
		int getIndex = pos2;
		while(setIndex != pos1) {
			if(!contains(children[0].solution, secondParent.solution[getIndex]) && children[0].solution[setIndex] == -1) {
				children[0].solution[setIndex++] = secondParent.solution[getIndex];
			}
			getIndex++;
			if(setIndex == functionNumOfVars) {
				setIndex = 0;
			}
			if(getIndex == functionNumOfVars) {
				getIndex = 0;
			}
		}
		setIndex = pos2;
		getIndex = pos2;
		while(setIndex != pos1) {
			if(!contains(children[1].solution, firstParent.solution[getIndex]) && children[1].solution[setIndex] == -1) {
				children[1].solution[setIndex++] = firstParent.solution[getIndex];
			}
			getIndex++;
			if(setIndex == functionNumOfVars) {
				setIndex = 0;
			}
			if(getIndex == functionNumOfVars) {
				getIndex = 0;
			}
		}
		children[0].fitness = -1 * function.valueAt(children[0].solution);
		children[1].fitness = -1 * function.valueAt(children[1].solution);
		return children;
	}
	
	private Permutation tournament(int n) {
		int[] indexes = new int[n];
		Random rand = new Random();
		int genereted = 0;
		while(genereted < n) {
			int temp = rand.nextInt(populationSize);
			if(!contains(indexes, temp)) {
				indexes[genereted++] = temp;
			}
		}
		Permutation winner = population[indexes[0]];
		for(int i = 1; i < n; i++) {
			if(population[indexes[i]].fitness > winner.fitness) {
				winner = population[indexes[i]];
			}
		}
		return winner;
	}
	
	private boolean contains(int[] array, int x) {
		for(int a : array) {
			if(a == x) { return true; }
		}
		return false;
	}
	
	private void printStatus(Permutation[] myPopulation) {
		Permutation best = myPopulation[0];
		for(int i = 1; i < myPopulation.length; i++) {
			if(myPopulation[i].fitness > best.fitness) {
				best = myPopulation[i];
			}
		}
		System.out.println(best + " | fitness = " + best.fitness);
	}
	
	private void evaluate() {
		for(Permutation permutation : population) {
			permutation.fitness = -1 * function.valueAt(permutation.solution);
		}
	}
	
	private double succRatio(int succChildren) {
		return (double)succChildren / populationSize;
	}
	
	private boolean isSuccessful(Permutation firstParent, Permutation secondParent, Permutation child) {
		Permutation bestParent = firstParent.fitness > secondParent.fitness ? firstParent : secondParent;
		Permutation worstParent = firstParent.fitness < secondParent.fitness ? firstParent : secondParent;
		if(child.fitness < worstParent.fitness) { return false; }
		double totalDistance = bestParent.fitness - worstParent.fitness;
		double childDistance = child.fitness - worstParent.fitness;
		return childDistance / totalDistance > compFactor ? true : false;
	}
}
