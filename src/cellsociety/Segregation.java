package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Segregation extends Simulation {
    private Grid mySimulationGrid;
    private List<String> openPos;

    private final double prob = 0.50; //Double.parseDouble(myResources.getString("SatisfiedThreshold"));

    public Segregation() {

    }

    public String findRandEmptySpot() {
        if (openPos.size()>0) {
            Random rand = new Random();
            int randIndex = rand.nextInt(openPos.size());
            return openPos.get(randIndex);
        }
        return "";
    }

    public void findInitialEmpty() {
        for (int r = 0; r < mySimulationGrid.getRows(); r++) {
            for (int c = 0; c < mySimulationGrid.getCols(); c++) {
                if (mySimulationGrid.getCell(r,c).getStatus().equals("empty")) {
                    String pos = Integer.toString(r) + "," + Integer.toString(c);
                    openPos.add(pos);
                }
            }
        }
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
        openPos = new ArrayList<>();
        findInitialEmpty();
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numNeighbors = getNeighbors(i,j).size();
                String currStatus = currCell.getStatus();
                int numSimilarNeighbors = mySimulationGrid.countNeighbors(getNeighbors(i, j), currStatus);
                double percent = numSimilarNeighbors/numNeighbors;
                String newSpot = findRandEmptySpot();
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (! currStatus.equals("empty") && percent < prob && !newSpot.equals("")) {
                    int row = Integer.parseInt(newSpot.split(",")[0]);
                    int col = Integer.parseInt(newSpot.split(",")[1]);
                    updatedGrid.setCell(row, col, newCell);
                    openPos.add(Integer.toString(i)+","+Integer.toString(j));
                    int rem = openPos.indexOf(Integer.toString(row)+","+Integer.toString(col));
                    openPos.remove(rem);

                }
            }
        }
        mySimulationGrid = updatedGrid;
        return updatedGrid;
    }

    @Override
    public List<Cell> getNeighbors(int row, int col) {

        int[] indexR = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] indexC = {1, 1, 1, 0, 0, -1, -1, -1};

        return mySimulationGrid.getSpecifiedNeighbors(row, col, indexR, indexC, mySimulationGrid);
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
        }
        else if (currStatus.equals("agent1")) {
            fr.write(1 + ",");
        } else {
            fr.write(2 + ",");
        }
    }
}
