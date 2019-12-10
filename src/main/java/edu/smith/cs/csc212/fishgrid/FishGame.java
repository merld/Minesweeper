package edu.smith.cs.csc212.fishgrid;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import me.jjfoley.gfx.TextBox;

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
	 * 2D array of squares
	 */
	Square[][] squares;
	/**
	 * Create a FishGame of a particular size.
	 * @param w how wide is the grid?
	 * @param h how tall is the grid?
	 */
	
	int flagsLeft;
	int correctFlags;

	public FishGame(int w, int h) {
		world = new World(w, h);
		squares=new Square[14][14];
		flagsLeft=40;
		correctFlags=0;
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
		if(squares[player.getX()][player.getY()].isMine) {
			squares[player.getX()][player.getY()].isVisible=true;
		}
		else if(squares[player.getX()][player.getY()].mineNum==0) {
			clearSquares(player.getX(),player.getY());
		}
		else {
			squares[player.getX()][player.getY()].isVisible=true;
		}
	}
	
	public void clickF(Graphics2D g) {
		Flag flag = new Flag(world);
		flag.setPosition(player.getX(), player.getY());
		world.register(flag);
		flagsLeft--;
		if(squares[player.getX()][player.getY()].isMine)
			correctFlags++;
	}
	
	public boolean isValidIndex(int x, int y) {
		if(x<0||x>13||y<0||y>13)
			return false;
		return true;
	}
	
	
	/**
	 * This method is how the Main app tells whether we're done.
	 * @return true if the player has won (or maybe lost?).
	 */
	public int gameOver() {
		if(squares[player.getX()][player.getY()].isMine==true&&squares[player.getX()][player.getY()].isVisible==true)
			return 1;
		else if(flagsLeft==0&&correctFlags==40)
			return 0;
		return -1;
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
			if(x>5&&x<9&&y>5&&y<9)
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
	
	public void clearSquares(int x, int y) {
		if(squares[x][y].mineNum>0) {
			return;
		}
		else {
			if(isValidIndex(x,y-1)&&squares[x][y-1].isVisible==false){
				squares[x][y-1].isVisible=true;
				clearSquares(x,y-1);
			}	
			if(isValidIndex(x-1,y-1)&&squares[x-1][y-1].isVisible==false){
				squares[x-1][y-1].isVisible=true;
				clearSquares(x-1,y-1);
			}
			if(isValidIndex(x+1,y-1)&&squares[x+1][y-1].isVisible==false){
				squares[x+1][y-1].isVisible=true;
				clearSquares(x+1,y-1);
			}
			if(isValidIndex(x-1,y)&&squares[x-1][y].isVisible==false){
				squares[x-1][y].isVisible=true;
				clearSquares(x-1,y);
			}	
			if(isValidIndex(x+1,y)&&squares[x+1][y].isVisible==false){
				squares[x+1][y].isVisible=true;
				clearSquares(x+1,y);
			}	
			if(isValidIndex(x-1,y+1)&&squares[x-1][y+1].isVisible==false){
				squares[x-1][y+1].isVisible=true;
				clearSquares(x-1,y+1);
			}
			if(isValidIndex(x+1,y+1)&&squares[x+1][y+1].isVisible==false){
				squares[x+1][y+1].isVisible=true;
				clearSquares(x+1,y+1);
			}
			if(isValidIndex(x,y+1)&&squares[x][y+1].isVisible==false){
				squares[x][y+1].isVisible=true;
				clearSquares(x,y+1);
			}
		}
			
	}
	
	public Square[] checkSurrounding(int x, int y) {
		Square[] ret = new Square[4];
		int count=0;
		if(isValidIndex(x,y-1)&&squares[x][y-1].mineNum==0){
			ret[count]=squares[x][y-1];
			count++;
		}	
		if(isValidIndex(x-1,y)&&squares[x-1][y].mineNum==0){
			ret[count]=squares[x-1][y];
			count++;
		}	
		if(isValidIndex(x+1,y)&&squares[x+1][y].mineNum==0){
			ret[count]=squares[x+1][y];
			count++;
		}	

		if(isValidIndex(x,y+1)&&squares[x][y+1].mineNum==0){
			ret[count]=squares[x][y+1];
			count++;
		}
		return ret;
	}
	
	
	
	
	
}
