package cellsociety.visualization;

import cellsociety.Grid;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.util.Map;


public abstract class GridView {
    protected Pane gridDisplay;
    protected double cellHeight;
    protected double cellWidth;
    protected Grid grid;
    protected double myGridWidth;
    protected double myGridHeight;

    public GridView(double width, double height) {
        myGridWidth = width;
        myGridHeight = height;
        gridDisplay = new Pane();
        gridDisplay.setId("grid-pane");
        gridDisplay.setPrefWidth(width);
        gridDisplay.setPrefHeight(height);

    }

    public Pane getInitGridPane() {return gridDisplay;}

    /**
     * Determines the cell width, height, and creates the Grid appearance.
     */
    public Pane handleGridSetUp(Grid g) {
        grid = g;
        cellHeight = myGridHeight/ grid.getRows();
        cellWidth = myGridWidth/ grid.getCols();
        visualizeGrid(grid);
        return gridDisplay;
    }

    /**
     * Creates the grid depending on the shape of the cells and type of neighborhood.
     */
    public abstract void visualizeGrid(Grid grid);

    public void changeCellColors(String state, String newColor) {
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols();c++) {
                if (grid.getCell(r,c).getStatus().equals(state)) {
                    grid.getCell(r,c).getShape().setFill(Paint.valueOf(newColor));
                }
            }
        }
    }

    public void changeCellImages(Map<String, ImagePattern> images, String state) {
        for (int r = 0; r < grid.getRows(); r++) {
            for (int c = 0; c < grid.getCols();c++) {
                if (grid.getCell(r,c).getStatus().equals(state)) {
                    grid.getCell(r,c).getShape().setFill(images.get(state));
                }
            }
        }
    }



}
