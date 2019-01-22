package hr.fer.zemris.optjava.dz10.antpackage.tree;

import hr.fer.zemris.optjava.dz10.antpackage.Ant;
import hr.fer.zemris.optjava.dz10.antpackage.World;

public abstract class FunctionNode extends Node{

	public abstract Node duplicate();
	public abstract boolean doWhatYouAre(Ant ant, World world);
	
}
