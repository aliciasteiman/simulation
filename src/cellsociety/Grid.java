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
        initGrid();
    }

    /**
     * Initializes the Grid to have all empty cells at first.
     */
    public void initGrid() {
        for (int r = 0; r < NUM_ROWS; r++) {
            ArrayList<Cell> row = new ArrayList<>();
            for (int c = 0; c < NUM_COLS; c++) {
                row.add(new Cell(false));
            }
            allCells.add(row);
        }
    }

    public void updateAllCells(List<List<Cell>> cells) {
        allCells = cells;
    }

    public int getRows() {
        return NUM_ROWS;
    }

    public int getCols() {
        return NUM_COLS;
    }

    public Cell getCell(int row, int col) {
        return allCells.get(row).get(col);
    }

    public int countAliveNeighbors(List<Cell> neighbors) {
        int count  = 0;
        for (Cell cell : neighbors) {
            if (cell.getStatus() == true) {
                count += 1;
            }
        }
        return count;
    }

}
