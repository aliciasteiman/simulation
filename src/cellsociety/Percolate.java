package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Percolate extends Simulation {

    private Grid mySimulationGrid;
    public Percolate() {

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
                int numFullNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), "full");
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (currCell.getStatus().equals("open") && numFullNeighbors >= 1) {
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

        int[] indexR = {1, -1, 0, 0};
        int[] indexC = {0, 0, 1, -1};
        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

//    @Override
//    public void updateCellStyle(Cell c) {
//        if (c.getStatus().equals("blocked")) {
//            c.getShape().getStyleClass().add("PERC-blocked-cell");
//        } else if (c.getStatus().equals("open")) {
//            c.getShape().getStyleClass().add("PERC-open-cell");
//        }
//        else {
//            c.getShape().getStyleClass().add("PERC-full-cell");
//        }
//    }


//    @Override
//    public void setCellFromFile(int row, int col, char ch, Grid g ) {
//        if (ch == '0') {
//            g.getCell(row, col).setStatus("blocked");
//        }
//        if (ch == '1') {
//            g.getCell(row, col).setStatus("open");
//        }
//        if (ch == '2') {
//            g.getCell(row, col).setStatus("full");
//        }
//    }

//    @Override
//    public void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException {
//        String currStatus = g.getCell(row, col).getStatus();
//        if (currStatus.equals("blocked")) {
//            fr.write(0 + ",");
//        }
//        else if (currStatus.equals("open")) {
//            fr.write(1 + ",");
//        } else {
//            fr.write(2 + ",");
//        }
//    }
}
