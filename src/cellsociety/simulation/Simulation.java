package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The purpose of this class is to hold the abstract methods to be implemented by the specific simulations.
 * The goal of a "simulation" is to update a simulation grid based on the specific rules of a simulation and
 * execute methods (such as getNeighbors) to assist with implementing the rules. A simulation also holds the
 * information needed to communicate with the model/view what color to represent a cell of a given state as in
 * the user interface. It is also capable of translating cell states to numbers in a CSV file representing
 * the configuration and vice versa.
 */
public abstract class Simulation {
    protected List<String> myStateReps;
    protected List<String> myStates;
    protected List<String> myStatesCSS;
    protected Grid mySimulationGrid;

    public Simulation(List<String> states, List<String> stateReps, List<String> stateCSS) {
        myStates = states;
        myStateReps = stateReps;
        myStatesCSS = stateCSS;
    }

    /**
     * Updates the cell configuration on the grid based on the rules of a certain simulation.
     * @return a Grid object with the updated cell states.
     */
    public abstract Grid updateCells();

    /**
     * Returns all the neighbors of a certain cell in the Grid.
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     */
    public abstract List<Cell> getNeighbors(int row, int col);


    /**
     * Given a Grid, translate the cell states into an integer and create the configuration file
     * where each line represents a row of cells (integers are chosen based on a cell's state index
     * in the list of all states specific to a simulation)
     * @param fr
     * @param row
     * @param col
     * @param g
     * @throws IOException
     */
    public void writeCellToFile (FileWriter fr, int row, int col, Grid g) throws IOException {
        for (int i = 0; i < myStates.size(); i++) {
            if (g.getCell(row,col).getStatus().equals(myStates.get(i))) {
                fr.write(Integer.parseInt(myStateReps.get(i))+",");
            }
        }
    }

    /**
     * Sets the cell to the desired state based on the values read in from a CSV file
     * @param row - the row that the cell is on
     * @param col - the column that the cell is on
     * @param ch - the character that corresponds to the value that cell will take
     * @param g - Grid that cell is being set in
     */
    public void setCellFromFile(int row, int col, char ch, Grid g) {
        for (int i = 0; i < myStateReps.size(); i++) {
            if (ch == myStateReps.get(i).charAt(0)){
                g.getCell(row, col).setStatus(myStates.get(i));
            }
        }
    }

    /**
     * Updates the APPEARANCE of a single cell based on is status
     * @param c - the cell to be updated
     */
    public void updateCellStyle(Cell c) {
        for (int i = 0; i < myStates.size(); i++) {
            if (c.getStatus().equals(myStates.get(i))) {
                c.getShape().getStyleClass().add(myStatesCSS.get(i));
            }
        }
    }

    public Grid getGrid() {return mySimulationGrid;};
    public void setGrid(Grid g) {mySimulationGrid = g;};
}
