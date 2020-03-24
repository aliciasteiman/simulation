package cellsociety.visualization;

import cellsociety.Cell;
import cellsociety.Grid;
import javafx.scene.shape.Rectangle;

public class RectangleGridView extends GridView{
    public RectangleGridView(double width, double height) {
        super(width, height);
    }

    @Override
    public void visualizeGrid(Grid grid) {
        double xShift = 0;
        double yShift = 0;
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                Cell c = grid.getCell(row, col);
                Rectangle r = new Rectangle(cellWidth, cellHeight);
                r.setX(xShift);
                r.setY(yShift);
                r.setId("cell" + row + col);
                c.setShape(r);
                gridDisplay.getChildren().add(r);
                xShift += cellWidth;
            }
            yShift += cellHeight;
            xShift = 0;
        }
    }
}
