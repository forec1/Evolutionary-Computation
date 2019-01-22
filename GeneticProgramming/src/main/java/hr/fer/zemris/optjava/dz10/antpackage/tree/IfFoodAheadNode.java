package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public class IfFoodAheadNode extends FunctionNode{
	
	@Override
	public boolean doWhatYouAre(Ant ant, World world) {
		int next = world.whatIsNext(ant);
		boolean ret;
		if(next == 1) {
			ret = firstChild.doWhatYouAre(ant, world);
		} else {
			ret = secondChild.doWhatYouAre(ant, world);
		}
		return ret;
	}

	@Override
	public Node duplicate() {
		return new IfFoodAheadNode();
	}

	@Override
	public String visit(Node node) {
		String ret = "IF(";
		ret += firstChild.visit(firstChild);
		ret += ", ";
		ret += secondChild.visit(secondChild);
		ret += ")";
		return ret;
	}
}
