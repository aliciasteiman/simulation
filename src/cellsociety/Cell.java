package cellsociety;

import javafx.scene.shape.Shape;

/**
 * Represents a single Cell object that might appear in a Grid
 * Has a shape and a state
 */
public class Cell {

    private Shape myShape;
    private String myState;
    private int myRow;
    private int myCol;
    private int myRep;
    private int myEnergy;

    /**
     * Constructor for a Cell object, sets the status of a Cell (dead, alive) and its position (row, col)
     * @param
     */
    public Cell(int row, int col, String state) {
        myRow = row;
        myCol = col;
        myState = state;
    }

    /**
     * Returns row position of a Cell
     */
    public int getRow() { return myRow; }

    /**
     * Returns column position of a Cell
     */
    public int getCol() { return myCol; }

    /**
     * Sets the shape of a cell (such as rectangle, circle, hexagon, etc.)
     * Used in SimulationView in updateCellAppearance() to set the shape of Cell objects in a Grid
     * @param s
     */
    public void setShape(Shape s) {
        myShape = s;
    }

    /**
     * Returns shape of Cell
     */
    public Shape getShape() {
        return myShape;
    }

    /**
     * Returns status of Cell (isAlive = true/false)
     */
    public String getStatus() {
        return myState;
    }

    /**
     * Allows for the status of a Cell to be changed
     * @param newStatus
     */
    public void setStatus(String newStatus) {
        myState = newStatus;
    }


    //will change these later when creating cell subclasses -- need for wa-tor
    public void setRepCount(int num) {myRep = num; }

    public int getRepCount() {return myRep; }

    public void setEnergy(int num) {myEnergy = num; }

    public int getEnergy() {return myEnergy; }

}
