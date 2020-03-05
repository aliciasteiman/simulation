package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class Simulation {
    protected List<String> stateReps;
    protected List<String> states;
    protected List<String> statesCSS;

    public abstract Grid getGrid();
    public abstract void setGrid(Grid g);
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
    public abstract void setCellFromFile(int row, int col, char ch, Grid g);

    public abstract void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException;

    /*
        CAN REPLACE abstract setCellFromFile with this
     */
    public void settingCellFromFile(int row, int col, char ch, Grid g) {
        for (int i = 0; i < stateReps.size(); i++) {
            if (ch == (char) i){
                g.getCell(row, col).setStatus(states.get(i));
            }
        }
    }


}
