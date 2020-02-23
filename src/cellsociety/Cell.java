package cellsociety;

import javafx.scene.shape.Shape;

public class Cell {

    private Shape myShape;
    private boolean isAlive;

    public Cell(boolean bool) {
        isAlive = bool;
    }

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
