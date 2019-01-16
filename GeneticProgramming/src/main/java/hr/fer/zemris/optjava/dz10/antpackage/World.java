package hr.fer.zemris.optjava.dz10.antpackage;

import java.util.Arrays;

public class World {
	
	private int[][] world;
	
	public World(int[][] world) {
		this.world = world;
	}
	
	public int[] getNewPosition(Ant ant) {
		int currentRow = ant.getCurrentRow();
		int currentColumn = ant.getCurrentColumn();
		AntDirection direction = ant.getDirection();
		if (direction == AntDirection.UP) {
			--currentRow;
			currentRow = currentRow < 0 ? world.length - 1 : currentRow;
		} else if (direction == AntDirection.DOWN) {
			++currentRow;
			currentRow = currentRow >= world.length ? 0 : currentRow;
		} else if (direction == AntDirection.LEFT) {
			--currentColumn;
			currentColumn = currentColumn < 0 ? world[currentRow].length - 1 : currentColumn;
		} else if (direction == AntDirection.RIGHT) {
			++currentColumn;
			currentColumn = currentColumn >= world[currentRow].length ? 0 : currentColumn;
		}
		return new int[] { currentRow, currentColumn };
	}
	
	public int whatIsNow(Ant ant) {
		return world[ant.getCurrentRow()][ant.getCurrentColumn()];
	}
	
	public void update(Ant ant, int val) {
		set(ant.getCurrentRow(), ant.getCurrentColumn(), val);
	}
	
	private void set(int row, int column, int val) {
		world[row][column] = val;
	}
	
	public int whatIsNext(Ant ant) {
		int[] newPosition = getNewPosition(ant);
		return world[newPosition[0]][newPosition[1]];
	}
	
	public void refresh() {
		for(int i = 0; i < world.length; i++) {
			for(int j = 0; j < world[i].length; j++) {
				if(world[i][j] == 2) {
					set(i, j, 1);
				} else if (world[i][j] == 3) {
					set(i, j, 0);
				}
			}
		}
	}
	
	public void print() {
		for(int i = 0; i < world.length; i++) {
			for(int j = 0; j < world.length; j++) {
				System.out.print(world[i][j]);
			}
			System.out.println();
		}
	}
	
//	public World duplicate() {
//		int[][] world2 = new int[world.length][world[0].length];
//		for(int i = 0; i < world.length; i++) {
//			world2[i] = Arrays.copyOf(world[i], world[i].length);
//		}
//		return new World(world2);
//	}
}
