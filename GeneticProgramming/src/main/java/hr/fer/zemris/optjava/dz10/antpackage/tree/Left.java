package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public class Left extends TerminalNode {

	public Left() {
		firstChild = secondChild = thirdChild = null;
	}
	
	@Override
	public Node duplicate() {
		return new Left();
	}

	@Override
	public boolean doWhatYouAre(Ant ant, World world) {
		ant.left();
		return false;
	}

	@Override
	public String visit(Node node) {
		return "Left()";
	}

}
