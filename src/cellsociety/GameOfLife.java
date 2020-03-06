package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Game of Life simulation -- models the life cycle of bacteria
 * Each grid location is either empty (dead) or occupied by a single cell (alive)
 * A location's neighbors are any cells in the surrounding eight locations
 */
public class GameOfLife extends Simulation {
    protected Grid mySimulationGrid;
    public static final String ALIVE_STYLE = "GOL-alive-cell";
    public static final String DEAD_STYLE = "GOL-dead-cell";

    /**
     * Constructor for a GameOfLife simulation
     */
    public GameOfLife() {

    }

    @Override
    public Grid getGrid() { return mySimulationGrid; }

    @Override
    public void setGrid(Grid g) {
        mySimulationGrid = g;
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
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numLiveNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), "alive");
                boolean isAlive = ((currCell.getStatus().equals("alive") && numLiveNeighbors == 2) || numLiveNeighbors == 3);
                Cell newCell = new Cell(i, j, "dead");
                if (isAlive) {
                    newCell = new Cell(i, j, "alive");
                }
                updatedGrid.setCell(i, j, newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return updatedGrid;
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
        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};

        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);

    }

//    /**
//     * Updates the style of a Cell (alive vs. dead)
//     * @param c - Cell to be updated
//     */
//    @Override
//    public void updateCellStyle(Cell c) {
//        if (c.getStatus().equals("alive")) {
//            c.getShape().getStyleClass().add(ALIVE_STYLE);
//        } else {
//            c.getShape().getStyleClass().add(DEAD_STYLE);
//        }
//    }

//    @Override
//    public void setCellFromFile(int row, int col, char ch, Grid g) {
//        if (ch == '1') {
//            g.getCell(row, col).setStatus("alive");
//        }
//    }

//    @Override
//    public void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException {
//        if (g.getCell(row, col).getStatus().equals("alive")) {
//            fr.write(1 + ",");
//        }
//        else {
//            fr.write(0 + ",");
//        }
//    }
}