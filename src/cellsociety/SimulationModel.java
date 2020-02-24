package cellsociety;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    public void readGridFromFile(String file) {
        ArrayList<String> initialStates = new ArrayList<>();
        Scanner input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(file));
        while (input.hasNext()) {
            initialStates.add(input.nextLine());
        }
        int num_rows = Integer.parseInt(initialStates.get(0).substring(0,1));
        int num_cols = Integer.parseInt(initialStates.get(0).substring(2));
        mySimulationGrid = new Grid(num_rows, num_cols);
        for (int row = 1; row <= num_rows; row++) {
            String rowConfig = initialStates.get(row);
            String cells = rowConfig.replaceAll("\\s", "");
            for (int col = 0; col < num_cols; col++) {
                char ch = cells.charAt(col);
                setCellFromFile(row, col, ch);
            }
        }
    }





}
