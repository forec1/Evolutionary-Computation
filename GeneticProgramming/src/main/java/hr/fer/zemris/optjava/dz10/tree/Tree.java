package hr.fer.zemris.optjava.dz10.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tree{
	
	private Node root;
	private List<TerminalNode> terminalNodes;
	private double fitness;
	private int size;
	
	public Tree(Node root) {
		this.root = root;
		this.terminalNodes = new ArrayList<>();
	}
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public Node getRoot() {
		return root;
	}

	public double calculate() {
		return root.calculate();
	}
	
	public List<TerminalNode> getTerminalNodes() {
		return terminalNodes;
	}
	
	public void removeTerminals(Node node) {
		if(node == null) { return; }
		terminalNodes.remove(node);
		removeTerminals(node.leftChild);
		removeTerminals(node.rightChild);
	}
	
	public void addTerminals(Node node) {
		if(node == null) { return; }
		if(node instanceof TerminalNode) {
			addTerminalNode((TerminalNode)node);
		}
		addTerminals(node.leftChild);
		addTerminals(node.rightChild);
	}
	
	public void setTerminalNodes(double[] input) {
		for(TerminalNode node : terminalNodes) {
			node.setVariable(input);
		}
	}
	
	public void switchNodes(Node myNode, Node newNode) {
		switchNodes(root, myNode, newNode);
		this.terminalNodes = ((FunctionNode) root).getTerminals();
		this.update();
	}
	
	private void updateUpperDepth(Node node, int depth) {
		if(node == null) { return; }
		node.setUpperDepth(depth);
		updateUpperDepth(node.leftChild, depth + 1);
		updateUpperDepth(node.rightChild, depth + 1);
	}
	
	private void switchNodes(Node node, Node myNode, Node newNode) {
		if(node == null) {
			return;
		}
		boolean switched = false;
		if(node.leftChild != null) {
			if(node.leftChild.equals(myNode)) {
				node.leftChild = newNode;
				updateUpperDepth(newNode, node.upperDepth + 1);
				switched = true;
				size += newNode.numberOfChildren;
				size -= myNode.numberOfChildren;
			}
		} 
		if(node.rightChild != null) {
			if(node.rightChild.equals(myNode)) {
				node.rightChild = newNode;
				updateUpperDepth(newNode, node.upperDepth + 1);
				switched = true;
				size += newNode.numberOfChildren;
				size -= myNode.numberOfChildren;
			}
		}
		if(!switched) {
			switchNodes(node.leftChild, myNode, newNode);
			switchNodes(node.rightChild, myNode, newNode);
		}
	}
	
	public void addTerminalNode(TerminalNode node) {
		terminalNodes.add(node);
	}
	
//	public int depth() {
//		return depth(root);
//	}
	
	private int depth(Node node) {
		if(node == null) {
			return 0;
		} else {
			int lDepth = depth(node.leftChild);
			int rDepth = depth(node.rightChild);
			int depth = Math.max(lDepth, rDepth) + 1;
			node.setDepthOfChildren(depth - 1);
			return depth;
		}
	}
	
	public Tree duplicate() {
		Node newRoot = root.duplicate();
		duplicate(root, newRoot);
		Tree newTree = new Tree(newRoot);
		newTree.fitness = this.fitness;
		newTree.size = this.size;
		newTree.terminalNodes = new LinkedList<>();
		newTree.terminalNodes.addAll(this.terminalNodes);
		return newTree;
	}
	
	private void duplicate(Node node, Node newNode) {
		if(node == null) {
			return;
		}
		if(node.leftChild != null) {
			newNode.setLeftChild(node.leftChild.duplicate());
		}
		if(node.rightChild != null) {
			newNode.setRightChild(node.rightChild.duplicate());
		}
		duplicate(node.leftChild, newNode.leftChild);
		duplicate(node.rightChild, newNode.rightChild);
	}
//	
//	public void updateDepths() {
//		depth(root);
//	}
//	
	public Node getNode(int index) {
		List<Node> list = new LinkedList<>();
		makeList(root, list);
		return list.get(index);
	}
	
	private void makeList(Node node, List<Node> list) {
		if(node == null) {
			return;
		}
		list.add(node);
		makeList(node.leftChild, list);
		makeList(node.rightChild, list);
	}
	
	public int size() {
		return size;
	}
	
	private int size(Node node) {
		if(node == null) {
			return 0;
		} else {
			int numOfChildren = (size(node.leftChild) + 1 + size(node.rightChild));
			node.setNumberOfChildren(numOfChildren - 1);
			return numOfChildren;
		}
	}
//	
//	public void updateNumberOfChildren() {
//		size(root);
//	}
	
	public void update() {
		this.size = size(root);
		depth(root);
	}
	
	public void printInorder() {
		printInorder(root);
	}
	
	public void printSymbolic() {
		printSymbolic(root);
	}
	
	private void printSymbolic(Node node) {
		if(node == null) { return; }
		if(node.isOnlyOneChild()) {
			System.out.print(node + "(");
			if(node.leftChild instanceof TerminalNode) {
				printTerminalWithBracket(node.leftChild);
			} else {
				printSymbolic(node.leftChild);
			}
			printSymbolic(node.rightChild);
		} else {
			printSymbolic(node.leftChild);
			System.out.print(node + " ");
			printSymbolic(node.rightChild);
		}
	}
	
	private void printTerminalWithBracket(Node node) {
		System.out.print(((TerminalNode)node).toStringWithBracket() + " ");
	}
	
	private void printInorder(Node node) {
		if(node == null) {
			return;
		}
		printInorder(node.leftChild);
		System.out.print(node + " ");
		printInorder(node.rightChild);
	}
	
	public void printPreorder() {
		printPreorder(root);
	}
	
	private void printPreorder(Node node) {
		if(node == null) {
			return;
		}
		System.out.print(node + " ");
		printPreorder(node.leftChild);
		printPreorder(node.rightChild);
	}
	
	public void print() {
		root.print();
	}
}
