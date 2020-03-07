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

    @Override
    public Grid getGrid() { return mySimulationGrid; }

    @Override
    public void setGrid(Grid g) {
        mySimulationGrid = g;
    }

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
        if (statusNeighbors > 0) {
            return true;
        }
        return false;
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

    @Override
    public List<Cell> getNeighbors(int row, int col) {
        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }
}
