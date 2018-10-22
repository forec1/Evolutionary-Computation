package hr.fer.zemris.optjava.dz2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class FunctionDraw extends JComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int[] pointsX;
	private int[] pointsY;
	
	public FunctionDraw(int[] X, int[] Y)  {
		this.pointsX = X;
		this.pointsY = Y;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g1 = (Graphics2D) g;
		g1.setColor(Color.RED);
		g1.drawString("FILIP", 20, 20);
		
		for(int i = 0; i < pointsX.length-1; i++) {
			g1.drawLine(pointsX[i], pointsY[i], pointsX[i+1], pointsY[i+1]);
		}
	}
}
