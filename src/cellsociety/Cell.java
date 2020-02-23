package cellsociety;

import javafx.scene.shape.Shape;

public class Cell {

    private Shape myShape;
    private boolean isAlive;

    public Cell(boolean bool) {
        isAlive = bool;
    }
}
