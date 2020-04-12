package edu.smith.cs.csc212.minesweeper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

public class Flag extends WorldObject{

	Polygon triangle;
	
	public Flag(World world) {
		super(world);
		triangle = new Polygon();
		triangle.addPoint(1, 9);
		triangle.addPoint(1, 1);
		triangle.addPoint(9, 5);
	}
	
	public int getColorIndex() {
		return -1;
	}
	
	public void step() {}
	
	public void draw(Graphics2D g) {
		
		Graphics2D scale = (Graphics2D) g.create();
		scale.scale(1.0/10.0, 1.0/10.0);
		scale.translate(-5, -5);
		
		scale.setColor(Color.RED);
		
		scale.drawPolygon(triangle);
		scale.fillPolygon(triangle);

	}
	
	
}
