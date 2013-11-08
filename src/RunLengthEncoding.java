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

public class RunLengthEncoding {

  /**
   *  I've implemented this class to manage a doubly linked list of Run objects
   *  implemented as nodes. The coding seemed much cleaner this way than accessing
   *  the runs through a linked list of nodes pointing to run objects.
   */
	private Run runPointer; // Used for the nextRun() and resetRuns() methods
	private int starveVal; // the starveTime for the Ocean object
	private int width; // width of the Ocean object
	private int height; // height of the Ocean object

	private Run header; // header and trailer are dummy Run nodes with their data members set to -1
	private Run trailer; // RunLengthEncoding constructors should initialize header and trailer with the two argument Run constructor.

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
	  header = new Run(null, trailer);
	  trailer = new Run(header, null);
	  Run emptyOcean = new Run(i * j, Ocean.EMPTY, starveVal);
	  insertLast(emptyOcean);
	  runPointer = getFirst();
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
	  header = new Run(null, trailer);
	  trailer = new Run(header, null);
	  Run newRun;
	  
	  for(int x = 0; x < runLengths.length; x++) {
		  newRun = new Run(runLengths[x], runTypes[x], ((runTypes[x] == Ocean.SHARK)? starveVal: -1));
		  insertLast(newRun);
	  }
	  
	  runPointer = getFirst();
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

  public void restartRuns() {runPointer = getFirst();}

  /**
   *  nextRun() returns the next run in the enumeration, as described above.
   *  If the runs have been exhausted, it returns null.  The return value is
   *  a TypeAndSize object, which is nothing more than a way to return two
   *  integers at once.
   *  @return the next run in the enumeration, represented by a TypeAndSize
   *          object.
   */

  public TypeAndSize nextRun() {
	  if(!(hasNext(runPointer)))
		  return null;
	  
	  TypeAndSize ts = new TypeAndSize(runPointer.getRunType(), runPointer.getRunLength());
	  runPointer = runPointer.getNext();
	  
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
	  Run currentRun = getFirst();
	  int i = 0;
	  
	  while(hasNext(currentRun)) {
		  
		  for(int x = 0; x < currentRun.getRunLength(); x++) {
			  if(currentRun.getRunType() == Ocean.SHARK)
				  returnOcean.addShark(i % width, i / width, currentRun.getHungerVal());
			  else if(currentRun.getRunType() == Ocean.FISH)
				  returnOcean.addFish(i % width, i / width);
			  
			  i++;
		  }
		  currentRun = currentRun.getNext();
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
	  header = new Run(null, trailer);
	  trailer = new Run(header, null);
	  
	  while(current < totalLength) {
		  type = sea.cellContents(current);
		  hunger = sea.sharkFeeding(current);
		  current++;
		  runLength = 1;
		  
		  while((current < totalLength) && (type == sea.cellContents(current)) && (hunger == sea.sharkFeeding(current))) {
			  current++;
			  runLength++;
		  }
		  
		  insertLast(new Run(runLength, type, hunger));
	  }
	  
	  runPointer = getFirst();
	  
    check();
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
	  Run insertionRun = prepInsertionRun(x, y);
	  
	  if(insertionRun == null)
		  return;
				  
	  Run start = insertionRun.getPrev().getPrev();
	  Run end = insertionRun.getNext().getNext();
	  insertionRun.setRunType(Ocean.FISH);
	  insertionRun.setHungerVal(-1);
	  
	  smoothOver(start, end);
	  
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
	  Run insertionRun = prepInsertionRun(x, y);
	  
	  if(insertionRun == null)
		  return;
				  
	  Run start = insertionRun.getPrev().getPrev();
	  Run end = insertionRun.getNext().getNext();
	  insertionRun.setRunType(Ocean.SHARK);
	  insertionRun.setHungerVal(starveVal);
	  
	  smoothOver(start, end);
  
  check();
  }
  
  private Run prepInsertionRun(int x, int y) {
	  Run currentRun = getFirst();
	  int insertionPoint = wrap(x, y);
	  int position = 0;
	  int totalLength = width * height;
	  
	  while(position < totalLength) {
		  if(insertionPoint > currentRun.getRunLength() + position - 1) {
			  position += currentRun.getRunLength();
			  currentRun = currentRun.getNext();
			  continue;
		  }
		  
		  break;
	  }
	  
	  if((position >= totalLength) || (currentRun.getRunType() != Ocean.EMPTY))
		  return null; // Either this is a run of type EMPTY or insertionPoint is out of bounds
	  
	  Run start = currentRun.getPrev();
	  int currentLength = currentRun.getRunLength();
	  int currentType = Ocean.EMPTY;
	  int currentHunger = -1;
	  
	  insertAfter(start, new Run(insertionPoint - position, currentType, currentHunger));
	  insertAfter(currentRun, new Run(currentLength - currentRun.getPrev().getRunLength() - 1, currentType,  currentHunger));
	  currentRun.setRunLength(1);
	  
	  return currentRun;
  }
  
  private void smoothOver(Run start, Run end) {
	  Run currentRun = start;
	  
	  while(currentRun != end.getNext()) {
		  if(currentRun.next.getRunLength() == 0)
			  remove(currentRun.getNext());
		  
		  else if((currentRun.getRunType() == currentRun.getNext().getRunType())
				  && (currentRun.getHungerVal() == currentRun.getNext().getHungerVal())) {
			  
			  currentRun.setRunLength(currentRun.getRunLength() + currentRun.getNext().getRunLength());
			  remove(currentRun.getNext());
		  }
		  
		  else
			  currentRun = currentRun.getNext();
	  }
  }
  
  int wrap(int x, int y) {return (x % width) + (y * width);}

  /**
   *  check() walks through the run-length encoding and prints an error message
   *  if two consecutive runs have the same contents, or if the sum of all run
   *  lengths does not equal the number of cells in the ocean.
   */

  public void check() throws IllegalStateException {
	  Run currentRun = getFirst();
	  int curLength = 0;
	  int totalLength = width * height;
	  
	  while(hasNext(currentRun)) {
		  if((currentRun.getRunType() == currentRun.getNext().getRunType())
			&&(currentRun.getHungerVal() == currentRun.getNext().getHungerVal())) {
			  
			  currentRun = currentRun.getNext();
			  break;
		  }
		  
		  curLength += currentRun.getRunLength();
		  currentRun = currentRun.getNext();
	  }
	  
	  if(curLength != totalLength)
		   throw new IllegalStateException("Sum of Run lengths not equal to RunLengthEncoding length ");
	  
	  else if(currentRun.getRunType() == currentRun.getPrev().getRunType()
			 && currentRun.getHungerVal() == currentRun.getPrev().getHungerVal())
		  throw new IllegalStateException("RunLengthEncoding contains consecutive runs of same runType and hungerVal");
	  
  }
  
  public Run getFirst() throws NullPointerException {
	  Run returnNode = header.getNext();
	  
	  if(returnNode == trailer)
		  throw new NullPointerException("trailer should not be returned from getFirst()");
	  
	  return returnNode;
  }
  
  public Run getLast() {
	  Run returnNode = trailer.getPrev();

	  if(returnNode == header)
		  throw new NullPointerException("header should not be returned from getFirst()");
	  
	  return returnNode;
  }
  
  public boolean hasNext(Run r) {return r != trailer;}
  
  public boolean hasPrev(Run r) {return r != header;}

  public void insertFirst(Run newRun) {insertAfter(header, newRun);}

  public void insertLast(Run newRun) {insertBefore(trailer, newRun);}
  
  public void insertBefore(Run after, Run newRun) throws IllegalArgumentException {
	  if(after == header)
		  throw new IllegalArgumentException("Can not insert before header");
	  
	  Run before = after.getPrev();
	  before.setNext(newRun);
	  after.setPrev(newRun);
	  newRun.setNext(after);
	  newRun.setPrev(before);
  }

  public void insertAfter(Run before, Run newRun) throws IllegalArgumentException {
	  if(before == trailer)
		  throw new IllegalArgumentException("Can not insert after trailer");
	  
	  Run after = before.getNext();
	  after.setPrev(newRun);
	  before.setNext(newRun);
	  newRun.setPrev(before);
	  newRun.setNext(after);
  }
  
  public void remove(Run toRemove) {
	  Run before = toRemove.getPrev();
	  Run after = toRemove.getNext();
	  
	  before.setNext(after);
	  after.setPrev(before);
  }
  
  public String toString() {
	  String s = "";
	  Run currentRun = getFirst();
	  
	  while(hasNext(currentRun)) {
		  s += currentRun + "\n";
		  currentRun = currentRun.getNext();
	  }
	  
	  return s + "\n";
  }

  class Run {
	  private int runLength;
	  private int runType;
	  private int hungerVal;
	  private Run prev;
	  private Run next;
	  
	  // This constructor is used to initialize the header and trailer nodes.
	  Run(Run p, Run n) {
		  prev = p;
		  next = n;
		  runLength = -1;
		  runType = -1;
		  hungerVal = -1;
	  }
	  
	  Run(int length, int type, int hunger) {
		  runLength = length;
		  runType = type;
		  hungerVal = hunger;
	  }
	  
	  Run(int length, int type) {this(length, type, -1);}
	  
	  Run(int length) {this(length, Ocean.EMPTY);}
	  
	  public int getRunLength(){return runLength;}
	  
	  public int getRunType(){return runType;}
	  
	  public int getHungerVal(){return hungerVal;}
	  
	  public Run getPrev() {return prev;}
	  
	  public Run getNext() {return next;}
	  
	  void setPrev(Run p) {prev = p;}
	  
	  void setNext(Run n) {next = n;}

	  void setRunType(int type) {runType = type;}
	  
	  void setRunLength(int length) {runLength = length;}
	  
	  void setHungerVal(int hunger) {hungerVal = hunger;}
	  
	  public String toString() {
		  String type;
		  
		  switch(runType) {
		  
		  case Ocean.EMPTY:
			  type = "Empty";
			  break;
		  case Ocean.FISH:
			  type = "Fish";
			  break;
		  case Ocean.SHARK:
			  type = "Shark";
			  break;
		  default:
			  type = "Invalid Type";
		  }
		  
		  return "[" + type + ", " + runLength + ", " + hungerVal + "]";
		  
	  }
  }
}