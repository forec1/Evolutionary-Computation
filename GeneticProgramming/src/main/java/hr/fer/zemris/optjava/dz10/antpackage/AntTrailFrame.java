package hr.fer.zemris.optjava.dz10.antpackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AntTrailFrame extends JFrame{

	private int[][] world;
	private JLabel[][] colors;
	private Ant ant;
	
	public AntTrailFrame(int[][] world, Ant ant) {
		super();
		this.world = world;
		this.ant = ant;
		colors = new JLabel[world.length][world[0].length];
		for(int i = 0; i < world.length; i++) {
			for(int j = 0; j < world[0].length; j++) {
				colors[i][j] = new JLabel();
				colors[i][j].setOpaque(true);
				if(world[i][j] == 0 || world[i][j] == 2) {
					colors[i][j].setBackground(Color.orange);
				} else if(world[i][j] == 1) {
					colors[i][j].setBackground(Color.blue);
				}
			}
		}
		colors[ant.getCurrentRow()][ant.getCurrentColumn()].setBackground(Color.black);
		update();
		initGUI();
	}
	
	private void update() {
		for(int i = 0; i < world.length; i++) {
			for(int j = 0; j < world[0].length; j++) {
				colors[i][j].setOpaque(true);
				if(world[i][j] == 0 || world[i][j] == 2) {
					colors[i][j].setBackground(Color.orange);
				} else if(world[i][j] == 1) {
					colors[i][j].setBackground(Color.blue);
				}
			}
		}
		colors[ant.getCurrentRow()][ant.getCurrentColumn()].setBackground(Color.black);
	}
	
	private void initGUI(){
		this.setLayout(new BorderLayout());
		Container pane = this.getContentPane();
		
		JPanel matrix = new JPanel();
		matrix.setLayout(new GridLayout(world.length, world[0].length, 3, 3));
		for(int i = 0; i < colors.length; i++) {
			for(int j = 0; j < colors[0].length; j++) {
				matrix.add(colors[i][j]);
			}
		}
		World world = new World(this.world);
		JButton btnNext = new JButton("Next");
		btnNext.addActionListener(e -> {
			ant.getBrain().doStuff(ant, world);
			update();
			if(ant.getActionsLeft() <= 0) {
				btnNext.setEnabled(false);
			}
		});
		JButton btnIter = new JButton("Run, Anthony, run!");
		btnIter.addActionListener(e -> {
			btnIter.setEnabled(false);
			btnNext.setEnabled(false);
			while(ant.getActionsLeft() > 0) {
				ant.getBrain().doStuff(ant, world);
				update();
			}
		});
		JPanel down = new JPanel(new GridLayout(1, 2));
		down.add(btnIter);
		down.add(btnNext);
		pane.add(matrix, BorderLayout.CENTER);
		pane.add(down, BorderLayout.SOUTH);
		
	}
}
