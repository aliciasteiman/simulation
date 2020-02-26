package cellsociety;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the functionality of the simulation (each type of simulation extends this class)
 * Reads in an initial configuration for a simulation and specifies how to update cells/grid
 */
public abstract class SimulationModel {
    protected Grid mySimulationGrid;

    /**
     * Constructor for the abstract class SimulationModel.
     * It will have subclasses as all the kinds of simulations.
     * Upon creation, it creates an INITIAL grid configuration based on a file.
     * @param file - CSV file with starting grid configuration
     */
    public SimulationModel(String file) {
        readGridFromFile(file);

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
     * Updates the APPEARANCE of a single cell based on is status
     * @param c - the cell to be updated
     */
    public abstract void updateCellStyle(Cell c);

    /**
     * Sets the cell to the desired state based on the values read in from a CSV file
     * @param row - the row that the cell is on
     * @param col - the column that the cell is on
     * @param ch - the character that corresponds to the value that cell will take
     */
    public abstract void setCellFromFile(int row, int col, char ch);

    /**
     * Returns the Grid object that represents the current state of the simulation
     */
    public Grid getMySimulationGrid() {
        return mySimulationGrid;
    }

    /**
     * Initializes a grid based on what's read in from a file
     * @param file - CSV file that contains initial states for the grid
     */
    private void readGridFromFile(String file) {
        Scanner input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(file));
        String[] header = input.nextLine().split(",");
        int num_rows = Integer.parseInt(header[0]);
        int num_cols = Integer.parseInt(header[1]);
        mySimulationGrid = new Grid(num_rows, num_cols);
        int row = 0;
        while (input.hasNextLine()) {
            String rowConfig = input.nextLine();
            String cells = rowConfig.replaceAll(",", "");
            for (int col = 0; col < num_cols; col++) {
                char ch = cells.charAt(col);
                setCellFromFile(row, col, ch);
            }
            row++;
        }
    }

    /**
     * When user hits save button, this method is called
     * Translates the current grid (upon hitting save) into a .csv file that the user can later access
     * @param grid
     */
    public File saveCurrentConfig(Grid grid) {
        File file = new File("data/new.csv");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(mySimulationGrid.getRows() + "," + mySimulationGrid.getCols() + "\n");
            for (List<Cell> lst : grid.getAllCells()) {
                for (Cell c : lst) {
                    if (c.getStatus()) {
                        fr.write(1 + ",");
                    }
                    else {
                        fr.write(0 + ",");
                    }
                }
                fr.write("\n");
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace(); //need to change this... look at nanobrowser example
        }
        return file;
    }




}
