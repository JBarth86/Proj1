/* Ocean.java */

/**
 *  The Ocean class defines an object that models an ocean full of sharks and
 *  fish.  Descriptions of the methods you must implement appear below.  They
 *  include a constructor of the form
 *
 *      public Ocean(int i, int j, int starveTime);
 *
 *  that creates an empty ocean having width i and height j, in which sharks
 *  starve after starveTime timesteps.
 *
 *  See the README file accompanying this project for additional details.
 */

public class Ocean {

  /**
   *  Do not rename these constants.  WARNING:  if you change the numbers, you
   *  will need to recompile Test4.java.  Failure to do so will give you a very
   *  hard-to-find bug.
   */

  public final static int EMPTY = 0;
  public final static int SHARK = 1;
  public final static int FISH = 2;

  /**
   *  Define any variables associated with an Ocean object here.  These
   *  variables MUST be private.
   */
  private int width;
  private int height;
  private int starveTime;
  private OceanTile[][] grid;



  /**
   *  The following methods are required for Part I.
   */

  /**
   *  Ocean() is a constructor that creates an empty ocean having width i and
   *  height j, in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public Ocean(int i, int j, int starveTime) {
	  width = i;
	  height = j;
	  this.starveTime = starveTime;
	  grid = new OceanTile[width][height];
	  
	  for(int x = 0; x < width; x++) {
		  for(int y = 0; y < height; y++) {
			  grid[x][y] = new EmptyTile(x, y);
		  }
	  }
  }

  /**
   *  width() returns the width of an Ocean object.
   *  @return the width of the ocean.
   */

  public int width() {return width;}

  /**
   *  height() returns the height of an Ocean object.
   *  @return the height of the ocean.
   */

  public int height() {return height;}

  /**
   *  starveTime() returns the number of timesteps sharks survive without food.
   *  @return the number of timesteps sharks survive without food.
   */

  public int starveTime() {return starveTime;}

  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
	  if(grid[x][y].getContents() == EMPTY)
		  grid[x][y] = new FishTile(x, y);
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
	  if(grid[x][y].getContents() == EMPTY)
		  grid[x][y] = new SharkTile(x, y, starveTime);
  }

  /**
   *  cellContents() returns EMPTY if cell (x, y) is empty, FISH if it contains
   *  a fish, and SHARK if it contains a shark.
   *  @param x is the x-coordinate of the cell whose contents are queried.
   *  @param y is the y-coordinate of the cell whose contents are queried.
   */

  public int cellContents(int x, int y) {return grid[x][y].getContents();}

  public int cellContents(int i) {return cellContents(i % width, i / width);}
  
  /**
   *  timeStep() performs a simulation timestep as described in README.
   *  @return an ocean representing the elapse of one timestep.
   */

  public Ocean timeStep() {
	  Ocean futureOcean = new Ocean(width, height, starveTime);
	  int[] surroundings;
	  for(int y = 0; y < height; y++) {
		  for(int x = 0; x < width; x++) {
			  surroundings = getSurroundings(validateX(x), validateY(y));
		
			  futureOcean.grid[x][y] = grid[x][y].step(surroundings[FISH], surroundings[SHARK]);
		  }
	  }
	  
	  return futureOcean;
  }  
  
  private int[] getSurroundings(int xCoord, int yCoord) {
	  int[] surroundings = new int[3];
	  
	  //System.out.println("origin = " + xCoord + ", " + yCoord);
	  
	  for(int y = yCoord - 1; y <= yCoord + 1; y++) {
		  for(int x = xCoord - 1; x <= xCoord + 1; x++) {
			  
			  if(!((validateX(x) == xCoord) && (validateY(y) == yCoord))){
				  //System.out.println("\tchecking: " + validateX(x) + ", " + validateY(y));
				  if(cellContents(validateX(x), validateY(y)) == FISH) {
					 // System.out.println("\t\tfish found");
					  
					  surroundings[FISH]++;
				  }
				  else if(cellContents(validateX(x), validateY(y)) == SHARK) {
					 // System.out.println("\t\tshark found");
					  surroundings[SHARK]++;
				  }
			  }
		  }
	  }
	  
	  return surroundings;
  }
  
  private int validateX(int x) {
	  if(x < 0)
		  return width - 1;
	  if(x >= width)
		  return 0;
	  return x;
  }
  
  private int validateY(int y) {
	  if(y < 0)
		  return height - 1;
	  if(y >= height)
		  return 0;
	  return y;
  }
  
  /**
   *  The following method is required for Part II.
   */

  /**
   *  addShark() (with three parameters) places a shark in cell (x, y) if the
   *  cell is empty.  The shark's hunger is represented by the third parameter.
   *  If the cell is already occupied, leave the cell as it is.  You will need
   *  this method to help convert run-length encodings to Oceans.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   *  @param feeding is an integer that indicates the shark's hunger.  You may
   *         encode it any way you want; for instance, "feeding" may be the
   *         last timestep the shark was fed, or the amount of time that has
   *         passed since the shark was last fed, or the amount of time left
   *         before the shark will starve.  It's up to you, but be consistent.
   */

  public void addShark(int x, int y, int feeding) {	  
	  if(grid[x][y].getContents() == EMPTY)
		  grid[x][y] = new SharkTile(x, y, feeding);
  }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  sharkFeeding() returns an integer that indicates the hunger of the shark
   *  in cell (x, y), using the same "feeding" representation as the parameter
   *  to addShark() described above.  If cell (x, y) does not contain a shark,
   *  then its return value is undefined--that is, anything you want.
   *  Normally, this method should not be called if cell (x, y) does not
   *  contain a shark.  You will need this method to help convert Oceans to
   *  run-length encodings.
   *  @param x is the x-coordinate of the cell whose contents are queried.
   *  @param y is the y-coordinate of the cell whose contents are queried.
   */

  public int sharkFeeding(int x, int y) {
	  if(grid[x][y] instanceof SharkTile)
		  return ((SharkTile)grid[x][y]).getHunger();
	  
	  return -1;
  }

  public int sharkFeeding(int i) {return sharkFeeding(i % width, i / width);}
  
  abstract class OceanTile {
	  int xCoord;
	  int yCoord;
	  int contains;
	  
	  public int getContents() {return contains;}
	  
	  
	  
	  public abstract OceanTile step(int fish, int sharks);
  }
  
  final class SharkTile extends OceanTile {
	  int hunger;
	  
	  
	  public SharkTile(int x, int y, int hunger) {
		  xCoord = x;
		  yCoord = y;
		  contains = Ocean.SHARK;
		  this.hunger = hunger;
	  }
	  
	  public OceanTile step(int fish, int sharks) {
		  if(fish > 0)
			  hunger = starveTime() + 1;
		  if(hunger - 1 < 0)
			  return new EmptyTile(xCoord, yCoord);
		  return new SharkTile(xCoord, yCoord, hunger - 1);
	  }
	  
	  public int getHunger() {return hunger;}
  }
  
  final class EmptyTile extends OceanTile {
	  public EmptyTile(int x, int y) {
		  xCoord = x;
		  yCoord = y;
		  contains = Ocean.EMPTY;
	  }

	  public OceanTile step(int fish, int sharks) {
		  if(fish < 2)
			  return new EmptyTile(xCoord, yCoord);
		  else if(sharks < 2)
			  return new FishTile(xCoord, yCoord);
		  return new SharkTile(xCoord, yCoord, starveTime());
	  }
  }
  
  final class FishTile extends OceanTile {
	  public FishTile(int x, int y) {
		  xCoord = x;
		  yCoord = y;
		  contains = Ocean.FISH;
	  }
	  
	  public OceanTile step(int fish, int sharks) {
		  if(sharks == 1)
			  return new EmptyTile(xCoord, yCoord);
		  else if(sharks > 1)
			  return new SharkTile(xCoord, yCoord, starveTime());
		  return new FishTile(xCoord, yCoord);
	  }
  }
  
  public void printGrid() {
	  for(int i = 0; i < width + 2; i++)
		  System.out.print("_");
	  System.out.println();
	  for(int y = 0; y < height; y++) {
		  System.out.print("|");
		  for(int x = 0; x < width; x++) {
			  if(grid[x][y] instanceof EmptyTile) 
				  System.out.print(" ");
			  else if(grid[x][y] instanceof FishTile)
				  System.out.print("~");
			  else
				  System.out.print("S"); //System.out.print(sharkFeeding(x, y));
		  }
		  System.out.println("|");
	  }
	  for(int i = 0; i < width + 2; i++)
		  System.out.print("_");
  }
  
  
  public static void main(String[] args) {
	  Ocean o = new Ocean(50, 37, 3);
	  
	  o.printGrid();
	  o.addFish(1, 1);
	  System.out.println();
	  o.printGrid();
  }

}