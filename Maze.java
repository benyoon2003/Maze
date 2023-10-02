import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;
import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// Represents a cell
abstract class ACell {
  Color color;
  boolean leftLine;
  boolean rightLine;
  boolean topLine;
  boolean bottomLine;
  int id;
  ACell left;
  ACell right;
  ACell top;
  ACell bottom;
  ArrayList<ACell> asgnList;
  ArrayList<ACell> validNeighbors;

  ACell() {}
  /* TEMPLATE
   * FIELDS:
   * ... this.color ...   -- Color
   * ... this.leftLine ...    -- Boolean
   * ... this.rightLine ...   -- Boolean
   * ... this.topLine ...   -- Boolean
   * ... this.bottomLine ...    -- Boolean
   * ... this.id ...    -- int
   * ... this.left ...    -- ACell
   * ... this.right ...   -- ACell
   * ... this.top ...   -- ACell
   * ... this.bottom ...    -- ACell
   * ... this.asgnList ...    -- ArrayList<ACell>
   * ... this.validNeighbors ...    -- ArrayList<ACell>
   * 
   * METHODS:
   * ... this.assignAdjacent(int row, int col, 
   * ArrayList<ArrayList<ACell>> board, int rowSize, int colSize) ...   -- void
   * ... this.makeCell(int rowSize, int colSize) ...    -- WorldImage
   * ... setList(ArrayList<ACell> newList) ...    -- void
   * ... swapArrays(ACell other, ArrayList<ACell> mainList, 
   * boolean horizontal) ...    -- ArrayList<ACell> 
   * ... swapArraysHelper(Cell other, ArrayList<ACell> mainList, 
   * boolean horizontal) ...    -- ArrayList<ACell>
   * ... sameArray(ACell other) ...   -- boolean
   * ... removeLine(int line) ...   -- void
   * ... changeColor(Color c) ...   -- void
   * ... append(ArrayList<ACell> neighbors) ...   -- void
   * ... findValidNeighbors() ...   -- void;
   * 
   * METHODS OF FIELDS:
   * ... this.asgnList.equals(other.asgnList) ...   -- boolean
   * 
   */

  // assigns neighboring cells
  public void assignAdjacent(int row, int col, ArrayList<ArrayList<ACell>> board,
      int rowSize, int colSize) {
    if (col != 0) {
      this.top = board.get(row).get(col - 1);
    } 
    else {
      this.top = new MtCell();
    }
    if (col < colSize - 1) {
      this.bottom = board.get(row).get(col + 1);
    } 
    else {
      this.bottom = new MtCell();
    }
    if (row != 0) {
      this.left = board.get(row - 1).get(col);
    } 
    else {
      this.left = new MtCell();
    }
    if (row < rowSize - 1) {
      this.right = board.get(row + 1).get(col);
    } 
    else {
      this.right = new MtCell();
    }
  }

  // creates a cell
  WorldImage makeCell(int rowSize, int colSize) {

    int dim = colSize;
    if (rowSize > colSize) {
      dim = rowSize;
    }

    WorldImage image = new RectangleImage(700 / dim, 700 / dim, OutlineMode.SOLID, this.color);

    if (this.topLine) {
      image =  new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.TOP,
          new LineImage(new Posn(700 / dim, 0), Color.BLACK),
          0, 0, image);
    }

    if (this.bottomLine) {
      image = new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.BOTTOM,
          new LineImage(new Posn(700 / dim, 0), Color.BLACK),
          0, 0, image);
    }

    if (this.leftLine) {
      image = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.MIDDLE,
          new LineImage(new Posn(0, 700 / dim), Color.BLACK),
          0, 0, image);
    }

    if (this.rightLine) {
      image = new OverlayOffsetAlign(AlignModeX.RIGHT, AlignModeY.MIDDLE,
          new LineImage(new Posn(0, 700 / dim), Color.BLACK),
          0, 0, image);
    }

    return image; 
  }

  // sets the given list as the asgnList
  public void setList(ArrayList<ACell> newList) {
    this.asgnList = newList; 
  }

  //removes the line between the two cells and appends their arrays
  abstract public ArrayList<ACell> swapArrays(ACell other, 
      ArrayList<ACell> mainList, boolean horizontal);

  //helps the ArrayList function with double dispatch 
  abstract public ArrayList<ACell> swapArraysHelper(Cell other, 
      ArrayList<ACell> mainList, boolean horizontal);

  //determines whether two lists of two different cells are the same
  public boolean sameArray(ACell other) {
    return this.asgnList.equals(other.asgnList);
  }

  // removes the given line
  public void removeLine(int line) {
    if (line == 0) {
      this.leftLine = false; 
    } else if (line == 1) {
      this.topLine = false;
    } else if (line == 2) {
      this.rightLine = false; 
    } else if (line == 3) {
      this.bottomLine = false;
    } else {
      throw new RuntimeException("line needs to be 3 or less");
    }
  }

  // changes the color of this cell with the given color
  public void changeColor(Color c) {
    this.color = c;
  }

  // adds the given array of ACell to this array of ACell
  public void append(ArrayList<ACell> neighbors) {
    neighbors.add(this);
  }

  // finds the valid neighbors that do not have a line between
  public abstract void findValidNeighbors();

}

// represents a cell
class Cell extends ACell {
  int x;
  int y;

  Cell(Color color, int x, int y, int id) {
    this.color = color;
    this.x = x;
    this.y = y;
    this.id = id;

    this.bottomLine = true; 
    this.topLine = true;
    this.rightLine = true; 
    this.leftLine = true; 

    this.asgnList = new ArrayList<ACell>();
    this.asgnList.add(this);
    this.validNeighbors = new ArrayList<ACell>();
  } 

  /* TEMPLATE
   * FIELDS:
   * ... this.x ...    -- int
   * ... this.y ...    -- int
   * ... this.color ...   -- Color
   * ... this.leftLine ...    -- Boolean
   * ... this.rightLine ...   -- Boolean
   * ... this.topLine ...   -- Boolean
   * ... this.bottomLine ...    -- Boolean
   * ... this.id ...    -- int
   * ... this.left ...    -- ACell
   * ... this.right ...   -- ACell
   * ... this.top ...   -- ACell
   * ... this.bottom ...    -- ACell
   * ... this.asgnList ...    -- ArrayList<ACell>
   * ... this.validNeighbors ...    -- ArrayList<ACell>
   * 
   * METHODS:
   * ... this.swapArrays(ACell other, ArrayList<ACell> mainList, 
   * boolean horizontal) ...    -- ArrayList<ACell> 
   * ... this.swapArraysHelper(Cell other, ArrayList<ACell> mainList, 
   * boolean horizontal) ...    -- ArrayList<ACell>
   * ... this.findValidNeighbors() ...   -- void;
   * 
   * METHODS OF FIELDS:
   * ... this.validNeighbors.add(this.bottom) ...   -- void
   * ... this.bottom.equals(new MtCell())) ...    -- boolean
   * ... this.top.equals(new MtCell())) ...   -- boolean
   * ... this.right.equals(new MtCell())) ...   -- boolean
   * ... this.left.equals(new MtCell())) ...    -- boolean
   * ... this.asgnList.equals(mainList) ...   -- boolean
   * ... this.asgnList.get(i).setList(mainList) ...   -- void
   * ... this.asgnList.addAll(other.asgnList) ...   -- void
   * 
   * 
   */


  // finds the valid neighbors that do not have a line between
  public void findValidNeighbors() {
    if (!this.bottomLine) {
      this.validNeighbors.add(this.bottom);
      if (this.bottom == null || this.bottom.equals(new MtCell())) {
        throw new RuntimeException("bottom is null");
      }
    }

    if (!this.topLine) {
      this.validNeighbors.add(this.top);
      if (this.top == null || this.top.equals(new MtCell())) {
        throw new RuntimeException("top is null");
      }
    }

    if (!this.rightLine) {
      this.validNeighbors.add(this.right);
      if (this.right == null || this.right.equals(new MtCell())) {
        throw new RuntimeException("right is null");
      }
    }

    if (!this.leftLine) {
      this.validNeighbors.add(this.left);
    }
    if (this.left == null || this.left.equals(new MtCell())) {
      throw new RuntimeException("left is null");
    }

  }

  //removes the line between the two cells and appends their arrays
  public ArrayList<ACell> swapArrays(ACell other, ArrayList<ACell> mainList, boolean horizontal) {
    return other.swapArraysHelper(this, mainList, horizontal);
  }

  //helps the ArrayList function with double dispatch
  public ArrayList<ACell> swapArraysHelper(Cell other, ArrayList<ACell> mainList, 
      boolean horizontal) {
    if (this.asgnList.equals(mainList)) {
      mainList.addAll(other.asgnList);
      for (int i = 0; i < other.asgnList.size(); i++) {
        other.asgnList.get(i).setList(mainList);
      }
    } else if (other.asgnList.equals(mainList)) {
      mainList.addAll(this.asgnList);
      for (int i = 0; i < this.asgnList.size(); i++) {
        this.asgnList.get(i).setList(mainList);
      }
    } else {
      this.asgnList.addAll(other.asgnList);
      for (int i = 0; i < other.asgnList.size(); i++) {
        other.asgnList.get(i).setList(this.asgnList);
      }
    }
    if (horizontal) {
      this.removeLine(1);
      other.removeLine(3);
    } else {
      this.removeLine(0);
      other.removeLine(2);
    }
    return mainList;
  }

}

// Represents an empty Cell
class MtCell extends ACell {
  MtCell() {}

  /* TEMPLATE
   * FIELDS:
   * ... this.color ...   -- Color
   * ... this.leftLine ...    -- Boolean
   * ... this.rightLine ...   -- Boolean
   * ... this.topLine ...   -- Boolean
   * ... this.bottomLine ...    -- Boolean
   * ... this.id ...    -- int
   * ... this.left ...    -- ACell
   * ... this.right ...   -- ACell
   * ... this.top ...   -- ACell
   * ... this.bottom ...    -- ACell
   * ... this.asgnList ...    -- ArrayList<ACell>
   * ... this.validNeighbors ...    -- ArrayList<ACell>
   * 
   * METHODS:
   * ... swapArrays(ACell other, ArrayList<ACell> mainList, 
   * boolean horizontal) ...    -- ArrayList<ACell> 
   * ... swapArraysHelper(Cell other, ArrayList<ACell> mainList, 
   * boolean horizontal) ...    -- ArrayList<ACell>
   * ... findValidNeighbors() ...   -- void
   * 
   */

  //removes the line between the two cells and appends their arrays
  public ArrayList<ACell> swapArrays(ACell other, ArrayList<ACell> mainList, boolean horizontal) {
    throw new RuntimeException("cant call swapArrays on an empty cell!!!");
  }

  //helps the ArrayList function with double dispatch
  public ArrayList<ACell> swapArraysHelper(Cell other, ArrayList<ACell> mainList,
      boolean horizontal) {
    throw new RuntimeException("cant call swapArraysHelper on an empty cell!!!");
  }

  //finds the valid neighbors that do not have a line between
  public void findValidNeighbors() {
    throw new IllegalArgumentException("YIKES");

  }
}

// to represent a MazeWorld
class MazeWorld extends World {
  ArrayList<ArrayList<ACell>> board;
  int rowSize;
  int colSize;
  boolean dfsB;
  boolean bfsB;
  Random rand;
  ArrayList<ArrayList<Boolean>> lineHorizontal;
  ArrayList<ArrayList<Boolean>> lineVertical;
  int count;
  ArrayList<ACell> visited;
  boolean end;


  MazeWorld(int rowSize, int colSize) {
    this.rowSize = rowSize;
    this.colSize = colSize;
    this.makeBoard();

  }

  /* TEMPLATE
   * FIELDS:
   * ... this.board ...   -- ArrayList<ArrayList<ACell>>
   * ... this.rowSize ...   -- int
   * ... this.colSize ...   -- int
   * ... this.dfsB ...    -- boolean
   * ... this.bfsB ...    -- boolean
   * ... this.rand ...    -- Random
   * ... this.lineHorizontal ...    -- ArrayList<ArrayList<Boolean>>
   * ... this.lineVertical ...    -- ArrayList<ArrayList<Boolean>>
   * ... this.count ...   -- int
   * ... this.visited ...   -- ArrayList<ACell>
   * ... this.end ...   -- boolean
   * 
   * METHODS:
   * ... this.makeBoard() ...   -- void
   * ... this.makeScene() ...   -- WorldScene
   * ... this.onTick() ...   -- void
   * ... this.bfs() ...   -- void
   * ... this.dfs() ...   -- void
   * ... this.onKeyEvent() ...    -- void
   * ... this.reconstruct() ...   -- void
   * 
   * METHODS OF FIELDS:
   * ... this.board.add(currRow) ...    -- void
   * ... this.lineVertical.add(currL) ...   -- void
   * ... this.lineHorizontal.add(currL2) ...    -- void
   * ... this.board.get(i).get(j).assignAdjacent(i, j, this.board, 
   * this.rowSize, this.colSize) ...    -- void
   * ... this.board.get(0).get(0) ...   -- ACell
   * ... this.board.get(0).get(0).setList(mainList) ...   -- void
   * ... this.rand.nextInt(2) ...   -- int
   * ... this.rand.nextInt(this.rowSize) ...    -- int
   * ... this.rand.nextInt(this.colSize - 1) ...    -- int
   * ... this.lineHorizontal.get(ranCol).get(ranRow) ...    -- boolean
   * ... this.board.get(ranCol).get(ranRow)
   * .sameArray(this.board.get(ranCol).get(ranRow + 1)) ...   -- boolean
   * ... this.lineHorizontal.get(ranCol).set(ranRow, false) ...   -- void
   * ... this.board.get(ranCol).get(ranRow)
   * .swapArrays(this.board.get(ranCol).get(ranRow + 1),
   *  mainList, true) ...    -- ArrayList<ACell>
   * ... this.rand.nextInt(this.rowSize - 1) ...    -- int
   * ... this.rand.nextInt(this.colSize) ...    -- int
   * ... this.lineVertical.get(ranCol).get(ranRow) ...    -- boolean
   * ... this.board.get(ranCol).get(ranRow)
   * .sameArray(this.board.get(ranCol + 1).get(ranRow))   -- boolean
   * ... this.lineVertical.get(ranCol).set(ranRow, false) ...   -- boolean
   * ... this.board.get(ranCol).get(ranRow)
   * .swapArrays(this.board.get(ranCol + 1)
   * .get(ranRow), mainList, false) ...   -- ArrayList<ACell>
   * ... this.board.get(i).get(j).findValidNeighbors() ...    -- void
   * ... this.board.get(0).get(0).changeColor(Color.GREEN) ...    -- void
   * ... this.board.get(this.rowSize - 1).get(this.colSize - 1)
   * .changeColor(Color.MAGENTA) ...    -- void
   * ... this.visited.contains(current) ...   -- boolean
   * ... this.board.get(this.rowSize - 1).get(this.colSize - 1)
   * .equals(current) ...   -- boolean
   * ... this.visited.add(board.get(this.rowSize - 1)
   * .get(this.colSize - 1) ...   -- void
   * ... this.visited.contains(c) ...   -- boolean
   * ... this.visited.contains(current) ...   -- boolean
   * ... this.board.get(this.rowSize - 1)
   * .get(this.colSize - 1).equals(current) ...   -- boolean
   * ... this.board.get(0).get(0) ...   -- ACell
   * ... this.board.get(this.rowSize - 1).get(this.colSize - 1)
   * .equals(current) ...   -- boolean
   * ... this.visited.add(board.get(this.rowSize - 1)
   * .get(this.colSize - 1)...   -- void
   * ... this.visited.size() ...    -- int
   * ... this.visited.get(this.count).changeColor(Color.BLUE)... -- void
   * 
   */

  // Creates the board
  public void makeBoard() {
    this.rand = new Random();
    this.board = new ArrayList<ArrayList<ACell>>();
    lineVertical  = new ArrayList<ArrayList<Boolean>>();
    lineHorizontal = new ArrayList<ArrayList<Boolean>>();
    this.end = false;
    for (int i = 0; i < rowSize; i ++) {
      ArrayList<ACell> currRow = new ArrayList<ACell>();
      for (int j = 0; j < colSize; j ++) {
        int id = i + j;
        ACell currCell = new Cell(Color.GRAY, i, j, id);
        currRow.add(currCell);

      }
      this.board.add(currRow);
    }

    for (int i = 0; i < rowSize; i ++) {
      ArrayList<Boolean> currL = new ArrayList<Boolean>();
      ArrayList<Boolean> currL2 = new ArrayList<Boolean>();
      for (int j = 0; j < colSize; j ++) {
        currL.add(true);
        currL2.add(true);
      }
      this.lineVertical.add(currL);
      this.lineHorizontal.add(currL2);
    }

    for (int i = 0; i < rowSize; i ++) {
      for (int j = 0; j < colSize; j ++) {
        this.board.get(i).get(j).assignAdjacent(i, j, this.board, 
            this.rowSize, this.colSize);
      }
    }

    ArrayList<ACell> mainList = new ArrayList<ACell>();
    mainList.add(this.board.get(0).get(0));
    this.board.get(0).get(0).setList(mainList);

    while (mainList.size() < this.colSize * this.rowSize) {
      int hHuh = this.rand.nextInt(2);
      if (hHuh == 1) {
        int ranCol = this.rand.nextInt(this.rowSize);
        int ranRow = this.rand.nextInt(this.colSize - 1);
        if (this.lineHorizontal.get(ranCol).get(ranRow) 
            && !(this.board.get(ranCol).get(ranRow)
                .sameArray(this.board.get(ranCol).get(ranRow + 1)))) {
          this.lineHorizontal.get(ranCol).set(ranRow, false);
          //make it so that it checks for 0, 0, and it if is at 0, 0 work with mainList 
          //and then we check for whether the top or bottom is the mainlist and then do the same
          //if none of those cases are true then we just use
          mainList = this.board.get(ranCol).get(ranRow)
              .swapArrays(this.board.get(ranCol).get(ranRow + 1), mainList, true);
        }
      }
      else {
        int ranCol = this.rand.nextInt(this.rowSize - 1);
        int ranRow = this.rand.nextInt(this.colSize);
        if (this.lineVertical.get(ranCol).get(ranRow)
            && !(this.board.get(ranCol).get(ranRow)
                .sameArray(this.board.get(ranCol + 1).get(ranRow)))) {
          this.lineVertical.get(ranCol).set(ranRow, false);
          //make it so that it checks for 0, 0, and it if is at 0, 0 work with mainList 
          //and then we check for whether the top or bottom is the mainlist and then do the same
          //if none of those cases are true then we just use
          mainList = this.board.get(ranCol).get(ranRow)
              .swapArrays(this.board.get(ranCol + 1).get(ranRow), mainList, false);
        }
      }
    }
    for (int i = 0; i < this.rowSize; i ++) {
      for (int j = 0; j < this.colSize; j ++) {
        this.board.get(i).get(j).findValidNeighbors();
      }
    }

    this.board.get(0).get(0).changeColor(Color.GREEN);
    this.board.get(this.rowSize - 1).get(this.colSize - 1).changeColor(Color.MAGENTA);

  }


  // makes the world scene
  public WorldScene makeScene() {
    int dim = this.colSize;
    if (this.rowSize > this.colSize) {
      dim = this.rowSize;
    }
    WorldScene background = new WorldScene(1080, 1000);
    for (int i = 0; i < this.rowSize; i ++) {
      for (int j = 0; j < this.colSize; j ++) {
        background.placeImageXY(this.board.get(i).get(j).makeCell(this.rowSize, this.colSize), 
            (i + 1) * 700 / dim, (j + 1)  * 700 / dim);
      }
    }
    return background;
  }

  // world tick
  public void onTick() {
    this.count += 1;
    this.reconstruct();
  }


  // breadth first search algorithm
  public void bfs() {
    // creates a stack that keeps track of the current cells that must be traversed
    ArrayDeque<ACell> queue = new ArrayDeque<ACell>();

    queue.add(this.board.get(0).get(0));

    while (queue.size() != 0 && !this.end) {
      ACell current = queue.getLast();
      queue.removeLast();
      if (this.visited.contains(current)) {
        // already visited - does nothing
      }

      else if (this.board.get(this.rowSize - 1).get(this.colSize - 1).equals(current)) {
        this.visited.add(board.get(this.rowSize - 1).get(this.colSize - 1));
        this.end = true;
      }

      else {
        this.visited.add(current);
        for (ACell c : current.validNeighbors) {
          if (!this.visited.contains(c)) {
            queue.addFirst(c);

          }
        }
      }
    }
  }

  // depth first search algorithm
  public void dfs() {
    // creates a stack that keeps track of the current cells that must be traversed
    ArrayDeque<ACell> stack = new ArrayDeque<ACell>();

    stack.add(this.board.get(0).get(0));

    while (stack.size() != 0 && !this.end) { 

      ACell current = stack.pop();
      if (this.visited.contains(current)) {
        // already visited - does nothing
      }

      else if (this.board.get(this.rowSize - 1).get(this.colSize - 1).equals(current)) {
        this.visited.add(board.get(this.rowSize - 1).get(this.colSize - 1));
        this.end = true;
      }

      else {
        this.visited.add(current);
        for (int i = 0; i < current.validNeighbors.size(); i++) {
          ACell c  = current.validNeighbors.get(i);
          if (!this.visited.contains(c)) {
            stack.addFirst(c);
          }
        }
      }
    }
  }

  // mutates the color of the traversed cells
  public void reconstruct() {
    if (this.visited != null && this.count < this.visited.size()) {
      this.visited.get(this.count).changeColor(Color.BLUE);
    }
  }


  // mutates the board based on key presses
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.makeBoard();
    }

    if (key.equals("b")) {
      this.visited = new ArrayList<ACell>();
      this.count = 0;
      this.end = false; 
      this.bfs();
      this.bfsB = true; 
      this.dfsB = false;
    }

    if (key.equals("d")) {
      this.visited = new ArrayList<ACell>();
      this.count = 0;
      this.end = false; 
      this.dfs();
      this.bfsB = false; 
      this.dfsB = true;
    }

  }

}

class ExamplesMaze {
  MazeWorld mw = new MazeWorld(50, 60);

  boolean testMakeScene(Tester t) {
    mw.bigBang(1080, 1000, 0.001);
    return true;
  }
}


