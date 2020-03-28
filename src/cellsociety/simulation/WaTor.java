package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;

import java.util.*;

public class WaTor extends Simulation {

    private Grid mySimulationGrid;

    private int sharkEatingGain;
    private int sharkInitEnergy;
    private int fishRepTimer;
    private int sharkRepTimer;

    private final String FISH;
    private final String SHARK;
    private final String EMPTY;

    private List<List<Integer>> movedCells;

    /**
     * @param states list of states the cells can take as dictated by its properties file
     * @param stateReps states as represented in the csv files (0,1,2)
     * @param stateCSS CSS style corresponding to each state
     * @param fishTimer timer that counts down time until a fish can reproduce
     * @param sharkTimer timer that counts down time until a shark can reproduce
     * @param shEatingGain energy the shark will gain when it eats a fish, as dictated by WaTor properties file
     * @param shInitEnergy initial energy of all shark cells before any time steps
     */
    public WaTor(List<String> states, List<String> stateReps, List<String> stateCSS, int fishTimer, int sharkTimer, int shEatingGain, int shInitEnergy) {
        super(states, stateReps, stateCSS);
        fishRepTimer = fishTimer;
        sharkRepTimer = sharkTimer;
        sharkEatingGain = shEatingGain;
        sharkInitEnergy = shInitEnergy;

        movedCells = new ArrayList<>();

        EMPTY = myStates.get(0);
        FISH = myStates.get(1);
        SHARK = myStates.get(2);
    }

    // is kind-of a mess, needs to be refactored/fixed before the next deadline
    // logic is there, implementation is off

    /**
     * Updates all the cells in the grid based on the RULES of WaTor World:
     * a fish:
         * if it has empty neighbors, it can move to a random empty neighbor
         * if it has survived enough time units to complete its reproduction timer and has an empty neighbor, it can reproduce
     * a shark:
        * if it has fish neighbors, it can eat a random fish neighbor and gains sharkEatingGain
        * if not, it can move to a random empty neighbor
        * if it has survived enough time units to complete its reproduction timer and has an empty neighbor, it can reproduce
        * loses a unit of energy for each time step; if its energy reaches 0, it does
     * @return a new grid that contains the updated cells
     */
    @Override
    public Grid updateCells() { //need to add newCell to the allFish and allShark
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                if (currCell.getStatus().equals(FISH)) {
                    //increase rep count
                    currCell.setRepCount(currCell.getRepCount() + 1);
                    if (currCell.getRepCount() == fishRepTimer && hasStatusNeighbor(currCell, EMPTY)) {
                        //keep fish cell in current spot and reproduce in an empty spot
                        currCell = moveCell(currCell, getSpot(getNeighbors(currCell.getRow(), currCell.getCol()), EMPTY));
                        movedCells.add(Arrays.asList(currCell.getRow(), currCell.getCol()));
                        currCell.setRepCount(0);
                        updatedGrid.setCell(currCell);
                    }
                    else if (hasStatusNeighbor(currCell, EMPTY)) {
                        //move fish cell to an empty cell and set its old spot to empty
                        Cell empty = new Cell(currCell.getRow(), currCell.getCol(), EMPTY);
                        currCell = moveCell(currCell, getSpot(getNeighbors(currCell.getRow(), currCell.getCol()), EMPTY));
                        movedCells.add(Arrays.asList(currCell.getRow(), currCell.getCol()));
                        updatedGrid.setCell(currCell);
                        updatedGrid.setCell(empty);
                    }
                }
                if (currCell.getStatus().equals(SHARK)) {
                    //increase rep count, decrease energy
                    currCell.setRepCount(currCell.getRepCount() + 1);
                    currCell.setEnergy(currCell.getEnergy() - 1);
                    if (currCell.getEnergy() == 0) {
                        //if energy = 0, shark dies, set cell to empty
                        currCell.setStatus(EMPTY);
                        updatedGrid.setCell(currCell);
                    }
                    else if (hasStatusNeighbor(currCell, FISH)) {
                        //move shark cell to fish cell, set old cell empty, and increase energy
                        Cell empty = new Cell(currCell.getRow(), currCell.getCol(), EMPTY);
                        currCell = moveCell(currCell, getSpot(getNeighbors(currCell.getRow(), currCell.getCol()), FISH));
                        movedCells.add(Arrays.asList(currCell.getRow(), currCell.getCol()));
                        currCell.setEnergy(currCell.getEnergy() + 1);
                        updatedGrid.setCell(currCell);
                        updatedGrid.setCell(empty);
                    }
                    else if (hasStatusNeighbor(currCell, EMPTY) && currCell.getRepCount() == sharkRepTimer) {
                        //keep shark cell in current spot and reproduce shark in an empty neighbor
                        currCell = moveCell(currCell, getSpot(getNeighbors(currCell.getRow(), currCell.getCol()), EMPTY));
                        movedCells.add(Arrays.asList(currCell.getRow(), currCell.getCol()));
                        currCell.setRepCount(0);
                        updatedGrid.setCell(currCell);
                    }
                    else if (hasStatusNeighbor(currCell, EMPTY)) {
                        //move shark cell to empty cell, set old cell empty
                        Cell empty = new Cell(currCell.getRow(), currCell.getCol(), EMPTY);
                        currCell = moveCell(currCell, getSpot(getNeighbors(currCell.getRow(), currCell.getCol()), EMPTY));
                        movedCells.add(Arrays.asList(currCell.getRow(), currCell.getCol()));
                        currCell.setStatus("empty");
                        updatedGrid.setCell(empty);
                        updatedGrid.setCell(currCell);
                    }
                }
                else {
                    updatedGrid.setCell(currCell);
                }
            }
        }
        mySimulationGrid = updatedGrid;
        return mySimulationGrid;
    }

    private boolean hasStatusNeighbor(Cell currCell, String status) {
        int statusNeighbors = mySimulationGrid.countNeighbors(getNeighbors(currCell.getRow(), currCell.getCol()), status);
        return statusNeighbors > 0;
    }

    private List<Integer> getSpot(List<Cell> openNeighbors, String status) { //needs to account for things moving
        List<Cell> statusNeighbors = new ArrayList<>();
        for (Cell neighbor : openNeighbors) {
            if (neighbor.getStatus().equals(status) && ! movedCells.contains(Arrays.asList(neighbor.getRow(), neighbor.getCol()))) {
                statusNeighbors.add(neighbor);
            }
        }
        Random rand = new Random();
        int randPos = rand.nextInt(statusNeighbors.size());
        Cell randNeighbor = statusNeighbors.get(randPos);
        return Arrays.asList(randNeighbor.getRow(), randNeighbor.getCol());
    }

    private Cell moveCell(Cell c, List<Integer> spot) {
        Cell movedCell = new Cell(spot.get(0), spot.get(1), c.getStatus());
        return movedCell;
    }

    /**
     * Gets the four cardinal neighbors (north, south, east, west) of a Cell given its row, col
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     * @return the four cardinal neighbors of the cell
     */
    @Override
    public List<Cell> getNeighbors(int row, int col) {
        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }
}
