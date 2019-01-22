package hr.fer.zemris.optjava.dz10.tree;


public class TerminalNode extends Node{

	private double data;
	private boolean isConst;
	private int variableIndex;
	
	public TerminalNode(double data) {
		this.data = data;
		this.isConst = true;
		this.variableIndex = -1;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public TerminalNode(int variableIndex) {
		this.data = 1;
		this.variableIndex = variableIndex;
		this.isConst = false;
		this.leftChild = null;
		this.rightChild = null;
	}
	
	public void setVariable(double[] input) {
		data = isConst ? data : input[variableIndex];
	}
	
	@Override
	public double calculate() {
		return data;
	}
	
	public void print(String prefix, boolean isTail) {
		System.out.println(prefix + (isTail ? "└── " : "├── ") + this + ", " + depthOfChildren + "," + upperDepth);
        if(leftChild != null) {
        	leftChild.print(prefix + (isTail ? "    " : "│   "), false);
        }
        if(rightChild != null) {
        	rightChild.print(prefix + (isTail ?"    " : "│   "), true);
        }
    }
	
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof TerminalNode)) {
//			return false;
//		}
//		TerminalNode node = (TerminalNode) obj;
////		if(isConst) {
////			if(node.isConst && data == node.data) {
////				return true;
////			} else {
////				return false;
////			}
////		} else {
////			return variableIndex == node.variableIndex;
////		}
//		return this.equals(node);
//	}

	@Override
	public Node duplicate() {
		TerminalNode newNode = isConst ? new TerminalNode(data) : new TerminalNode(variableIndex);
		newNode.depthOfChildren = this.depthOfChildren;
		newNode.upperDepth = this.upperDepth;
		newNode.numberOfChildren = this.numberOfChildren;
		return newNode;
	}
	
	@Override
	public String toString() {
		return isConst ? Double.toString(data) : "x" + Integer.toString(variableIndex);
	}
	
	public String toStringWithBracket() {
		return isConst ? Double.toString(data) + ")" : "x" + Integer.toString(variableIndex) + ")";
	}

}
