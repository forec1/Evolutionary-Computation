package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public class Move extends TerminalNode {

	@Override
	public Node duplicate() {
		return new Move();
	}

	@Override
	public boolean doWhatYouAre(Ant ant, World world) {
		ant.move(world);
		return true;
	}

	@Override
	public String visit(Node node) {
		return "Move()";
	}
}
