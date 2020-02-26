package cellsociety;

import javafx.scene.shape.Shape;

public class Cell {

    private Shape myShape;
    private boolean isAlive;
    private int myRow;
    private int myCol;

    public Cell(int row, int col, boolean bool) {
        myRow = row;
        myCol = col;
        isAlive = bool;
    }

    public int getRow() { return myRow; }

    public int getCol() { return myCol; }

    public void setShape(Shape s) {
        myShape = s;
    }

    public Shape getShape() {
        return myShape;
    }

    public boolean getStatus() {
        return isAlive;
    }

    public void setStatus(boolean newStatus) {
        isAlive = newStatus;
    }
}
