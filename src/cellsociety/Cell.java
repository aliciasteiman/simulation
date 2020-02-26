package cellsociety;

import javafx.scene.shape.Shape;

/**
 * Represents a single Cell object that might appear in a Grid
 * Has a shape and a state
 */
public class Cell {

    private Shape myShape;
    private boolean isAlive;

    /**
     * Constructor for a Cell object, sets the status of a Cell (dead, alive)
     * @param bool
     */
    public Cell(boolean bool) {
        isAlive = bool;
    }

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
