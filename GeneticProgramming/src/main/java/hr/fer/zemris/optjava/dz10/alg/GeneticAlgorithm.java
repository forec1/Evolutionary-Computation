package hr.fer.zemris.optjava.dz10.alg;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz10.err.IErrorFunction;
import hr.fer.zemris.optjava.dz10.tree.FunctionNode;
import hr.fer.zemris.optjava.dz10.tree.IOperation;
import hr.fer.zemris.optjava.dz10.tree.Node;
import hr.fer.zemris.optjava.dz10.tree.TerminalNode;
import hr.fer.zemris.optjava.dz10.tree.Tree;

public class GeneticAlgorithm {
	
	private int populationSize;
	private int maxInitDepth;
	private int maxDepth;
	private int numberOfVars;
	private int numOfErrCalc;
	private int maxNumOfErrCalc;
	private int maxChildren;
	private List<Tree> population;
	private String[] operations;
	private double[] constRange;
	private Random rand;
	private IErrorFunction error;
	private int tournamentSize;
	private double mutationProb;
	private int stagnationIter;
	
	public GeneticAlgorithm(int populationSize, int maxInitDepth, int maxDepth, int numberOfVars,
			int maxNumOfErrCalc, int maxChildren, String[] operations, double[] constRange, IErrorFunction error,
			int tournamentSize, double mutationProb, int stagnationIter) {
		this.populationSize = populationSize;
		this.maxInitDepth = maxInitDepth;
		this.maxDepth = maxDepth;
		this.numberOfVars = numberOfVars;
		this.maxNumOfErrCalc = maxNumOfErrCalc;
		this.maxChildren = maxChildren;
		this.operations = operations;
		this.constRange = constRange;
		this.error = error;
		this.tournamentSize = tournamentSize;
		this.mutationProb = mutationProb;
		this.stagnationIter = stagnationIter;
		this.rand = new Random();
	}

	public Tree run() {
		rampedHalfAndHalf();
		evaluate();
		int iter = 0;
		List<Tree> newPopulation = new LinkedList<>();
		Tree best = getBest();
		int sameBest = 0;
		while(numOfErrCalc < maxNumOfErrCalc) {
			++iter;
			Tree iterBest = getBest();
			if(iterBest.getFitness() > best.getFitness()) {
				best = iterBest;
				best.printSymbolic();
				System.out.println(" iter: " + iter + " best: " + -best.getFitness());
				sameBest = 0;
				if(-best.getFitness() < 10e-7) {
					break;
				}
			}
			++sameBest;
			if(sameBest > stagnationIter) {
				break;
			}
			newPopulation.add(best);	//elitistički algoritam
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
		return getBest();
	}
	
	private void mutate(List<Tree> newPopulation) {
		Tree parent = kTournament(tournamentSize).duplicate();
		int treeSize = parent.size();
		int nodeIndex;
		try {
			nodeIndex = rand.nextInt(treeSize - 1) + 1;
		} catch (IllegalArgumentException e) {
			System.out.println(treeSize);
			parent.printInorder();
			throw e;
		}
		Node node = parent.getNode(nodeIndex);
		int nodeDepth = node.getUpperDepth();
		int subtreeDepth = maxDepth - nodeDepth;
		Tree subtree = grow(subtreeDepth, true);
		Node newNode = subtree.getRoot();
		parent.switchNodes(node, newNode);
		newPopulation.add(parent);
	}
	
	private void cross(List<Tree> newPopulation) {
		Tree parent1 = kTournament(tournamentSize);
		Tree parent2 = kTournament(tournamentSize);
		while(parent1.equals(parent2)) {
			parent2 = kTournament(tournamentSize);
		}
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
			try {
				nodeIndex1 = rand.nextInt(treeSize1 - 1) + 1;
				nodeIndex2 = rand.nextInt(treeSize2 - 1) + 1;
			} catch (IllegalArgumentException e) {
				parent1.printInorder();
				System.out.println();
				parent2.printInorder();
				throw e;
			}
			Node node1 = child1.getNode(nodeIndex1);
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
//		child1.update();
//		child2.update();
		newPopulation.add(child1);
		newPopulation.add(child2);
	}
	
	private Tree kTournament(int k) {
		int[] indexes = new int[k];
		int genereted = 0;
		while(genereted < k) {
			int temp = rand.nextInt(population.size());
			if(!contains(indexes, temp)) {
				indexes[genereted++] = temp;
			}
		}
		Tree winner = population.get(indexes[0]);
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
	
	private Tree getBest() {
		Tree max = population.get(0);
		for(int i = 1, n = population.size(); i < n; i++) {
			Tree tree = population.get(i);
			if(tree.getFitness() > max.getFitness()) {
				max = tree;
			}
		}
		return max;
	}
	
	private void evaluate() {
		for(Tree solution : population) {
			error.evaluate(solution);
			++numOfErrCalc;
		}
	}
	
	private void rampedHalfAndHalf() {
		population = new LinkedList<>();
		int numberOfDiffernetDepths = maxInitDepth - 1;
		double percentage = 1.0 / numberOfDiffernetDepths;
		int amountOfTrees = (int)(percentage * populationSize);
		for(int depth = 2; depth <= maxInitDepth; depth++) {
			int i = 0;
			for(int n = amountOfTrees / 2; i < n; i++) {
				population.add(full(depth));
			}
			for(int j = i; j < amountOfTrees; j++) {
				population.add(grow(depth, false));
			}
		}
	}
	
	
	private FunctionNode getFunctionNode(int index) {
		String stringOperation = operations[index];
		IOperation operation = null;
		if(stringOperation.equals("+")) {
			
			operation = (o1, o2) -> o1 + o2;
			return new FunctionNode(operation, false);
			
		} else if(stringOperation.equals("-")) {
			
			operation = (o1, o2) -> o1 - o2;
			return new FunctionNode(operation, false);
			
		} else if(stringOperation.equals("*")) {
			
			operation = (o1, o2) -> o1 * o2;
			return new FunctionNode(operation, false);
			
		} else if(stringOperation.equals("/")) {
			
			operation = (o1, o2) -> {
				if(o2 == 0.0) { return 1; }
				return o1 / o2;
			};
			return new FunctionNode(operation, false);
			
		} else if(stringOperation.equals("sin")) {
			
			operation = (o1, o2) -> Math.sin(o1);
			return new FunctionNode(operation, true);
			
		} else if(stringOperation.equals("cos")) {
			
			operation = (o1, o2) -> Math.cos(o1);
			return new FunctionNode(operation, true);
		} else if(stringOperation.equals("sqrt")) {
			
			operation = (o1, o2) -> {
				if(o1 < 0.0) { return 1; }
				return Math.sqrt(o1);
			};
			return new FunctionNode(operation, true);
			
		} else if(stringOperation.equals("log")) {
			
			operation = (o1, o2) -> {
				if(o1 <= 0.0) { return 1; }
				return Math.log(o1);
			};
			return new FunctionNode(operation, true);
			
		} else if(stringOperation.equals("exp")) {
			
			operation = (o1, o2) -> Math.exp(o1);
			return new FunctionNode(operation, true);
		} else {
			throw new IllegalArgumentException("Undefined operation!");
		}
	}
	
	private TerminalNode getRandomTerminalNode() {
		if(constRange != null) {
			int randIndex = rand.nextInt(numberOfVars + 1);
			if(randIndex == numberOfVars) {
				double data = rand.nextDouble() * (constRange[1] - constRange[0]) + constRange[0];
				return new TerminalNode(data);
			} else {
				return new TerminalNode(randIndex);
			}
		} else {
			int varIndex = rand.nextInt(numberOfVars);
			return new TerminalNode(varIndex);
		}
	}
	
	private TerminalNode getTerminalNode(int index) {
		return new TerminalNode(index);
	}
	
	private TerminalNode getConstNode() {
		double data = rand.nextDouble() * (constRange[1] - constRange[0]) + constRange[0];
		return new TerminalNode(data);
	}
	
	private Node getRandomNode() {
		if(constRange != null) {
			int randIndex = rand.nextInt(operations.length + numberOfVars + 1);
			if(randIndex < operations.length) {
				return getFunctionNode(randIndex);
			} else if (randIndex < operations.length + numberOfVars) {
				return getTerminalNode(randIndex - operations.length);
			} else {
				return getConstNode();
			}
		} else {
			int randIndex = rand.nextInt(operations.length + numberOfVars);
			if(randIndex < operations.length) {
				return getFunctionNode(randIndex);
			} else {
				return getTerminalNode(randIndex - operations.length);
			}
		}
			
	}
	
	private Tree full(int depth) {
		FunctionNode root = getFunctionNode(rand.nextInt(operations.length));
		Tree tree = new Tree(root);
		
		List<Node> currentNodes = new LinkedList<>();
		currentNodes.add(root);
		int currentDepth = 1;
		while(currentDepth < depth - 1) {
			List<Node> newCurrentNodes = new LinkedList<>();
			for(Node currentNode : currentNodes) {
				currentNode.setUpperDepth(currentDepth - 1);
				FunctionNode leftChild = getFunctionNode(rand.nextInt(operations.length));
				newCurrentNodes.add(leftChild);
				FunctionNode rightChild = null;
				if(!currentNode.isOnlyOneChild()) {
					rightChild = getFunctionNode(rand.nextInt(operations.length));
					newCurrentNodes.add(rightChild);
				}
				currentNode.setLeftChild(leftChild);
				currentNode.setRightChild(rightChild);
			}
			currentNodes = newCurrentNodes;
			currentDepth++;
		}
		for(Node currentNode : currentNodes) {
			currentNode.setUpperDepth(currentDepth - 1);
			TerminalNode leftChild = getRandomTerminalNode();
			tree.addTerminalNode(leftChild);
			leftChild.setUpperDepth(currentDepth);
			TerminalNode rightChild = null;
			if(!currentNode.isOnlyOneChild()) {
				rightChild = getRandomTerminalNode();
				rightChild.setUpperDepth(currentDepth);
				tree.addTerminalNode(rightChild);
			}
			currentNode.setLeftChild(leftChild);
			currentNode.setRightChild(rightChild);
		}
		tree.update();
		return tree;
	}
	
	private Tree grow(int depth, boolean mutation) {
		FunctionNode functionRoot = null;
		if(mutation) {
			Node root = getRandomNode();
			if (root instanceof TerminalNode) {
				TerminalNode terminalRoot = (TerminalNode) root;
				Tree tree = new Tree(terminalRoot);
				tree.addTerminalNode(terminalRoot);
				tree.update();
				return tree;
			} else {
				functionRoot = (FunctionNode) root;
			}
		} else {
			functionRoot = getFunctionNode(rand.nextInt(operations.length));
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
					Node leftChild = getRandomNode();
					newCurrentNodes.add(leftChild);
					Node rightChild = null;
					if(!currentNode.isOnlyOneChild()) {
						rightChild = getRandomNode();
						newCurrentNodes.add(rightChild);
					}
					currentNode.setLeftChild(leftChild);
					currentNode.setRightChild(rightChild);
				} else if (currentNode instanceof TerminalNode) {
					tree.addTerminalNode((TerminalNode)currentNode);
				}
			}
			currentNodes = newCurrentNodes;
			currentDepth++;
		}
		for(Node currentNode : currentNodes) {
			currentNode.setUpperDepth(currentDepth - 1);
			if(currentNode instanceof FunctionNode) {
				TerminalNode leftChild = getRandomTerminalNode();
				tree.addTerminalNode(leftChild);
				leftChild.setUpperDepth(currentDepth);
				TerminalNode rightChild = null;
				if(!currentNode.isOnlyOneChild()) {
					rightChild = getRandomTerminalNode();
					tree.addTerminalNode(rightChild);
					rightChild.setUpperDepth(currentDepth);
				}
				currentNode.setLeftChild(leftChild);
				currentNode.setRightChild(rightChild);
			}
		}
		tree.update();
		return tree;
	}
}
