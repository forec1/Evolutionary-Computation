package hr.fer.zemris.optjava.dz10.antpackage.tree;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public class Tree {

	private Node root;
	private int size;
	
	public Tree(Node root) {
		this.root = root;
	}
	
	public Node getRoot() {
		return root;
	}
	
	public int size() {
		return size;
	}
	
	public void doStuff(Ant ant, World world) {
		root.doWhatYouAre(ant, world);
	}
	
	public void switchNodes(Node myNode, Node newNode) {
		switchNodes(root, myNode, newNode);
		this.update();
	}
	
	private void updateUpperDepth(Node node, int depth) {
		if(node == null) { return; }
		node.setUpperDepth(depth);
		updateUpperDepth(node.firstChild, depth + 1);
		updateUpperDepth(node.secondChild, depth + 1);
	}
	
	private void switchNodes(Node node, Node myNode, Node newNode) {
		if(node == null) {
			return;
		}
		boolean switched = false;
		if(node.firstChild != null) {
			if(node.firstChild.equals(myNode)) {
				node.firstChild = newNode;
				updateUpperDepth(newNode, node.upperDepth + 1);
				switched = true;
				size += newNode.numberOfChildren;
				size -= myNode.numberOfChildren;
			}
		} 
		if(node.secondChild != null) {
			if(node.secondChild.equals(myNode)) {
				node.secondChild = newNode;
				updateUpperDepth(newNode, node.upperDepth + 1);
				switched = true;
				size += newNode.numberOfChildren;
				size -= myNode.numberOfChildren;
			}
		}
		if(node.thirdChild != null) {
			if(node.thirdChild.equals(myNode)) {
				node.thirdChild = newNode;
				updateUpperDepth(newNode, node.upperDepth + 1);
				switched = true;
				size += newNode.numberOfChildren;
				size -= myNode.numberOfChildren;
			}
		} 
		if(!switched) {
			switchNodes(node.firstChild, myNode, newNode);
			switchNodes(node.secondChild, myNode, newNode);
			switchNodes(node.thirdChild, myNode, newNode);
		}
	}
	
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
		makeList(node.firstChild, list);
		makeList(node.secondChild, list);
		makeList(node.thirdChild, list);
	}
	
	public Tree duplicate() {
		Node newRoot = root.duplicate();
		duplicate(root, newRoot);
		Tree newTree = new Tree(newRoot);
		newTree.size = this.size;
		return newTree;
	}
	
	private void duplicate(Node node, Node newNode) {
		if(node == null) {
			return;
		}
		if(node.firstChild != null) {
			newNode.setFirstChild(node.firstChild.duplicate());
		}
		if(node.secondChild != null) {
			newNode.setSecondChild(node.secondChild.duplicate());
		}
		if(node.thirdChild != null) {
			newNode.setThirdChild(node.thirdChild.duplicate());
		}
		duplicate(node.firstChild, newNode.firstChild);
		duplicate(node.secondChild, newNode.secondChild);
		duplicate(node.thirdChild, newNode.thirdChild);
	}
	
	private int depth(Node node) {
		if(node == null) {
			return 0;
		}
		int depth = 0;
		depth = Math.max(depth(node.firstChild), depth);
		depth = Math.max(depth(node.secondChild), depth);
		depth = Math.max(depth(node.thirdChild), depth);
		node.setDepthOfChildren(depth);
		return depth + 1;
}
	
	public void update() {
		updateSize();
		depth(root);
	}
	
	private void updateSize() {
		size = 0;
		updateSize(root);
	}
	
	private void updateSize(Node node) {
		if(node == null) {
			return;
		}
		size++;
		updateSize(node.firstChild);
		updateSize(node.secondChild);
		updateSize(node.thirdChild);
		
	}
	
	@Override
	public String toString() {
		return root.visit(root);
	}
}
