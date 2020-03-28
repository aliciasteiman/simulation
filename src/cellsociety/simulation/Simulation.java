package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class Simulation {
    protected List<String> myStateReps;
    protected List<String> myStates;
    protected List<String> myStatesCSS;
    protected Grid mySimulationGrid;

    /**
     * This abstract class provides a framework for all the various simulations to perform cell and grid updates. It shows
     * good design because it effectively uses abstraction to allow its subclasses control over their grids while providing
     * them with key functions and serving as the intermediary between them and SimulationModel, thus hiding exactly which
     * simulation is running after it has been set up. This design is important to note because it has evolved significantly
     * over the course of the project, gaining some of the functionality that originally belonged to SimulationModel but was
     * extracted here to make SimulationModel function as a separate sort of overarching controller without exposing all the
     * inner workings of its grid to SimulationModel.
     * @param states list of states the cells can take as dictated by its properties file
     * @param stateReps states as represented in the csv files (0,1)
     * @param stateCSS CSS style corresponding to each state
     */

    public Simulation(List<String> states, List<String> stateReps, List<String> stateCSS) {
        myStates = states;
        myStateReps = stateReps;
        myStatesCSS = stateCSS;
    }

    /**
     * Implemented by each simulation to updates the cell statuses in the grid based on that simulation's rules
     * @return a Grid object with the updated cell states.
     */
    public abstract Grid updateCells();

    /**
     * Returns all the neighbors of a certain cell in the Grid.
     * @param row - the current row the cell is on
     * @param col - the current column the cell is on
     * @return list of neighbors of the cell
     */
    public abstract List<Cell> getNeighbors(int row, int col);


    /**
     * Given a Grid, translate the cell states into an integer and create the configuration file
     * where each line represents a row of cells (integers are chosen based on a cell's state index
     * in the list of all states specific to a simulation)
     * @param fr Filewriter for file to be written to
     * @param row row of that cell
     * @param col column of that cell
     * @param g grid
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

    /**
     * @return the simulation's grid
     */
    public Grid getGrid() {return mySimulationGrid;};

    /**
     * Sets the simulation's grid to an inputted grid
     * @param g grid to set mySimulationGrid to
     */
    public void setGrid(Grid g) {mySimulationGrid = g;};
}