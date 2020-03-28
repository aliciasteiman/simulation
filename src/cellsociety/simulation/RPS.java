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

    /**
     * Constructor for a Rock Paper Scissors simulation where each Cell object is a "player" who is either
     * a Rock, Paper, or Scissors
     * @param states list of states the cells can take as dictated by its properties file
     * @param stateReps states as represented in the csv files (0,1,2)
     * @param stateCSS CSS style corresponding to each state
     * @param thresh threshold that dictates how many of a cell's neighbors need to be of its opposing state
     *               for it to lose
     */
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

    /**
     * Loops through the Cell objects in the Grid and updates the Cell/Grid according to the RULES:
         * Current cell goes against its 8 neighbors
         * If the neighbor count of cells that can beat currCell is greater than than a threshold value, then the
         * current cell becomes the winner (i.e. the type that defeated it)
     * E.g. if currCell = scissors, threshold = 3, 4 rocks surround currCell, currCell -> rock
     * @return a new grid that contains the updated cells
     */
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

    /**
     * Gets the 8 Moore neighbors of the current cell
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

}
