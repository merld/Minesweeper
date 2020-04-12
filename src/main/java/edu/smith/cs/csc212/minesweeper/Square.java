package edu.smith.cs.csc212.minesweeper;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Square extends WorldObject{
	
	public boolean isMine;
	public boolean isVisible;
	public int mineNum;
	
	public Square(World world) {
		super(world);
		isMine=false;
		isVisible=false;
		mineNum=0;
	}
	
	public int getColorIndex() {return -1;}
	public void step() {}
	
	
	
	public void draw(Graphics2D g) {
		Graphics2D scale = (Graphics2D) g.create();
		
		if(isMine==false) {
			scale.scale(1.0/15.0, 1.0/15.0);
			scale.translate(-4, 6);
			scale.setColor(Color.black);
			if(isVisible)
				scale.drawString(""+mineNum, -1, -1);
		}
		else {
			scale.scale(1.0/2.0,1.0/2.0);
			scale.translate(0, 0);
			Shape circle = new Ellipse2D.Double(-0.6, -0.6, 1.2, 1.2);
			scale.setColor(Color.red);
			if(isVisible)
				scale.draw(circle);
		}
	}
}
