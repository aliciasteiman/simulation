package cellsociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Segregation extends SimulationModel {
    private List<Cell> myNeighbors;
    private List<String> openPos;

    private final double prob = Double.parseDouble(myResources.getString("SatisfiedThreshold"));

    public Segregation(String f) {
        super(f);
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
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        openPos = new ArrayList<>();
        findInitialEmpty();
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                int numNeighbors = getNeighbors(i,j).size();
                String currStatus = currCell.getStatus();
                int numSimilarNeighbors = mySimulationGrid.countAliveNeighbors(getNeighbors(i, j), currStatus);
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
