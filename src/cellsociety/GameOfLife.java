package cellsociety;

import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends SimulationModel {

    private List<Cell> myNeighbors;

    public GameOfLife(String file) {
        super(file);
    }

    @Override
    public Grid updateCells() {
        List<List<Cell>> updatedGrid = new ArrayList<>();
        for (int r = 0; r < mySimulationGrid.getRows(); r++) {
            List<Cell> updatedRow = new ArrayList<>();
            for (int c = 0; c < mySimulationGrid.getCols(); c++) {
                Cell thisCell = mySimulationGrid.getCell(r, c);
                int numLiveNeighbors = mySimulationGrid.countAliveNeighbors(getNeighbors(r,c));
                boolean isAlive = ((thisCell.getStatus() && numLiveNeighbors == 2) || numLiveNeighbors == 3);
                updatedRow.add(new Cell(isAlive));
            }
            updatedGrid.add(updatedRow);
        }
        mySimulationGrid.updateAllCells(updatedGrid);
        return mySimulationGrid;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {
        myNeighbors = new ArrayList<>();
        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};

        for (int i = 0; i < 8; i++) {
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
        if (c.getStatus()) {
            c.getShape().getStyleClass().add("alive-cell");
        } else {
            c.getShape().getStyleClass().add("dead-cell");
        }
    }

    @Override
    public void setCellFromFile(int row, int col, char ch) {
        if (ch == '1') {
            mySimulationGrid.getCell(row, col).setStatus(true);
        }
    }
}