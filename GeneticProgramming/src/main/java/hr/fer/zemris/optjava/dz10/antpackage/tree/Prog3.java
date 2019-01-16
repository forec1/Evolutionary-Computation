package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public class Prog3 extends FunctionNode {

	@Override
	public Node duplicate() {
		return new Prog3();
	}

	@Override
	public boolean doWhatYouAre(Ant ant, World world) {
		boolean ret;
		ret = firstChild.doWhatYouAre(ant, world);
//		if(!ret) {
			ret = secondChild.doWhatYouAre(ant, world);
//		}
//		if(!ret) {
			ret = thirdChild.doWhatYouAre(ant, world);
//		}
		return ret;
	}

	@Override
	public String visit(Node node) {
		String ret = "PROG3(";
		ret += firstChild.visit(firstChild);
		ret += ", ";
		ret += secondChild.visit(secondChild);
		ret += ", ";
		ret += thirdChild.visit(thirdChild);
		ret += ")";
		return ret;
	}
}
