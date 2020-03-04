package cellsociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Percolate extends SimulationModel {

    private List<Cell> myNeighbors;

    public Percolate(String f) {
        super(f);
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numFullNeighbors = mySimulationGrid.countAliveNeighbors(getNeighbors(i, j), "full");
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals("empty") && numFullNeighbors >= 1) {
                    newCell = new Cell(i, j, "full");
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
        if (c.getStatus().equals("blocked")) {
            c.getShape().getStyleClass().add("PERC-blocked-cell");
        } else if (c.getStatus().equals("open")) {
            c.getShape().getStyleClass().add("PERC-open-cell");
        }
        else {
            c.getShape().getStyleClass().add("PERC-full-cell");
        }
    }

    @Override
    public void setCellFromFile(int row, int col, char ch) {
        if (ch == '0') {
            mySimulationGrid.getCell(row, col).setStatus("blocked");
        }
        if (ch == '1') {
            mySimulationGrid.getCell(row, col).setStatus("open");
        }
        if (ch == '2') {
            mySimulationGrid.getCell(row, col).setStatus("full");
        }
    }
}
