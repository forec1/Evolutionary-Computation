package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public abstract class Node {
	
	protected Node firstChild;
	protected Node secondChild;
	protected Node thirdChild;
	protected int depthOfChildren;
	protected int upperDepth;
	protected int numberOfChildren;
	
	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
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
	
	public Node getFirstChild() {
		return firstChild;
	}

	public void setFirstChild(Node firstChild) {
		this.firstChild = firstChild;
	}

	public Node getSecondChild() {
		return secondChild;
	}

	public void setSecondChild(Node secondChild) {
		this.secondChild = secondChild;
	}

	public Node getThirdChild() {
		return thirdChild;
	}

	public void setThirdChild(Node thirdChild) {
		this.thirdChild = thirdChild;
	}
	
	public abstract String visit(Node node);
	public abstract boolean doWhatYouAre(Ant ant, World world);
	public abstract Node duplicate();
}
