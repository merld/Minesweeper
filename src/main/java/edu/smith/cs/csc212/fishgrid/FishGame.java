package edu.smith.cs.csc212.fishgrid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import me.jjfoley.gfx.GFX;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class manages our model of gameplay: missing and found fish, etc.
 * @author jfoley
 *
 */
public class FishGame {
	/**
	 * This is the world in which the fish are missing. (It's mostly a List!).
	 */
	World world;
	/**
	 * The player (a Fish.COLORS[0]-colored fish) goes seeking their friends.
	 */
	public Fish player;
	/**
	 * Score!
	 */
	int score;
	
	/**
	 * 2D array of squares
	 */
	Square[][] squares;
	/**
	 * Create a FishGame of a particular size.
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */

	public FishGame(int w, int h) {
		world = new World(w, h);
		squares=new Square[14][14];
		
		// Make the player out of the 0th fish color.
		player = new Fish(0, world);
		// Start the player at "home".
		player.setPosition(7, 7);
		player.markAsPlayer();

		world.register(player);
		for(int x=0; x<14;x++) {
			for(int y=0; y<14; y++) {
				squares[x][y]=new Square(world);
				squares[x][y].setPosition(x, y);
				world.register(squares[x][y]);
			}
		}
		initializeMines();
	}
	
	public void step() {
		// These are all the objects in the world in the same cell as the player.
		List<WorldObject> overlap = this.player.findSameCell();
		// The player is there, too, let's skip them.
		overlap.remove(this.player);
		
		
		// Step any world-objects that run themselves.
		world.stepAll();
	}
	

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the game.
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(Graphics2D g) {

		if(squares[player.getX()][player.getY()].isMine==true) {
			//end game
		}
		else {
			squares[player.getX()][player.getY()].isVisible=true;
			//g.drawString(""+(mineNum[player.getX()][player.getY()]),player.getX(),player.getY());
		}
	}
	
	public void clickF(Graphics2D g) {
		Flag flag = new Flag(world);
		flag.setPosition(player.getX(), player.getY());
		world.register(flag);
	}
	
	public boolean isValidIndex(int x, int y) {
		if(x<0||x>13||y<0||y>13)
			return false;
		return true;
	}
	
	/**
	 * Fills 2D arrays with random mines and the number of mines surrounding
	 */
	public void initializeMines() {
		//Fills mines
		int mineCount=0;
		while(mineCount<40) {
			int x = (int) (Math.random()*14);
			int y = (int)(Math.random()*14);
			if(x==7&&y==7)
				continue;
			if(squares[x][y].isMine==false) {
				squares[x][y].isMine=true;
				mineCount++;
			}
		}
		//Fills mineNum
		for(int x=0; x<14;x++) {
			for(int y=0; y<14;y++) {
				int count=0;
				if(isValidIndex(x-1,y-1)&&squares[x-1][y-1].isMine==true)
					count++;
				if(isValidIndex(x,y-1)&&squares[x][y-1].isMine==true)
					count++;
				if(isValidIndex(x+1,y-1)&&squares[x+1][y-1].isMine==true)
					count++;
				if(isValidIndex(x-1,y)&&squares[x-1][y].isMine==true)
					count++;
				if(isValidIndex(x+1,y)&&squares[x+1][y].isMine==true)
					count++;
				if(isValidIndex(x-1,y+1)&&squares[x-1][y+1].isMine==true)
					count++;
				if(isValidIndex(x,y+1)&&squares[x][y+1].isMine==true)
					count++;
				if(isValidIndex(x+1,y+1)&&squares[x+1][y+1].isMine==true)
					count++;
				squares[x][y].mineNum=count;
			}
		}
		for(int x=0; x<14;x++) {
			for(int y=0; y<14;y++) {
				if(squares[y][x].isMine==true)
					System.out.print("1");
				else
					System.out.print("0");
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
		for(int x=0; x<14;x++) {
			for(int y=0; y<14;y++) {
				System.out.print(squares[y][x].mineNum+" ");
			}
			System.out.println();
		}
	}
	
	
	
	
	
}
