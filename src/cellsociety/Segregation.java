package cellsociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Segregation extends SimulationModel {
    private List<Cell> myNeighbors;
    private List<String> openPos;

    private final double threshold = Double.parseDouble(myResources.getString("SatisfiedThreshold"));

    public Segregation(String f) {
        super(f);
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (! isSatisfied(currCell)) {
                    newCell = moveCell(currCell, findOpenSpot());
                }
                updatedGrid.setCell(newCell.getRow(), newCell.getCol(), newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return updatedGrid;
    }

    private boolean isSatisfied(Cell c) {
        int similarNeighbors = mySimulationGrid.countAliveNeighbors(getNeighbors(c.getRow(), c.getCol()), c.getStatus());
        if ((double) similarNeighbors/myNeighbors.size() >= threshold) {
            return true;
        }
        return false;
    }

    private Cell moveCell(Cell c, List<Integer> openSpot) {
        Cell movedCell = new Cell(openSpot.get(0), openSpot.get(1), c.getStatus());
        return movedCell;
    }

    private List<Integer> findOpenSpot() {
        List<List<Integer>> openSpots = new ArrayList<>();
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
        if (openSpots.size() > 0) {
            Random rand = new Random();
            int randPos = rand.nextInt(openSpots.size());
            return openSpots.get(randPos);
        }
        return null;
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
    public void setCellFromFile(int row, int col, char ch) {
        if (ch == '0') {
            mySimulationGrid.getCell(row, col).setStatus("empty");
        }
        if (ch == '1') {
            mySimulationGrid.getCell(row, col).setStatus("agent1");
        }
        if (ch == '2') {
            mySimulationGrid.getCell(row, col).setStatus("agent2");
        }

    }
}
