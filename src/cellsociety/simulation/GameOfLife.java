package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.List;

/**
 * Game of Life simulation -- models the life cycle of bacteria based on Conway's Game of Life
 * Each grid location is either empty (dead) or occupied by a single cell (alive)
 * A location's neighbors are any cells in the surrounding eight locations
 */
public class GameOfLife extends Simulation {
    private final String DEAD;
    private final String ALIVE;

    /**
     * Constructor for a GameOfLife simulation
     * @param states list of states the cells can take as dictated by its properties file
     * @param stateReps states as represented in the csv files (0,1)
     * @param stateCSS CSS style corresponding to each state
     */
    public GameOfLife(List<String> states, List<String> stateReps, List<String> stateCSS) {
        super(states, stateReps, stateCSS);
        DEAD = myStates.get(0);
        ALIVE = myStates.get(1);
    }

    /**
     * Loops through the Cell objects in the Grid and updates the Cell/Grid according to the RULES:
         * Any live cell with two or three neighbors survives
         * Any dead cell with three live neighbors comes back to life
         * All other live cells die, and all other dead cells stay dead
     * @return a new grid that contains the updated cells
     */
    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numLiveNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), ALIVE);
                boolean isAlive = ((currCell.getStatus().equals(ALIVE) && numLiveNeighbors == 2) || numLiveNeighbors == 3);
                Cell newCell = new Cell(i, j, DEAD);
                if (isAlive) {
                    newCell = new Cell(i, j, ALIVE);
                }
                updatedGrid.setCell(newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return updatedGrid;
    }

    /**
     * Creates a List<Cell> where each Cell represents the neighbors of the Cell at the passed in row, col
     * Checks for 8 adjacent neighbors (up, down, left, right, diagonals)
     * @param row - the row of the current cell
     * @param col - the column the current cell
     * @return the 8 Moore neighbors of the current cell
     */
    @Override
    public List<Cell> getNeighbors(int row, int col) {
        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};

        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);

    }

}