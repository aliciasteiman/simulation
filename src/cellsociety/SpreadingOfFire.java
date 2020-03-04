package cellsociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpreadingOfFire extends SimulationModel {

    private List<Cell> myNeighbors;
    private final double probCatch = Double.parseDouble(myResources.getString("ProbabilityCatch"));

    public SpreadingOfFire(String f) {
        super(f);
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numBurningNeighbors = mySimulationGrid.countAliveNeighbors(getNeighbors(i, j), "burning");
                Random rand = new Random();
                double randNum = rand.nextDouble();
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals("tree") && numBurningNeighbors >= 1 && randNum < probCatch) {
                    newCell = new Cell(i, j, "burning");
                } else if (currCell.getStatus().equals("burning")) {
                    newCell = new Cell(i, j, "empty");
                }
                updatedGrid.setCell(i, j, newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return mySimulationGrid;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {
        myNeighbors = new ArrayList<>();
        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};

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
            c.getShape().getStyleClass().add("SOF-empty-cell");
        } else if (c.getStatus().equals("tree")){
            c.getShape().getStyleClass().add("SOF-tree-cell");
        } else {
            c.getShape().getStyleClass().add("SOF-burning-cell");
        }
    }

    @Override
    public void setCellFromFile(int row, int col, char ch) {
        if (ch == '0') {
            mySimulationGrid.getCell(row, col).setStatus("empty");
        }
        if (ch == '1') {
            mySimulationGrid.getCell(row, col).setStatus("tree");
        }
        if (ch == '2') {
            mySimulationGrid.getCell(row, col).setStatus("burning");
        }
    }
}
