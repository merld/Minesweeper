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
	//starts at 40 and keeps track of flags placed
	int flagsLeft;
	//keeps track of number of flags placed on mines
	int correctFlags;


	public FishGame(int w, int h) {
		world = new World(w, h);
		//2D array matches size of playing grid
		squares=new Square[14][14];
		flagsLeft=40;
		correctFlags=0;
		// Make the player out of the 0th fish color.
		player = new Fish(0, world);
		// Start the player at the middle.
		player.setPosition(7, 7);
		player.markAsPlayer();

		world.register(player);
		
		//register all of the Squares in squares
		for(int x=0; x<14;x++) {
			for(int y=0; y<14; y++) {
				squares[x][y]=new Square(world);
				squares[x][y].setPosition(x, y);
				world.register(squares[x][y]);
			}
		}
	}
	
	//Makes player move
	public void step() {
		world.stepAll();
	}
	

	/**
	 * This gets a click on the grid. We want it to destroy rocks that ruin the game.
	 * @param x - the x-tile.
	 * @param y - the y-tile.
	 */
	public void click(Graphics2D g) {
		//is there a flag in the clicked space?
		boolean foo = false;
		
		//Searches for flag in player's cell and updates if necessary
		for(WorldObject wo: player.findSameCell()) {
			if(wo instanceof Flag)
				foo=true;
		}
		//Shows mine if isMine and not flagged
		if(squares[player.getX()][player.getY()].isMine&&!foo) {
			squares[player.getX()][player.getY()].isVisible=true;
		}
		//Causes the flood method if mineNum is 0 and is not flagged
		else if(squares[player.getX()][player.getY()].mineNum==0&&!foo) {
			clearSquares(player.getX(),player.getY());
		}
		//Shows the minNum if not flagged
		else if(!foo) {
			squares[player.getX()][player.getY()].isVisible=true;
		}
	}
	
	public void clickF(Graphics2D g) {
		//Shows if there is already a flag there
		boolean remove = false;
		//Checks if there is a flag in the same cell, removes it if there is
		for(WorldObject w: this.player.findSameCell()) {
			if(w instanceof Flag) {
				world.remove(w);
				remove=true;
				flagsLeft++;
				if(squares[player.getX()][player.getY()].isMine)
					correctFlags--;
				break;
			}
		}
		//Creates a flag where there wasn't one
		if(!remove) {
			Flag flag = new Flag(world);
			flag.setPosition(player.getX(), player.getY());
			world.register(flag);
			flagsLeft--;
			if(squares[player.getX()][player.getY()].isMine)
				correctFlags++;
		}
	}
	
	//Checks to make sure index is in bounds
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
			if(x>player.getX()-2&&x<player.getX()+2&&y>player.getY()-2&&y<player.getY()+2)
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
		/* Print statements to debug. Prints where mines are and mineNums
		 * 
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
		}*/
	}
	
	//FLood method. Checks every adjacent square's mineNum and recurses through them
	public void clearSquares(int x, int y) {
		squares[x][y].isVisible=true;
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
}
