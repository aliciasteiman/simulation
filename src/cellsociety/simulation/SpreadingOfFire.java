package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SpreadingOfFire extends Simulation {

    private Grid mySimulationGrid;
    private List<Cell> myNeighbors;
    private double probCatch; // = 0.55; //Double.parseDouble(myResources.getString("ProbabilityCatch"));

    public SpreadingOfFire(double prob) {
        probCatch = prob;
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
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numBurningNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), "burning");
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

        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
    }

//    @Override
//    public void updateCellStyle(Cell c) {
//        if (c.getStatus().equals("empty")) {
//            c.getShape().getStyleClass().add("SOF-empty-cell");
//        } else if (c.getStatus().equals("tree")){
//            c.getShape().getStyleClass().add("SOF-tree-cell");
//        } else {
//            c.getShape().getStyleClass().add("SOF-burning-cell");
//        }
//    }


//    @Override
//    public void setCellFromFile(int row, int col, char ch, Grid g) {
//        if (ch == '0') {
//            g.getCell(row, col).setStatus("empty");
//        }
//        if (ch == '1') {
//            g.getCell(row, col).setStatus("tree");
//        }
//        if (ch == '2') {
//            g.getCell(row, col).setStatus("burning");
//        }
//    }

//    @Override
//    public void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException {
//        String currStatus = g.getCell(row, col).getStatus();
//        if (currStatus.equals("empty")) {
//            fr.write(0 + ",");
//        }
//        else if (currStatus.equals("tree")) {
//            fr.write(1 + ",");
//        } else {
//            fr.write(2 + ",");
//        }
//    }
}
