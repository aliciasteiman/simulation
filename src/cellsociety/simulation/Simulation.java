package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.Grid;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * This abstract class is responsible for maintaining all the information and performing
 * all the actions for a Simulation. It is abstract because each Simulation will apply
 * rules and calculate neighbors differently, but follows the same general pattern to set a
 * Cell from the file, write a Cell to a file, and update the Cell style. All simulations also
 * share the ability to communicate changes about their Grid objects and provide information
 * about their states.
 */
public abstract class Simulation {
    protected List<String> myStateReps; //how the states are represented in a file (ex. 0,1,2,etc)
    protected List<String> myStates; //the different forms a Cell can take (ex. Alive, Dead, etc.)
    protected List<String> myStatesCSS; //the CSS styling for each state (ex. GoL-Alive-Cell)
    protected Grid mySimulationGrid; //the Grid object that each type of simulation will operate on

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
     * @param fr - FileWriter to write to the CSV file.
     * @param row - current row
     * @param col - current col
     * @param g - Grid object that is being written to the file.
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
     * @return an UNMODIFIABLE list of states for the current Simulation.
     */
    public List<String> getMyStates() {
        return Collections.unmodifiableList(myStates);
    }

    /**
     * @return the current Grid object that the Simulation holds.
     */
    public Grid getGrid() {return mySimulationGrid;}

    /**
     * @param g - updated Grid object that the Simulation object will now act on.
     */
    public void setGrid(Grid g) {mySimulationGrid = g;};

}
