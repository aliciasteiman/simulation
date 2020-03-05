package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Segregation extends Simulation {

    private List<List<Integer>> openSpots;
    private Grid mySimulationGrid;
    private List<Cell> myNeighbors;
    private final double threshold = 0.3; //Double.parseDouble(myResources.getString("SatisfiedThreshold"));

    public Segregation() {
    }

    @Override
    public Grid getGrid() {
        return mySimulationGrid;
    }

    @Override
    public void setGrid(Grid g) {
        mySimulationGrid = g;
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        List<Cell> unsatisfiedCells = new ArrayList<>();
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                if (! currCell.getStatus().equals("empty") && ! isSatisfied(currCell)) {
                    unsatisfiedCells.add(currCell);
                }
            }
        }

        for (Cell c : unsatisfiedCells) {
            List<Integer> newSpot = findOpenSpot();
            moveCell(c, newSpot);
            openSpots.remove(newSpot);
            updatedGrid.setCell(c.getRow(), c.getCol(), new Cell(c.getRow(), c.getCol(), "empty"));
            updatedGrid.setCell(newSpot.get(0), newSpot.get(1), c);
        }

        mySimulationGrid = updatedGrid;
        return updatedGrid;
    }



    private boolean isSatisfied(Cell c) {
        int similarNeighbors = mySimulationGrid.countNeighbors(getNeighbors(c.getRow(), c.getCol()), c.getStatus());
        int emptyNeighbors = mySimulationGrid.countNeighbors(getNeighbors(c.getRow(), c.getCol()), "empty");
        int nonEmptyNeighbors = getNeighbors(c.getRow(), c.getCol()).size() - emptyNeighbors;
        if ((double) similarNeighbors/nonEmptyNeighbors >= threshold) {
            return true;
        }
        return false;
    }

    private List<Integer> findOpenSpot() {
        openSpots = new ArrayList<>();
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell c = mySimulationGrid.getCell(i, j);
                if (c.getStatus().equals("empty")) {
                    List<Integer> position = new ArrayList<>();
                    position.add(c.getRow());
                    position.add(c.getCol());
                    openSpots.add(position);
                }
            }
        }
        Random rand = new Random();
        int randPos = rand.nextInt(openSpots.size());
        return openSpots.get(randPos);
    }

    private Cell moveCell(Cell c, List<Integer> openSpot) {
        Cell movedCell = new Cell(openSpot.get(0), openSpot.get(1), c.getStatus());
        return movedCell;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {
        myNeighbors = new ArrayList<>();
        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};

        for (int i = 0; i < indexR.length; i++) {
            int currR = row + indexR[i];
            int currC = col + indexC[i];
            if (currR < mySimulationGrid.getRows() && currC < mySimulationGrid.getCols() && currR >= 0 && currC >= 0) {
                myNeighbors.add(mySimulationGrid.getCell(currR, currC));
            }
        }
        return myNeighbors;
    }

    @Override
    public void updateCellStyle(Cell c) {
        if (c.getStatus().equals("empty")) {
            c.getShape().getStyleClass().add("MOS-empty-cell");
        } else if (c.getStatus().equals("agent1")){
            c.getShape().getStyleClass().add("MOS-agent1-cell");
        } else {
            c.getShape().getStyleClass().add("MOS-agent2-cell");
        }
    }

    @Override
    public void setCellFromFile(int row, int col, char ch, Grid g) {
        if (ch == '0') {
            g.getCell(row, col).setStatus("empty");
        }
        if (ch == '1') {
            g.getCell(row, col).setStatus("agent1");
        }
        if (ch == '2') {
            g.getCell(row, col).setStatus("agent2");
        }
    }

    @Override
    public void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException {
        String currStatus = g.getCell(row, col).getStatus();
        if (currStatus.equals("empty")) {
            fr.write(0 + ",");
        } else if (currStatus.equals("agent1")) {
            fr.write(1 + ",");
        } else {
            fr.write(2 + ",");
        }
    }
}
