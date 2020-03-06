package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class WaTor extends Simulation {

    private Grid mySimulationGrid;
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
                        int reproductionCount = fishReproductionCounts.get(currCell);
                        Cell randEmptyNeighbor = getRandomNeighbor(getNeighborByStatus(getNeighbors(i, j), "empty"));
                        newCell = new Cell(randEmptyNeighbor.getRow(), randEmptyNeighbor.getCol(), "fish");
//                        updatedGrid.setCell(randEmptyNeighbor.getRow(), randEmptyNeighbor.getCol(), newCell);

                        if (reproductionCount == fishRepTimer) {
                            fishReproductionCounts.put(currCell, 0);
                            fishReproductionCounts.put(newCell, 0);

                        } else {
                            fishReproductionCounts.put(newCell, reproductionCount);
                            fishReproductionCounts.remove(currCell);
                            currCell.setStatus("empty");
                            updatedGrid.setCell(i, j, currCell);
                        }
                    }
                }
                if (currCell.getStatus().equals("shark")) {
                    sharkEnergies.put(currCell, sharkEnergies.get(currCell) - 1);
                    if (sharkEnergies.get(currCell) <= 0) {
                        newCell = new Cell(i, j, "empty");
                    }
                    else {
                        sharkReproductionCounts.put(currCell, sharkReproductionCounts.get(currCell) + 1);
                        int numFishNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), "fish");
                        boolean reproduce = false;
                        if (numFishNeighbors > 0) {
                            Cell randFishNeighbor = getRandomNeighbor(getNeighborByStatus(getNeighbors(i, j), "fish"));
                            newCell = new Cell(randFishNeighbor.getRow(), randFishNeighbor.getCol(), "shark");
                            sharkEnergies.put(newCell, sharkEnergies.get(currCell) + sharkEatingGain);
                        } else if (numEmptyNeighbors > 0) {
                            Cell randEmptyNeighbor = getRandomNeighbor(getNeighborByStatus(getNeighbors(i, j), "empty"));
                            newCell = new Cell(randEmptyNeighbor.getRow(), randEmptyNeighbor.getCol(), "shark");
                            sharkEnergies.put(newCell, sharkEnergies.get(currCell));

                            if (sharkReproductionCounts.get(currCell) == sharkRepTimer) {
                                sharkReproductionCounts.put(currCell, 0);
                                sharkReproductionCounts.put(newCell, 0);
                                sharkEnergies.put(currCell, sharkInitEnergy);
                                reproduce = true;
                            }
                        }
                        if (!reproduce) {
                            sharkReproductionCounts.put(newCell, sharkReproductionCounts.get(currCell));
                            sharkReproductionCounts.remove(currCell);
                            sharkEnergies.remove(currCell);
                            currCell.setStatus("empty");
                            updatedGrid.setCell(i, j, currCell);
                        }
                    }
                }
                updatedGrid.setCell(newCell.getRow(), newCell.getCol(), newCell);
            }
        }
        mySimulationGrid = updatedGrid;
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

    private List<Cell> getNeighborByStatus(List<Cell> neighbors, String status) {
        List<Cell> statusNeighbors = new ArrayList<>();
        for (Cell n : neighbors) {
            if (n.getStatus().equals(status)) {
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
