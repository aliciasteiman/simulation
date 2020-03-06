package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.List;

public class Percolate extends Simulation {
    private final String BLOCKED;
    private final String OPEN;
    private final String FULL;

    public Percolate(List<String> states) {
        super(states);
        BLOCKED = myStates.get(0);
        OPEN = myStates.get(1);
        FULL = myStates.get(2);
    }

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

    @Override
    public List<Cell> getNeighbors(int row, int col) {

        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

}
