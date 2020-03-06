package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpreadingOfFire extends Simulation {
    private List<Cell> myNeighbors;
    private double probCatch;
    private final String EMPTY = myStates.get(0);
    private final String TREE = myStates.get(1);
    private final String BURNING = myStates.get(2);

    public SpreadingOfFire(List<String> states,double prob) {
        super(states);
        probCatch = prob;
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numBurningNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), BURNING);
                Random rand = new Random();
                double randNum = rand.nextDouble();
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals(TREE) && numBurningNeighbors >= 1 && randNum < probCatch) {
                    newCell = new Cell(i, j, BURNING);
                } else if (currCell.getStatus().equals(BURNING)) {
                    newCell = new Cell(i, j, EMPTY);
                }
                updatedGrid.setCell(newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return mySimulationGrid;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {
        myNeighbors = new ArrayList<>();
        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};

        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

}
