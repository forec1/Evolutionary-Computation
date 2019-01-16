package hr.fer.zemris.optjava.dz10.antpackage;

import hr.fer.zemris.optjava.dz10.antpackage.tree.Tree;

public class Ant {
	
	private int currentRow;
	private int currentColumn;
	private AntDirection direction;
	private int eatenFood;
	private int eatenFoodToBeat;
	private int actionsLeft;
	private double fitness;
	private Tree brain;
	
	public Ant(int numberOfActions, Tree brain) {
		currentColumn = 0;
		currentRow = 0;
		direction = AntDirection.RIGHT;
		eatenFood = 0;
		actionsLeft = numberOfActions;
		this.brain = brain;
	}
	
	public void setActionsLeft(int actionsLeft) {
		this.actionsLeft = actionsLeft;
	}
	
	public Tree getBrain() {
		return brain;
	}
	
	public int getEatenFoodToBeat() {
		return eatenFoodToBeat;
	}

	public void setEatenFoodToBeat(int eatenFoodToBeat) {
		this.eatenFoodToBeat = eatenFoodToBeat;
	}

	public double getFitness() {
		return fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public AntDirection getDirection() {
		return direction;
	}
	
	public int getCurrentRow() {
		return currentRow;
	}

	public int getCurrentColumn() {
		return currentColumn;
	}

	public int getEatenFood() {
		return eatenFood;
	}

	public int getActionsLeft() {
		return actionsLeft;
	}

	public void left() {
		if(actionsLeft <= 0) {
			return;
		}
		if (direction == AntDirection.UP) {
			direction = AntDirection.LEFT;
		} else if (direction == AntDirection.DOWN) {
			direction = AntDirection.LEFT;
		} else if (direction == AntDirection.LEFT) {
			direction = AntDirection.DOWN;
		} else {
			direction = AntDirection.UP;
		}
		--actionsLeft;
	}
	
	public void right() {
		if(actionsLeft <= 0) {
			return;
		}
		if (direction == AntDirection.UP) {
			direction = AntDirection.RIGHT;
		} else if (direction == AntDirection.DOWN) {
			direction = AntDirection.RIGHT;
		} else if (direction == AntDirection.LEFT) {
			direction = AntDirection.UP;
		} else {
			direction = AntDirection.DOWN;
		}
		--actionsLeft;
	}

	public void move(World world) {
		if(actionsLeft <= 0) {
			return;
		}	
		checkForFoodAndEat(world);
	}
	
	private void checkForFoodAndEat(World world) {
		int[] newPosition = world.getNewPosition(this);
		currentRow = newPosition[0];
		currentColumn = newPosition[1];
		int now = world.whatIsNow(this);
		if(now == 1) {
			world.update(this, 2);
			++eatenFood;
		}
		--actionsLeft;
	}
	
	public void runAnthonyRun(World world) {
		while(actionsLeft > 0) {
			brain.doStuff(this, world);
		}
		fitness = eatenFood;
	}
	
	public void runAnthonyRunPrint(World world) {
		while(actionsLeft > 0) {
			brain.doStuff(this, world);
			world.print();
			System.out.println();
		}
		fitness = eatenFood;
	}
	
	public Ant duplicate() {
		Ant newAnt = new Ant(actionsLeft, brain.duplicate());
		return newAnt;
	}
	
	@Override
	public String toString() {
		return brain.toString();
	}
}
