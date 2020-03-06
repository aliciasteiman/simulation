package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WaTor extends Simulation {

    private Grid mySimulationGrid;
    private HashMap<Cell, String> updatedCellStatuses;
    private HashMap<Cell, Integer> fishReproductionCounts;
    private HashMap<Cell, Integer> sharkReproductionCounts;
    private HashMap<Cell, Integer> sharkEnergies;
    private int sharkEatingGain;
    private int sharkInitEnergy;
    private int fishRepTimer;
    private int sharkRepTimer;

    public WaTor(int fishTimer, int sharkTimer, int shEatingGain, int shInitEnergy) {
        fishRepTimer = fishTimer;
        sharkRepTimer = sharkTimer;
        sharkEatingGain = shEatingGain;
        fishReproductionCounts = new HashMap<>();
        sharkReproductionCounts = new HashMap<>();
        sharkEnergies = new HashMap<>();
        sharkInitEnergy = shInitEnergy;
        updatedCellStatuses = new HashMap<>();
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
                int numEmptyNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), "empty");
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals("fish")) {
                    fishReproductionCounts.put(currCell, fishReproductionCounts.get(currCell) + 1);
                    if (numEmptyNeighbors > 0) {
                        int currReproductionCount = fishReproductionCounts.get(currCell);
                        newCell = moveCellToNeighbor(i, j, "empty", currCell.getStatus());

                        if (currReproductionCount == fishRepTimer) {
                            fishReproductionCounts.put(currCell, 0);
                            fishReproductionCounts.put(newCell, 0);

                        } else {
                            vacateCell(updatedGrid, newCell, currCell, fishReproductionCounts, currReproductionCount);
                        }
                        updatedCellStatuses.put(currCell, currCell.getStatus());
                    }
                }
                if (currCell.getStatus().equals("shark")) {
                    sharkEnergies.put(currCell, sharkEnergies.get(currCell) - 1);
                    if (sharkEnergies.get(currCell) <= 0) {
                        // TODO could use vacateCell here instead
                        newCell = new Cell(i, j, "empty");
                        updatedCellStatuses.put(currCell, "empty");
                    }
                    else {
                        sharkReproductionCounts.put(currCell, sharkReproductionCounts.get(currCell) + 1);
                        int currReproductionCount = sharkReproductionCounts.get(currCell);
                        int numFishNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), "fish");
                        boolean reproduce = false;
                        if (numFishNeighbors > 0) {
                            newCell = moveCellToNeighbor(i, j, "fish", currCell.getStatus());
                            sharkEnergies.put(newCell, sharkEnergies.get(currCell) + sharkEatingGain);
                        } else if (numEmptyNeighbors > 0) {
                            newCell = moveCellToNeighbor(i, j, "empty", currCell.getStatus());
                            sharkEnergies.put(newCell, sharkEnergies.get(currCell));

                            if (currReproductionCount == sharkRepTimer) {
                                sharkReproductionCounts.put(currCell, 0);
                                sharkReproductionCounts.put(newCell, 0);
                                sharkEnergies.put(currCell, sharkInitEnergy);
                            } else {
                                sharkEnergies.remove(currCell);
                                vacateCell(updatedGrid, newCell, currCell, sharkReproductionCounts, currReproductionCount);
                                updatedCellStatuses.put(currCell, "empty");
                            }
                        }
                    }
                }
                updatedGrid.setCell(newCell.getRow(), newCell.getCol(), newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        updatedCellStatuses.clear();
        return mySimulationGrid;
    }
    /** If fish adjacent: move shark to random adjacent square with a fish
     Set status to shark
     Increment energy
     Else: if empty cell: move to random empty cell
     Deprive energy at time step; increase reproduction timer at time step
     If 0 energy, set cell to empty
     reproduction
     */

    private void vacateCell(Grid updatedGrid, Cell newCell, Cell currCell, HashMap<Cell, Integer> reproductionCounts, int currReproductionCount) {
        reproductionCounts.put(newCell, currReproductionCount);
        reproductionCounts.remove(currCell);
        currCell.setStatus("empty");
        updatedGrid.setCell(currCell.getRow(), currCell.getCol(), currCell);
    }

    private Cell moveCellToNeighbor(int i, int j, String neighborState, String myState) {
        Cell randEmptyNeighbor = getRandomNeighbor(getNeighborByStatus(getNeighbors(i, j), neighborState));
        return new Cell(randEmptyNeighbor.getRow(), randEmptyNeighbor.getCol(), myState);
    }

    private List<Cell> getNeighborByStatus(List<Cell> neighbors, String status) {
        List<Cell> statusNeighbors = new ArrayList<>();
        for (Cell n : neighbors) {
            String nStatus = n.getStatus();
            if (updatedCellStatuses.containsKey(n)) {
                nStatus = updatedCellStatuses.get(n);
            }
            if (nStatus.equals(status)) {
                statusNeighbors.add(n);
            }
        }
        return statusNeighbors;
    }

    private Cell getRandomNeighbor(List<Cell> neighbors) {
        Random rand = new Random();
        return neighbors.get(rand.nextInt(neighbors.size()));
    }


    @Override
    public List<Cell> getNeighbors(int row, int col) {

        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

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
}
