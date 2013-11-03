/* RunLengthEncoding.java */

/**
 *  The RunLengthEncoding class defines an object that run-length encodes an
 *  Ocean object.  Descriptions of the methods you must implement appear below.
 *  They include constructors of the form
 *
 *      public RunLengthEncoding(int i, int j, int starveTime);
 *      public RunLengthEncoding(int i, int j, int starveTime,
 *                               int[] runTypes, int[] runLengths) {
 *      public RunLengthEncoding(Ocean ocean) {
 *
 *  that create a run-length encoding of an Ocean having width i and height j,
 *  in which sharks starve after starveTime timesteps.
 *
 *  The first constructor creates a run-length encoding of an Ocean in which
 *  every cell is empty.  The second constructor creates a run-length encoding
 *  for which the runs are provided as parameters.  The third constructor
 *  converts an Ocean object into a run-length encoding of that object.
 *
 *  See the README file accompanying this project for additional details.
 */

public class RunLengthEncoding extends DList {

  /**
   *  Define any variables associated with a RunLengthEncoding object here.
   *  These variables MUST be private.
   */
	private DListNode runPointer; // Used for the nextRun() and resetRuns() methods
	private int starveVal; // the starveTime for the Ocean object
	private int width; // width of the Ocean object
	private int height; // height of the Ocean object



  /**
   *  The following methods are required for Part II.
   */

  /**
   *  RunLengthEncoding() (with three parameters) is a constructor that creates
   *  a run-length encoding of an empty ocean having width i and height j,
   *  in which sharks starve after starveTime timesteps.
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   */

  public RunLengthEncoding(int i, int j, int starveTime) {
	  starveVal = starveTime;
	  width = i;
	  height = j;
	  Run emptyOcean = new Run(i * j, Ocean.EMPTY, starveVal);
	  insertEnd(emptyOcean);
	  
	  //make sure to initialize runPointer AFTER initializing the first node.
	  //and after any future calls to insertFront() although I don't imagine
	  //I will need insertFront in this class 
	  runPointer = head;
  }

  /**
   *  RunLengthEncoding() (with five parameters) is a constructor that creates
   *  a run-length encoding of an ocean having width i and height j, in which
   *  sharks starve after starveTime timesteps.  The runs of the run-length
   *  encoding are taken from two input arrays.  Run i has length runLengths[i]
   *  and species runTypes[i].
   *  @param i is the width of the ocean.
   *  @param j is the height of the ocean.
   *  @param starveTime is the number of timesteps sharks survive without food.
   *  @param runTypes is an array that represents the species represented by
   *         each run.  Each element of runTypes is Ocean.EMPTY, Ocean.FISH,
   *         or Ocean.SHARK.  Any run of sharks is treated as a run of newborn
   *         sharks (which are equivalent to sharks that have just eaten).
   *  @param runLengths is an array that represents the length of each run.
   *         The sum of all elements of the runLengths array should be i * j.
   */

  public RunLengthEncoding(int i, int j, int starveTime, int[] runTypes, int[] runLengths) {
	  starveVal = starveTime;
	  width = i;
	  height = j;
	  Run newRun;
	  for(int x = 0; x < runLengths.length; x++) {
		  newRun = new Run(runLengths[x], runTypes[x], ((runTypes[x] == Ocean.SHARK)? starveVal: -1));
		  insertEnd(newRun);
	  }
	  // AFTER the list is initialized
	  runPointer = head;
  }

  /**
   *  restartRuns() and nextRun() are two methods that work together to return
   *  all the runs in the run-length encoding, one by one.  Each time
   *  nextRun() is invoked, it returns a different run (represented as a
   *  TypeAndSize object), until every run has been returned.  The first time
   *  nextRun() is invoked, it returns the first run in the encoding, which
   *  contains cell (0, 0).  After every run has been returned, nextRun()
   *  returns null, which lets the calling program know that there are no more
   *  runs in the encoding.
   *
   *  The restartRuns() method resets the enumeration, so that nextRun() will
   *  once again enumerate all the runs as if nextRun() were being invoked for
   *  the first time.
   *
   *  (Note:  Don't worry about what might happen if nextRun() is interleaved
   *  with addFish() or addShark(); it won't happen.)
   */

  /**
   *  restartRuns() resets the enumeration as described above, so that
   *  nextRun() will enumerate all the runs from the beginning.
   */

  public void restartRuns() {runPointer = head;}

  /**
   *  nextRun() returns the next run in the enumeration, as described above.
   *  If the runs have been exhausted, it returns null.  The return value is
   *  a TypeAndSize object, which is nothing more than a way to return two
   *  integers at once.
   *  @return the next run in the enumeration, represented by a TypeAndSize
   *          object.
   */

  public TypeAndSize nextRun() {
	  if(runPointer == null)
		  return null;
	  TypeAndSize ts = new TypeAndSize(((Run)runPointer.item).getRunType(), ((Run)runPointer.item).getRunLength());
	  runPointer = runPointer.next;
	  
	  return ts;
  }

  /**
   *  toOcean() converts a run-length encoding of an ocean into an Ocean
   *  object.  You will need to implement the three-parameter addShark method
   *  in the Ocean class for this method's use.
   *  @return the Ocean represented by a run-length encoding.
   */

  public Ocean toOcean() {
	  // Creates an Ocean object with width, height, and starveTime equal this RunLengthEncoding
	  // object's respective values. The method then iterates through each node of the list in
	  // order to set the Oceans tiles to the appropriate values according to each run.
	  
	  Ocean returnOcean = new Ocean(width, height, starveVal);
	  DListNode currentNode = head;
	  Run currentRun;
	  int i = 0;
	  
	  while(currentNode != null) {
		  currentRun = (Run)currentNode.item;
		  
		  for(int x = 0; x < currentRun.getRunLength(); x++) {
			  if(currentRun.getRunType() == Ocean.SHARK)
				  returnOcean.addShark(i % width, i / width, currentRun.getHungerVal());
			  else if(currentRun.getRunType() == Ocean.FISH)
				  returnOcean.addFish(i % width, i / width);
			  
			  i++;
		  }
		  currentNode = currentNode.next;
	  }
	  
    return returnOcean;
  }

  /**
   *  The following method is required for Part III.
   */

  /**
   *  RunLengthEncoding() (with one parameter) is a constructor that creates
   *  a run-length encoding of an input Ocean.  You will need to implement
   *  the sharkFeeding method in the Ocean class for this constructor's use.
   *  @param sea is the ocean to encode.
   */

  public RunLengthEncoding(Ocean sea) {
	  int current = 0;
	  int totalLength = sea.height() * sea.width();
	  int runLength;
	  int hunger;
	  int type;
	  starveVal = sea.starveTime();
	  width = sea.width();
	  height = sea.height();
	  
	  while(current < totalLength) {
		  type = sea.cellContents(current);
		  hunger = sea.sharkFeeding(current);
		  current++;
		  runLength = 1;
		  
		  while((current < totalLength) && (type == sea.cellContents(current)) && (hunger == sea.sharkFeeding(current))) {
			  current++;
			  runLength++;
		  }
		  
		  insertEnd(new Run(runLength, type, hunger));
	  }
	  
	  // No really make sure you initialize this AFTER you initialize the list...
	  // Seriously...
	  runPointer = head;
    //check();
  }

  /**
   *  The following methods are required for Part IV.
   */

  /**
   *  addFish() places a fish in cell (x, y) if the cell is empty.  If the
   *  cell is already occupied, leave the cell as it is.  The final run-length
   *  encoding should be compressed as much as possible; there should not be
   *  two consecutive runs of sharks with the same degree of hunger.
   *  @param x is the x-coordinate of the cell to place a fish in.
   *  @param y is the y-coordinate of the cell to place a fish in.
   */

  public void addFish(int x, int y) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
    check();
  }

  /**
   *  addShark() (with two parameters) places a newborn shark in cell (x, y) if
   *  the cell is empty.  A "newborn" shark is equivalent to a shark that has
   *  just eaten.  If the cell is already occupied, leave the cell as it is.
   *  The final run-length encoding should be compressed as much as possible;
   *  there should not be two consecutive runs of sharks with the same degree
   *  of hunger.
   *  @param x is the x-coordinate of the cell to place a shark in.
   *  @param y is the y-coordinate of the cell to place a shark in.
   */

  public void addShark(int x, int y) {
    // Your solution here, but you should probably leave the following line
    //   at the end.
    check();
  }

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same contents, or if the sum of all run
   *  lengths does not equal the number of cells in the ocean.
   */

  public void check() {
  }

  class Run {
	  private int runLength;
	  private int runType;
	  private int hungerVal;
	  
	  Run(int length, int type, int hunger) {
		  runLength = length;
		  runType = type;
		  hungerVal = hunger;
	  }
	  
	  Run(int length, int type) {this(length, type, -1);}
	  
	  Run(int length) {this(length, Ocean.EMPTY);}
	  
	  int getRunLength(){return runLength;}
	  
	  int getRunType(){return runType;}
	  
	  int getHungerVal(){return hungerVal;}
  }
  
public static void main(String[] args) {
	  
	  Ocean o = new Ocean(20, 20, 3);

	  o.addShark(2, 1);
	  o.addShark(1, 2);
	  o.addShark(2, 2);
	  o.addShark(3, 2);
	  o.addShark(2, 3);
	  o.addFish(1, 1);
	  o.addFish(3, 1);
	  o.addFish(4, 1);
	  o.addFish(0, 3);
	  o.addFish(1, 3);
	  
	  o.printGrid();
	  
	  RunLengthEncoding rlc = new RunLengthEncoding(o);

	  System.out.println();
	  System.out.println(rlc.nextRun());
	  System.out.println();
	  System.out.println(rlc.nextRun());
	  System.out.println();
	  
	  Ocean sea = rlc.toOcean();
	  
	  sea.printGrid();
	  sea = sea.timeStep();
	  System.out.println();
	  sea.printGrid();
	  sea = sea.timeStep();
	  System.out.println();
	  sea.printGrid();
	  sea = sea.timeStep();
	  System.out.println();
	  sea.printGrid();
	  sea = sea.timeStep();
	  System.out.println();
  }
}