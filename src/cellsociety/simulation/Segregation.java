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


    /**
     * Constructor for a Schelling's Model of Segregation -- shows how agents who differ choose to segregate
     * themselves over time, even if they "didn't mind" being surrounded by agents of another type
     * @param states
     * @param stateReps
     * @param stateCSS
     * @param thresh -- represents the willingness of an agent to be surrounded by agents of another type
     */
    public Segregation(List<String> states, List<String> stateReps, List<String> stateCSS, double thresh) {
        super(states, stateReps, stateCSS);
        EMPTY = myStates.get(0);
        AGENT1 = myStates.get(1);
        AGENT2 = myStates.get(2);
        threshold = thresh;
    }

    /**
     * Loops through the Cell objects in the Grid and updates the Cell according to the RULES:
     * If a Cell is not satisfied (see isSatisfied() method), the Cell will randomly move to an open spot
     * If a Cell is satisfied, it will remain where it is
     * @return
     */
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

    /**
     * Determines if a cell is "satisfied" i.e. if the number of similar neighbors (same status) surrounding
     * a cell divided by the total number of NON-EMPTY cells is greater than the threshold
     * @param c
     * @return
     */
    private boolean isSatisfied(Cell c) {
        int similarNeighbors = mySimulationGrid.countNeighbors(getNeighbors(c.getRow(), c.getCol()), c.getStatus());
        int emptyNeighbors = mySimulationGrid.countNeighbors(getNeighbors(c.getRow(), c.getCol()), EMPTY);
        int nonEmptyNeighbors = getNeighbors(c.getRow(), c.getCol()).size() - emptyNeighbors;
        if ((double) similarNeighbors/nonEmptyNeighbors >= threshold) {
            return true;
        }
        return false;
    }

    /**
     * Given a grid, counts the number of cells who are EMPTY
     * These spots represent the open spots that an unsatisfied cell can move to
     * Method is called at the start of each call to update()
     * @param g
     * @return a List of Lists where each element is a list with a cell's row index as the first element and a cell's
     * column index as the second element
     */
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

    /**
     * Given a list of open spots, randomly selects an open spot
     * Used to determine which spot an unsatisfied cell will move to
     * @param open
     * @return
     */
    private List<Integer> getOpenSpot(List<List<Integer>> open) {
        Random rand = new Random();
        int randPos = rand.nextInt(open.size());
        return open.get(randPos);
    }

    /**
     * Given a cell and an open spot, create/return a new Cell object at that open spot with the same status
     * as the cell passed in; removes the open spot from the list of open spots in a Grid and adds the cell's
     * old position to the list
     * @param c
     * @param openSpot
     * @return
     */
    private Cell moveCell(Cell c, List<Integer> openSpot) {
        openSpots.remove(openSpot);
        List<Integer> position = new ArrayList<>();
        position.add(c.getRow());
        position.add(c.getCol());
        openSpots.add(position);
        Cell movedCell = new Cell(openSpot.get(0), openSpot.get(1), c.getStatus());
        return movedCell;
    }

    /**
     * Gets the 8 neighbors of a Cell (up, down, left, right, and all four diagonals)
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     * @return
     */
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
