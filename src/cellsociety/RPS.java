package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RPS extends Simulation {
    private final int threshold = 3;
    private Grid mySimulationGrid;
    private HashMap<String, String> relations;

    public RPS() {
        relations = new HashMap<>();
        relations.put("rock", "paper");
        relations.put("paper", "scissors");
        relations.put("scissors", "rock");
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
                String winningState = relations.get(currCell.getStatus());
                int numWinningNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), winningState);
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (numWinningNeighbors >= threshold) {
                    newCell = new Cell(i, j, winningState);
                }
                updatedGrid.setCell(i, j, newCell);
            }
        }
        mySimulationGrid = updatedGrid;
        return mySimulationGrid;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {

        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

    @Override
    public void updateCellStyle(Cell c) {
        if (c.getStatus().equals("rock")) {
            c.getShape().getStyleClass().add("RPS-rock-cell");
        } else if (c.getStatus().equals("paper")) {
            c.getShape().getStyleClass().add("RPS-paper-cell");
        }
        else {
            c.getShape().getStyleClass().add("RPS-scissors-cell");
        }
    }


    @Override
    public void setCellFromFile(int row, int col, char ch, Grid g ) {
        if (ch == '0') {
            g.getCell(row, col).setStatus("rock");
        }
        if (ch == '1') {
            g.getCell(row, col).setStatus("paper");
        }
        if (ch == '2') {
            g.getCell(row, col).setStatus("scissors");
        }
    }

    @Override
    public void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException {
        String currStatus = g.getCell(row, col).getStatus();
        if (currStatus.equals("rock")) {
            fr.write(0 + ",");
        }
        else if (currStatus.equals("paper")) {
            fr.write(1 + ",");
        } else {
            fr.write(2 + ",");
        }
    }
}
