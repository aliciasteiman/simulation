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

    /**
     * Constructor for a Spreading of Fire simulation -- models the probability of trees catching on fire
     * when the probability of a tree catching on fire from a burning neighbor is predetermined
     * i.e. if probability of catch = 50%, due to humidity (for example), the fire is likely to spread rampantly
     * or die out quickly
     */
    public SpreadingOfFire(List<String> states, List<String> stateReps, List<String> stateCSS, double prob) {
        super(states, stateReps, stateCSS);
        probCatch = prob;
    }

    /**
     * Loops through the Cell objects in the Grid and updates the Cell according to the RULES:
     * If a cell is EMPTY, it remains empty
     * If a cell is BURNING, it becomes empty
     * If a cell is a TREE and at least one neighbor is BURNING and if a randomly generated number is greater
     * than or equal to probCatch (probability that a tree will catch on fire), it becomes BURNING
     * If the randomly generated number is less than probCatch, the cell remains a TREE
     * @return
     */
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

    /**
     * Gets 4 cardinal neighbors (north, south, east, west) aka (up, down, left, right)
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     * @return
     */
    @Override
    public List<Cell> getNeighbors(int row, int col) {
        myNeighbors = new ArrayList<>();
        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};

        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }
}
