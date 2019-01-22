package hr.fer.zemris.optjava.dz10.antpackage;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz10.antpackage.tree.FunctionNode;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Node;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Prog3;
import hr.fer.zemris.optjava.dz10.antpackage.tree.TerminalNode;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Tree;

public class GeneticAlgorithm {

	private int populationSize;
	private int maxDepth;
	private int maxiter;
	private int maxChildren;
	private int numberOfActions;
	private List<Ant> population;
	private Random rand;
	private int tournamentSize;
	private double mutationProb;
	private World world;
	private double minFit;
	
	public GeneticAlgorithm(int populationSize, int maxDepth, int maxiter, int maxChildren,
			int tournamentSize, double mutationProb, World world, int numberOfActions, double minFit) {
		this.populationSize = populationSize;
		this.maxDepth = maxDepth;
		this.maxiter = maxiter;
		this.maxChildren = maxChildren;
		this.tournamentSize = tournamentSize;
		this.mutationProb = mutationProb;
		this.numberOfActions = numberOfActions;
		this.world = world;
		this.minFit = minFit;
		this.rand = new Random();
	}
	
	public Ant run() {
		rampedHalfAndHalf();
		firstEvaluate();
		List<Ant> newPopulation = new LinkedList<>();
		Ant best = getBest();
		int iter = 0;
		while(iter < maxiter) {
			++iter;
			Ant iterBest = getBest();
			if(iterBest.getFitness() > best.getFitness()) {
				best = iterBest;
				if(best.getFitness() >= minFit) {
					break;
				}
			}
			if(iterBest.getFitness() == best.getFitness() && 
					iterBest.getBrain().size() < best.getBrain().size()) {
				best = iterBest;
			}
			System.out.println(" iter: " + iter + " best: " + best.getFitness());
			newPopulation.add(best);
			while(newPopulation.size() < populationSize) {
				double probs = rand.nextDouble();
				if(probs < 0.01) {
					newPopulation.add(kTournament(tournamentSize).duplicate()); //reprodukcija
				} else if (probs < mutationProb + 0.01) {
					mutate(newPopulation);
				} else {
					cross(newPopulation);
				}
			}
			population.clear();
			population.addAll(newPopulation);
			newPopulation.clear();
			evaluate();
		}
		return best;
	}
	
	private void rampedHalfAndHalf() {
		population = new LinkedList<>();
		int numberOfDiffernetDepths = maxDepth - 1;
		double percentage = 1.0 / numberOfDiffernetDepths;
		int amountOfTrees = (int)(percentage * populationSize);
		for(int depth = 2; depth <= maxDepth; depth++) {
			int i = 0;
			for(int n = amountOfTrees / 2; i < n; i++) {
				population.add(full(depth));
			}
			for(int j = i; j < amountOfTrees; j++) {
				population.add(grow(depth, false));
			}
		}
	}
	
	private void mutate(List<Ant> newPopulation) {
		Ant antParent = kTournament(tournamentSize);
		Tree parent = antParent.getBrain().duplicate();
		int treeSize = parent.size();
		int nodeIndex;
		nodeIndex = rand.nextInt(treeSize - 1) + 1;
		Node node = parent.getNode(nodeIndex);
		int nodeDepth = node.getUpperDepth();
		int subtreeDepth = maxDepth - nodeDepth;
		Tree subtree = grow(subtreeDepth, true).getBrain();
		Node newNode = subtree.getRoot();
		parent.switchNodes(node, newNode);
		Ant antChild = new Ant(numberOfActions, parent);
		antChild.setEatenFoodToBeat(antParent.getEatenFood());
		newPopulation.add(antChild);
	}
	
	private void cross(List<Ant> newPopulation) {
		Ant antParent1 = kTournament(tournamentSize);
		Ant antParent2 = kTournament(tournamentSize);
		while(antParent1.equals(antParent2)) {
			antParent2 = kTournament(tournamentSize);
		}
		Tree parent1 = antParent1.getBrain();
		Tree parent2 = antParent2.getBrain();
		Tree child1 = parent1.duplicate();
		Tree child2 = parent2.duplicate();
		int treeSize1 = parent1.size();
		int treeSize2 = parent2.size();
		int nodeIndex1, nodeIndex2;
		int iter = 0;
		while(true) {
			++iter;
			if(iter > 10) {
				break;
			}
			nodeIndex1 = rand.nextInt(treeSize1 - 1) + 1;
			nodeIndex2 = rand.nextInt(treeSize2 - 1) + 1;
			Node node1 = child1.getNode(nodeIndex1);
			if(node1.getDepthOfChildren() > maxDepth) {
				System.out.println("FAIL");
			}
			Node node2 = child2.getNode(nodeIndex2);
			int depth1 = node1.getDepthOfChildren();
			int depth2 = node2.getDepthOfChildren();
			int upperDepth1 = node1.getUpperDepth();
			int upperDepth2 = node2.getUpperDepth();
			int numOfChildren1 = node1.getNumberOfChildren();
			int numOfChildren2 = node2.getNumberOfChildren();
			int newChildren1 = numOfChildren2 - numOfChildren1;
			int newChildren2 = numOfChildren1 - numOfChildren2;
			if(upperDepth1 + depth2 > maxDepth || upperDepth2 + depth1 > maxDepth ||
					treeSize1 + newChildren1 > maxChildren || treeSize2 + newChildren2 > maxChildren) {
				continue;
			} else{
				Node tmp1 = node1;		//TODO
				Node tmp2 = node2;		//glupi pokusaj necega
				child1.switchNodes(node1, node2);
				child2.switchNodes(tmp2, tmp1);
				break;
			}
		}
		Ant antChild1 = new Ant(numberOfActions, child1);
		antChild1.setEatenFoodToBeat(antParent1.getEatenFood());
		Ant antChild2 = new Ant(numberOfActions, child2);
		antChild2.setEatenFoodToBeat(antParent2.getEatenFood());
		newPopulation.add(antChild1);
		newPopulation.add(antChild2);
	}
	
	private Ant kTournament(int k) {
		int[] indexes = new int[k];
		int genereted = 0;
		while(genereted < k) {
			int temp = rand.nextInt(population.size());
			if(!contains(indexes, temp)) {
				indexes[genereted++] = temp;
			}
		}
		Ant winner = population.get(indexes[0]);
		for(int i = 1; i < k; i++) {
			if(population.get(indexes[i]).getFitness() > winner.getFitness()) {
				winner = population.get(indexes[i]);
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
	
	private Ant getBest() {
		Ant best = population.get(0);
		for(int i = 0, n = population.size(); i < n; i++) {
			Ant ant = population.get(i);
			if(ant.getFitness() > best.getFitness()) {
				best = ant;
			}
		}
		return best;
	}
	
	private Ant grow(int depth, boolean mutation) {
		FunctionNode functionRoot = null;
		if(mutation) {
			Node root = NodeFactory.getRandomNode();
			if (root instanceof TerminalNode) {
				TerminalNode terminalRoot = (TerminalNode) root;
				Tree tree = new Tree(terminalRoot);
				tree.update();
				return new Ant(numberOfActions, tree);
			} else {
				functionRoot = (FunctionNode) root;
			}
		} else {
			functionRoot = NodeFactory.getRandomFunctionNode();
		}
		Tree tree = new Tree(functionRoot);
		
		List<Node> currentNodes = new LinkedList<>();
		currentNodes.add(functionRoot);
		int currentDepth = 1;
		while(currentDepth < depth - 1) {
			List<Node> newCurrentNodes = new LinkedList<>();
			for(Node currentNode : currentNodes) {
				currentNode.setUpperDepth(currentDepth - 1);
				if(currentNode instanceof FunctionNode) {
					Node firstChild = NodeFactory.getRandomNode();
					Node secondChild = NodeFactory.getRandomNode();
					newCurrentNodes.add(firstChild); newCurrentNodes.add(secondChild);
					Node thirdChild = null;
					if(currentNode instanceof Prog3) {
						thirdChild = NodeFactory.getRandomNode();
						newCurrentNodes.add(thirdChild);
					}
					currentNode.setFirstChild(firstChild);
					currentNode.setSecondChild(secondChild);
					currentNode.setThirdChild(thirdChild);
				}
			}
			currentNodes = newCurrentNodes;
			currentDepth++;
		}
		for(Node currentNode : currentNodes) {
			currentNode.setUpperDepth(currentDepth - 1);
			if(currentNode instanceof FunctionNode) {
				TerminalNode firstChild = NodeFactory.getRandomTerminalNode();
				TerminalNode secondChild = NodeFactory.getRandomTerminalNode();
				firstChild.setUpperDepth(currentDepth); firstChild.setUpperDepth(currentDepth);
				TerminalNode thirdChild = null;
				if(currentNode instanceof Prog3) {
					thirdChild = NodeFactory.getRandomTerminalNode();
					thirdChild.setUpperDepth(currentDepth);
				}
				currentNode.setFirstChild(firstChild);
				currentNode.setSecondChild(secondChild);
				currentNode.setThirdChild(thirdChild);
			}
		}
		tree.update();
		return new Ant(numberOfActions, tree);
	}
	
	private Ant full(int depth) {
		FunctionNode root = NodeFactory.getRandomFunctionNode();
		Tree brain = new Tree(root);
		List<Node> currentNodes = new LinkedList<>();
		currentNodes.add(root);
		int currentDepth = 1;
		while(currentDepth < depth - 1) {
			List<Node> newCurrentNodes = new LinkedList<>();
			for(Node currentNode : currentNodes) {
				currentNode.setUpperDepth(currentDepth - 1);
				FunctionNode firstChild = NodeFactory.getRandomFunctionNode();
				FunctionNode secondChild = NodeFactory.getRandomFunctionNode();
				newCurrentNodes.add(firstChild); newCurrentNodes.add(secondChild);
				FunctionNode thirdChild = null;
				if(currentNode instanceof Prog3) {
					thirdChild = NodeFactory.getRandomFunctionNode();
					newCurrentNodes.add(thirdChild);
				}
				currentNode.setFirstChild(firstChild);
				currentNode.setSecondChild(secondChild);
				currentNode.setThirdChild(thirdChild);
			}
			currentNodes = newCurrentNodes;
			currentDepth++;
		}
		
		for(Node currentNode : currentNodes) {
			currentNode.setUpperDepth(currentDepth - 1);
			TerminalNode firstChild = NodeFactory.getRandomTerminalNode();
			TerminalNode secondChild = NodeFactory.getRandomTerminalNode();
			firstChild.setUpperDepth(currentDepth); secondChild.setUpperDepth(currentDepth);
			TerminalNode thirdChild = null;
			if(currentNode instanceof Prog3) {
				thirdChild = NodeFactory.getRandomTerminalNode();
				thirdChild.setUpperDepth(currentDepth);
			}
			currentNode.setFirstChild(firstChild);
			currentNode.setSecondChild(secondChild);
			currentNode.setThirdChild(thirdChild);
		}
		brain.update();
		return new Ant(numberOfActions, brain);
	}
	
	private void evaluate() {
		for(Ant ant : population) {
			ant.runAnthonyRun(world);
			world.refresh();
			if(ant.getEatenFood() > ant.getEatenFoodToBeat()) {
				ant.setFitness(ant.getFitness() * 0.9);
			}
		}
	}
	
	private void firstEvaluate() {
		for(Ant ant : population) {
			ant.runAnthonyRun(world);
			world.refresh();
		}
	}
}
