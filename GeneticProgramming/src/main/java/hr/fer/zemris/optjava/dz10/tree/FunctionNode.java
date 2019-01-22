package hr.fer.zemris.optjava.dz10.tree;

import java.util.LinkedList;
import java.util.List;

public class FunctionNode extends Node{
	
	private IOperation operation;
//	private String name;
	
	public FunctionNode(IOperation operation, boolean isOnlyOneChild) {
		this.operation = operation;
		this.isOnlyOneChild = isOnlyOneChild;
//		this.name = name;
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
	 
	 public List<TerminalNode> getTerminals() {
		 List<TerminalNode> terminals = new LinkedList<>();
		 getTerminal(this, terminals);
		 return terminals;
	 }
	 
	 private void getTerminal(Node node, List<TerminalNode> terminals) {
		 if(node == null) { return; }
		 if(node instanceof TerminalNode) {
			 terminals.add((TerminalNode)node);
		 }
		 getTerminal(node.leftChild, terminals);
		 getTerminal(node.rightChild, terminals);
	 }

	@Override
	public double calculate() {
		double leftValue = this.leftChild.calculate();
		double rightValue = 0.0;
		if(rightChild != null) {
			rightValue = this.rightChild.calculate();
		}
		
		return operation.doOperation(leftValue, rightValue);
	}

	@Override
	public Node duplicate() {
		FunctionNode newNode = new FunctionNode(operation, isOnlyOneChild);
		newNode.depthOfChildren = this.depthOfChildren;
		newNode.upperDepth = this.upperDepth;
		newNode.numberOfChildren = this.numberOfChildren;
		return newNode;
	}
	
	@Override
	public String toString() {
		double result = operation.doOperation(3.0, 2.0);
		if(result == 5) { return "+"; }
		if(result == 1) { return "-"; }
		if(result == 6) { return "*"; }
		if(result == (3.0 / 2.0)) { return "/"; }
		if(result == Math.sin(3.0)) { return "sin"; }
		if(result == Math.cos(3.0)) { return "cos"; }
		if(result == Math.sqrt(3.0)) { return "sqrt"; }
		if(result == Math.log(3.0)) { return "log"; }
		return "exp";
	}
}
