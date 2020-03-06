package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.HashMap;
import java.util.List;

public class RPS extends Simulation {
    private int threshold;
    private HashMap<String, String> relations;
    private final String ROCK;
    private final String PAPER;
    private final String SCISSORS;

    public RPS(List<String> states, List<String> stateReps, List<String> stateCSS, int thresh) {
        super(states, stateReps, stateCSS);
        ROCK = myStates.get(0);
        PAPER = myStates.get(1);
        SCISSORS = myStates.get(2);
        threshold = thresh;
        relations = new HashMap<>();
        relations.put(ROCK, PAPER);
        relations.put(PAPER, SCISSORS);
        relations.put(SCISSORS, ROCK);
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                String winningState = relations.get(currCell.getStatus());
                int numWinningNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), winningState);
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (numWinningNeighbors >= threshold) {
                    newCell = new Cell(i, j, winningState);
                }
                updatedGrid.setCell(newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return mySimulationGrid;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {

        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

}
