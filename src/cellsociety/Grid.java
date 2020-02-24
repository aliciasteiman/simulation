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
                row.add(new Cell(false));
            }
            allCells.add(row);
        }
    }

    /**
     * Sets allCells equal to an updated List<List<Cell>> so that the Grid updates correctly
     * @param cells - each Cell object's state is updated
     */
    public void updateAllCells(List<List<Cell>> cells) {
        allCells = cells;
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

    /**
     * Given a List of Cell objects representing all of a single cell's neighbors, count how many neighbors
     * are full/alive (i.e. cell.getStatus() == true)
     * @param neighbors
     * @return count - represents the number of alive neighbors
     */
    public int countAliveNeighbors(List<Cell> neighbors) {
        int count  = 0;
        for (Cell cell : neighbors) {
            if (cell.getStatus() == true) {
                count += 1;
            }
        }
        return count;
    }

    /**
     * @return allCells - List<List<Cell>> representing all Cell objects in a Grid
     */
    public List<List<Cell>> getAllCells() {
        return allCells;
    }

}
