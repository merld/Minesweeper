package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;

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
		scale.scale(1.0/15.0, 1.0/15.0);
		scale.translate(-4, 6);
		
		scale.setColor(Color.black);
		if(isVisible)
			scale.drawString(""+mineNum, -1, -1);
	}
}
