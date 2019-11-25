package edu.smith.cs.csc212.fishgrid;

import java.util.ArrayList;
import java.util.List;
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
	Fish player;
	/**
	 * Score!
	 */
	int score;
	/**
	 * 2D Array of booleans to hold mines
	 */
	boolean[][] mines;
	/**
	 * 2D Array of ints to determine how many mines coordinate touches
	 */
	int[][]mineNum;
	/**
	 * Create a FishGame of a particular size.
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */
	public FishGame(int w, int h) {
		world = new World(w, h);
		mines = new boolean[14][14];
		mineNum = new int[14][14];
		initializeMines();
		// Make the player out of the 0th fish color.
		player = new Fish(0, world);
		// Start the player at "home".
		player.setPosition(7, 7);
		player.markAsPlayer();
		world.register(player);
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
	public void click(int x, int y) {
		System.out.println("Clicked on: "+x+","+y+ " world.canSwim(player,...)="+world.canSwim(player, x, y));
		List<WorldObject> atPoint = world.find(x, y);
		
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
			if(mines[x][y]==false) {
				mines[x][y]=true;
				mineCount++;
			}
		}
		//Fills mineNum
		for(int x=0; x<14;x++) {
			for(int y=0; y<14;y++) {
				int count=0;
				if(isValidIndex(x-1,y-1)&&mines[x-1][y-1]==true)
					count++;
				if(isValidIndex(x,y-1)&&mines[x][y-1]==true)
					count++;
				if(isValidIndex(x+1,y-1)&&mines[x+1][y-1]==true)
					count++;
				if(isValidIndex(x-1,y)&&mines[x-1][y]==true)
					count++;
				if(isValidIndex(x+1,y)&&mines[x+1][y]==true)
					count++;
				if(isValidIndex(x-1,y+1)&&mines[x-1][y+1]==true)
					count++;
				if(isValidIndex(x,y+1)&&mines[x][y+1]==true)
					count++;
				if(isValidIndex(x+1,y+1)&&mines[x+1][y+1]==true)
					count++;
				mineNum[x][y]=count;
			}
		}
		for(int x=0; x<14;x++) {
			for(int y=0; y<14;y++) {
				if(mines[x][y]==true)
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
				System.out.print(mineNum[x][y]+" ");
			}
			System.out.println();
		}
	}
}
