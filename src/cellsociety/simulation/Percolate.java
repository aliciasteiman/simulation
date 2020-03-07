package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.List;

public class Percolate extends Simulation {
    private final String BLOCKED;
    private final String OPEN;
    private final String FULL;

    /**
     * Constructor for a Percolate simulation -- models water flowing through a system
     * Cells can either be open, full (of water), or blocked (meaning water cannot go through them)
     * @param states
     * @param stateReps
     * @param stateCSS
     */
    public Percolate(List<String> states,List<String> stateReps, List<String> stateCSS) {
        super(states, stateReps, stateCSS);
        BLOCKED = myStates.get(0);
        OPEN = myStates.get(1);
        FULL = myStates.get(2);
    }

    /**
     * Loops through the Cell objects in the Grid and updates the Cell/Grid according to the RULES:
     * If a Cell is empty and has a full neighbor, Cell becomes full
     * Water percolates through the system when there is a path of water (full cells) from top to bottom
     * @return
     */
    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numFullNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), FULL);
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals(OPEN) && numFullNeighbors >= 1) {
                    newCell = new Cell(i, j, FULL);
                }
                updatedGrid.setCell(newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return mySimulationGrid;
    }

    /**
     * Gets the four cardinal neighbors (north, south, east, west) of a Cell given its row, col
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     * @return
     */
    @Override
    public List<Cell> getNeighbors(int row, int col) {
        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

}
