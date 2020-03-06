package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;

import java.io.FileWriter;
import java.io.IOException;
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

    private Map<Cell, Integer> allFish;
    private Map<Cell, List<Integer>> allSharks;

    public WaTor(List<String> states, List<String> stateReps, List<String> stateCSS, int fishTimer, int sharkTimer, int shEatingGain, int shInitEnergy) {
        super(states, stateReps, stateCSS);
        fishRepTimer = fishTimer;
        sharkRepTimer = sharkTimer;
        sharkEatingGain = shEatingGain;
        sharkInitEnergy = shInitEnergy;

        allFish = new HashMap<>();
        allSharks = new HashMap<>();

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
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals(FISH)) {
                    allFish.putIfAbsent(currCell, 0);
                    allFish.put(currCell, allFish.get(currCell) + 1);
                    if (allFish.get(currCell) == fishRepTimer && hasEmptyNeighbor(currCell)) {
                        newCell = moveCell(newCell, getOpenSpot());
                        resetRepCount(currCell);
                        updatedGrid.setCell(currCell);
                        updatedGrid.setCell(newCell);
                    }
                    else if (hasEmptyNeighbor(currCell)) {
                        newCell = moveCell(newCell, getOpenSpot());
                        updatedGrid.setCell(newCell);
                        currCell.setStatus(EMPTY);
                        updatedGrid.setCell(currCell);
                    }
                }
                if (currCell.getStatus().equals(SHARK)) {
                    allSharks.putIfAbsent(currCell, Arrays.asList(0, sharkInitEnergy));
                    allSharks.put(currCell, Arrays.asList(allSharks.get(currCell).get(0) + 1, allSharks.get(currCell).get(1) -1));
                    if (allSharks.get(currCell).get(1) == 0) {
                        newCell.setStatus(EMPTY);
                        updatedGrid.setCell(newCell);
                    }
                    else if (hasFishNeighbor(currCell)) {
                        newCell = moveCell(newCell, getOpenSpot());
                        allSharks.put(currCell, Arrays.asList(allSharks.get(currCell).get(0), allSharks.get(currCell).get(2) + sharkEatingGain));
                        updatedGrid.setCell(currCell);
                        updatedGrid.setCell(newCell);
                    }
                    else if (hasEmptyNeighbor() && allSharks.get(currCell).get(1) == sharkRepTimer) {
                        newCell = moveCell(currCell, getOpenSpot());
                        allSharks.put(currCell, Arrays.asList(0, allSharks.get(currCell).get(2)));
                        updatedGrid.setCell(currCell);
                        updatedGrid.setCell(newCell);
                    }
                    else if (hasEmptyNeighbor()) {
                        newCell = moveCell(currCell, getOpenSpot());
                        updatedGrid.setCell(newCell);
                        currCell.setStatus("empty");
                        updatedGrid.setCell(currCell);
                    }
                }
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

    /**
    @Override
    public void updateCellStyle(Cell c) {
        if (c.getStatus().equals("empty")) {
            c.getShape().getStyleClass().add("WAT-empty-cell");
        } else if (c.getStatus().equals("fish")) {
            c.getShape().getStyleClass().add("WAT-fish-cell");
        }
        else {
            c.getShape().getStyleClass().add("WAT-shark-cell");
        }
    }


    @Override
    public void setCellFromFile(int row, int col, char ch, Grid g ) {
        if (ch == '0') {
            g.getCell(row, col).setStatus("empty");
        }
        else if (ch == '1') {
            Cell currCell = g.getCell(row, col);
            currCell.setStatus("fish");
            fishReproductionCounts.put(currCell, 0);
        }
        else if (ch == '2') {
            Cell currCell = g.getCell(row, col);
            currCell.setStatus("shark");
            sharkReproductionCounts.put(currCell, 0);
            sharkEnergies.put(currCell, sharkInitEnergy);
        }
    }

    @Override
    public void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException {
        String currStatus = g.getCell(row, col).getStatus();
        if (currStatus.equals("empty")) {
            fr.write(0 + ",");
        }
        else if (currStatus.equals("fish")) {
            fr.write(1 + ",");
        } else {
            fr.write(2 + ",");
        }
    }
    */
}
