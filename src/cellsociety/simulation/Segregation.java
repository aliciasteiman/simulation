package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.util.*;


public class Segregation extends Simulation {
    private List<List<Integer>> openSpots;
    private List<Cell> myNeighbors;

    private final double threshold;
    private final String EMPTY;
    private final String AGENT1;
    private final String AGENT2;


    public Segregation(List<String> states,double thresh) {
        super(states);
        EMPTY = myStates.get(0);
        AGENT1 = myStates.get(1);
        AGENT2 = myStates.get(2);
        threshold = thresh;
    }

    @Override
    public Grid updateCells() {
        Grid updatedGrid = new Grid(mySimulationGrid.getRows(), mySimulationGrid.getCols());
        openSpots = findOpenSpots(mySimulationGrid);
        for (int i = 0; i < mySimulationGrid.getRows(); i++) {
            for (int j = 0; j < mySimulationGrid.getCols(); j++) {
                Cell currCell = mySimulationGrid.getCell(i, j);
                Cell newCell = new Cell(i, j, currCell.getStatus());
                if (! currCell.getStatus().equals(EMPTY) && ! isSatisfied(currCell)) {
                    newCell = moveCell(newCell, getOpenSpot(openSpots));
                    updatedGrid.setCell(newCell);
                    currCell.setStatus(EMPTY);
                    updatedGrid.setCell(currCell);
                }
                else if (! currCell.getStatus().equals(EMPTY) && isSatisfied(currCell)) {
                    updatedGrid.setCell(newCell);
                }
                else if (currCell.getStatus().equals(EMPTY) && openSpots.contains(Arrays.asList(currCell.getRow(), currCell.getCol()))) {
                    updatedGrid.setCell(newCell);
                }
                else {
                    continue;
                }
            }
        }
        mySimulationGrid = updatedGrid;
        return updatedGrid;
    }

    private boolean isSatisfied(Cell c) {
        int similarNeighbors = mySimulationGrid.countNeighbors(getNeighbors(c.getRow(), c.getCol()), c.getStatus());
        int emptyNeighbors = mySimulationGrid.countNeighbors(getNeighbors(c.getRow(), c.getCol()), EMPTY);
        int nonEmptyNeighbors = getNeighbors(c.getRow(), c.getCol()).size() - emptyNeighbors;
        if ((double) similarNeighbors/nonEmptyNeighbors >= threshold) {
            return true;
        }
        return false;
    }

    private List<List<Integer>> findOpenSpots(Grid g) {
        List<List<Integer>> open = new ArrayList<>();
        for (int i = 0; i < g.getRows(); i++) {
            for (int j = 0; j < g.getCols(); j++) {
                Cell c = g.getCell(i, j);
                if (c.getStatus().equals(EMPTY)) {
                    List<Integer> position = new ArrayList<>();
                    position.add(c.getRow());
                    position.add(c.getCol());
                    open.add(position);
                }
            }
        }
        return open;
    }

    private List<Integer> getOpenSpot(List<List<Integer>> open) {
        //findOpenSpot();
        Random rand = new Random();
        int randPos = rand.nextInt(open.size());
        return open.get(randPos);
    }

    private Cell moveCell(Cell c, List<Integer> openSpot) {
        openSpots.remove(openSpot);
        List<Integer> position = new ArrayList<>();
        position.add(c.getRow());
        position.add(c.getCol());
        openSpots.add(position);
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

}
