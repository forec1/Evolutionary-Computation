package hr.fer.zemris.optjava.dz2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Jama.Matrix;

public class ImageTrajectory {
	
	private BufferedImage bi;
	private Matrix x;
	
	public ImageTrajectory(Matrix x) {
		bi = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		this.x = x;
		init();
	}
	
	private void init() {
		Graphics2D ig2 = bi.createGraphics();
		ig2.setPaint(Color.WHITE);
		ig2.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		int x1 = 0;
		int y1 = bi.getHeight() / 2;
		int x2 = bi.getWidth();
		int y2 = bi.getHeight() / 2;
		// x os
		ig2.setPaint(Color.BLACK);
		ig2.drawLine(x1, y1, x2, y2);
		
		x1 = bi.getWidth() / 2;
		y1 = 0;
		x2 = bi.getWidth() / 2;
		y2 = bi.getHeight();
		
		//y os
		ig2.drawLine(x1, y1, x2, y2);
		
	}
	
	public void put(Matrix newX) {
		Graphics2D ig2 = bi.createGraphics();
		//ig2.setBackground(Color.WHITE);
		ig2.setPaint(Color.RED);
		int x1 = (int) (x.get(0, 0) * 100 + bi.getWidth() / 2);
		int y1 = (int) ((x.get(1, 0) * 100) * (-1) + bi.getHeight() / 2);
		int x2 = (int) (newX.get(0, 0) * 100 + bi.getWidth() / 2);
		int y2 = (int) ((newX.get(1, 0) * 100) * (-1) + bi.getHeight() / 2);
		ig2.drawLine(x1, y1, x2, y2);
		x = newX;
	}
	
	public void save(String path) {
		try {
			ImageIO.write(bi, "PNG", new File(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}