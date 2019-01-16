package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public class Right extends TerminalNode{

	public Right() {
		firstChild = secondChild = thirdChild = null;
	}
	
	@Override
	public Node duplicate() {
		return new Right();
	}

	@Override
	public boolean doWhatYouAre(Ant ant, World world) {
		ant.right();
		return false;
	}

	@Override
	public String visit(Node node) {
		return "Right()";
	}

}
