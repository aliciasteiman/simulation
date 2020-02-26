package cellsociety;

import javafx.scene.shape.Shape;

/**
 * Represents a single Cell object that might appear in a Grid
 * Has a shape and a state
 */
public class Cell {

    private Shape myShape;
    private boolean isAlive;
    private int myRow;
    private int myCol;

    /**
     * Constructor for a Cell object, sets the status of a Cell (dead, alive) and its position (row, col)
     * @param bool
     */
    public Cell(int row, int col, boolean bool) {
        myRow = row;
        myCol = col;
        isAlive = bool;
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
    public boolean getStatus() {
        return isAlive;
    }

    /**
     * Allows for the status of a Cell to be changed
     * @param newStatus
     */
    public void setStatus(boolean newStatus) {
        isAlive = newStatus;
    }
}
