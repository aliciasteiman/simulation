package cellsociety;

import java.util.ArrayList;
import java.util.List;

/**
 * Game of Life simulation -- models the life cycle of bacteria
 * Each grid location is either empty (dead) or occupied by a single cell (alive)
 * A location's neighbors are any cells in the surrounding eight locations
 */
public class GameOfLife extends SimulationModel {

    private List<Cell> myNeighbors;

    public static final String ALIVE_STYLE = "alive-cell";
    public static final String DEAD_STYLE = "dead-cell";

    /**
     * Constructor for a GameOfLife simulation; calls super
     * @param file
     */
    public GameOfLife(String file) {
        super(file);
    }

    /**
     * Loops through the Cell objects in the Grid and updates the Cell according to the RULES:
     * Any live cell with two or three neighbors survives
     * Any dead cell with three live neighbors comes back to life
     * All other live cells die, and all other dead cells stay dead
     * @return
     */
    @Override
    public Grid updateCells() {
        List<List<Cell>> updatedGrid = new ArrayList<>();
        for (int r = 0; r < mySimulationGrid.getRows(); r++) {
            List<Cell> updatedRow = new ArrayList<>();
            for (int c = 0; c < mySimulationGrid.getCols(); c++) {
                Cell thisCell = mySimulationGrid.getCell(r, c);
                int numLiveNeighbors = mySimulationGrid.countAliveNeighbors(getNeighbors(r,c));
                boolean isAlive = ((thisCell.getStatus() && numLiveNeighbors == 2) || numLiveNeighbors == 3);
                updatedRow.add(new Cell(isAlive));
            }
            updatedGrid.add(updatedRow);
        }
        mySimulationGrid.updateAllCells(updatedGrid);
        return mySimulationGrid;
    }

    /**
     * Creates a List<Cell> where each Cell represents the neighbors of the Cell at the passed in row, col
     * Checks for 8 adjacent neighbors (up, down, left, right, diagonals)
     * Passed into countAliveNeighbors() in Grid
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     * @return
     */
    @Override
    public List<Cell> getNeighbors(int row, int col) {
        myNeighbors = new ArrayList<>();
        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};

        for (int i = 0; i < 8; i++) {
            int currR = row + indexR[i];
            int currC = col + indexC[i];
            if (currR < mySimulationGrid.getRows() && currC < mySimulationGrid.getCols() && currR >= 0 && currC >= 0) {
                myNeighbors.add(mySimulationGrid.getCell(currR, currC));
            }
        }
        return myNeighbors;
    }

    /**
     * Updates the style of a Cell (alive vs. dead)
     * @param c - Cell to be updated
     */
    @Override
    public void updateCellStyle(Cell c) {
        if (c.getStatus()==true) {
            c.getShape().getStyleClass().add(ALIVE_STYLE);
        } else {
            c.getShape().getStyleClass().add(DEAD_STYLE);
        }
    }

    /**
     * Used in readGridFromFile() in SimulationModel to set the status of a Cell to true/false depending
     * on its character representation in the configuration file
     * @param row - the row that the cell is on
     * @param col - the column that the cell is on
     * @param ch  - the character that corresponds to the value that cell will take
     */
    @Override
    public void setCellFromFile(int row, int col, char ch) {
        if (ch == '1') {
            mySimulationGrid.getCell(row, col).setStatus(true);
        }
    }
}