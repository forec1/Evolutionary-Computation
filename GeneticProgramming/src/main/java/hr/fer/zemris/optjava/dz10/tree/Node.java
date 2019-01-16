package hr.fer.zemris.optjava.dz10.tree;

public abstract class Node {
	
	protected Node leftChild;
	protected Node rightChild;
	protected boolean isOnlyOneChild;
	protected int depthOfChildren;
	protected int upperDepth;
	protected int numberOfChildren;
	
	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

	public boolean isOnlyOneChild() {
		return isOnlyOneChild;
	}

	public void setDepthOfChildren(int depth) {
		this.depthOfChildren = depth;
	}
	
	public void setUpperDepth(int upperDepth) {
		this.upperDepth = upperDepth;
	}
	
	public int getUpperDepth() {
		return upperDepth;
	}
	
	public int getDepthOfChildren() {
		return depthOfChildren;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}
	
	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}
	
	public abstract double calculate();
	public abstract Node duplicate();
	public abstract void print(String prefix, boolean isTail);
	public void print() {
		print("", true);
	}
}
