package cellsociety;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int NUM_ROWS;
    private int NUM_COLS;
    private List<List<Cell>> allCells;

    public Grid(int rows, int cols) {
        NUM_ROWS = rows;
        NUM_COLS = cols;
        allCells = new ArrayList<>();
        initialGrid();
    }

    /**
     * Initializes the Grid to have all empty cells
     * GOL - empty = dead
     */
    private void initialGrid() {
        for (int r = 0; r < NUM_ROWS; r++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int c = 0; c < NUM_COLS; c++) {
                row.add(new Cell(r, c, ""));
            }
            allCells.add(row);
        }
    }

    /**
     * @return NUM_ROWS - number of rows in a Grid
     */
    public int getRows() {
        return NUM_ROWS;
    }

    /**
     * @return NUM_COLS - number of columns in a Grid
     */
    public int getCols() {
        return NUM_COLS;
    }

    /**
     * Given a specific row and column, get the Cell object in that spot from the List<List<Cell>>
     * @param row - row of desired cell
     * @param col - column of desired cell
     * @return Cell
     */
    public Cell getCell(int row, int col) {
        return allCells.get(row).get(col);
    }

    public void setCell(Cell c) {
        allCells.get(c.getRow()).set(c.getCol(), c);
    }

    /**
     * Given a List of Cell objects representing all of a single cell's neighbors, count how many neighbors
     * are full/alive (i.e. cell.getStatus() == true)
     * @param neighbors
     * @return count - represents the number of alive neighbors
     */
    public int countNeighbors(List<Cell> neighbors, String status) {
        int count  = 0;
        for (Cell cell : neighbors) {
            if (cell.getStatus().equals(status)) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * Based on two lists passed in, this method helps calculate the specified number of neighbors.
     * @param row - row of current cell
     * @param col - column of current cell
     * @param indexR - all the index locations in the row that should be considered neighbors
     * @param indexC - all the index locations in the column that should be considered neighbors
     * @param g - the Grid from which the cell's neighbors will be calculated from
     * @return - list of Cells representing the neighbors
     */
    public List<Cell> getSpecifiedNeighbors(int row, int col, int[] indexR, int[] indexC, Grid g) {
        List<Cell> myNeighbors = new ArrayList<>();
        for (int i = 0; i < indexR.length; i++) {
            int currR = row + indexR[i];
            int currC = col + indexC[i];
            if (currR < g.getRows() && currC < g.getCols() && currR >= 0 && currC >= 0) {
                myNeighbors.add(g.getCell(currR, currC));
            }
        }
        return myNeighbors;
    }

}
