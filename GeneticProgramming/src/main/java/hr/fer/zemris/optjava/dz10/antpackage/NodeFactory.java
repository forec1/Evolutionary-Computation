package hr.fer.zemris.optjava.dz10.antpackage;

import java.util.Random;

import hr.fer.zemris.optjava.dz10.antpackage.tree.FunctionNode;
import hr.fer.zemris.optjava.dz10.antpackage.tree.IfFoodAheadNode;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Left;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Move;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Node;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Prog2;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Prog3;
import hr.fer.zemris.optjava.dz10.antpackage.tree.Right;
import hr.fer.zemris.optjava.dz10.antpackage.tree.TerminalNode;

public class NodeFactory {
	
	public static FunctionNode getRandomFunctionNode() {
		Random rand = new Random();
		int index = rand.nextInt(3);
		if(index == 0) {
			return new IfFoodAheadNode();
		} else if (index == 1) {
			return new Prog2();
		} else {
			return new Prog3();
		}
	}
	
	public static TerminalNode getRandomTerminalNode() {
		Random rand = new Random();
		int index = rand.nextInt(3);
		if(index == 0) {
			return new Move();
		} else if (index == 1) {
			return new Right();
		} else {
			return new Left();
		}
	}
	
	public static Node getRandomNode() {
		Random rand = new Random();
		int index = rand.nextInt(6);
		if(index == 0) {
			return new IfFoodAheadNode();
		} else if (index == 1) {
			return new Prog2();
		} else if (index == 2){
			return new Prog3();
		} else if(index == 3) {
			return new Move();
		} else if (index == 4) {
			return new Right();
		} else {
			return new Left();
		}
	}
}
