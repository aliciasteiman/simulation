package cellsociety.visualization;

import cellsociety.Cell;
import cellsociety.Grid;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class TriangleGridView extends GridView {

    public TriangleGridView(double width, double height) {
        super(width, height);
    }

    @Override
    public void visualizeGrid(Grid grid) {
        double rowShift = 0;
        double colShift = 0;
        adjustTriangleCellWidth(grid);
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                Shape triangle;
                if (row %2==0 && col %2==0) { //right side up @ start
                    triangle = createTriangle(colShift, cellHeight + rowShift,
                            colShift + cellWidth/2, rowShift,
                            colShift + cellWidth, cellHeight + rowShift);
                }
                else if (row%2==0 && col %2!=0) { //upside down halfway
                    triangle = createTriangle(colShift + cellWidth/2, rowShift,
                            colShift + cellWidth, rowShift + cellHeight,
                            colShift + cellWidth + cellWidth/2, rowShift);

                    colShift += cellWidth;
                }
                else if (col%2==0 && row%2!=0) { //upside down @ start
                    triangle = createTriangle(colShift, rowShift,
                            colShift + cellWidth, rowShift,
                            colShift + cellWidth/2, rowShift+cellHeight);
                }
                else { //col%2!=0 && row%2!=0; right side up halfway
                    triangle = createTriangle(colShift + cellWidth/2, rowShift + cellHeight,
                            colShift + cellWidth + cellWidth/2, rowShift + cellHeight,
                            colShift + cellWidth, rowShift);
                    colShift += cellWidth;
                }
                Cell c = grid.getCell(row,col);
                c.setShape(triangle);
                triangle.setId("cell"+row+col);
                gridDisplay.getChildren().add(triangle);
            }
            rowShift += cellHeight;
            colShift = 0;
        }
    }

    private void adjustTriangleCellWidth(Grid grid) {
        cellWidth = myGridWidth / ((grid.getCols()/2)+0.5); //for even number of cols
        if (grid.getCols() %2 != 0) {
            cellWidth = myGridWidth / ((grid.getCols()+1)/2); //for odd number of cols
        }
    }

    private Shape createTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(new Double[]{x1, y1, x2, y2, x3, y3});
        return triangle;
    }
}
