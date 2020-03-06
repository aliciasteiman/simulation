package cellsociety;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class Simulation {
    protected List<String> stateReps;
    protected List<String> states;
    protected List<String> statesCSS;

    public List<String> getStates() {
        return Collections.unmodifiableList(states);
    }

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


    //public abstract void updateCellStyle(Cell c);

    //public abstract void setCellFromFile(int row, int col, char ch, Grid g);

    //public abstract void writeCellToFile(FileWriter fr, int row, int col, Grid g) throws IOException;

    public void writeCellToFile (FileWriter fr, int row, int col, Grid g) throws IOException {
        for (int i = 0; i < states.size();i++) {
            if (g.getCell(row,col).getStatus().equals(states.get(i))) {
                fr.write(Integer.parseInt(stateReps.get(i))+",");
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
        for (int i = 0; i < stateReps.size(); i++) {
            if (ch == stateReps.get(i).charAt(0)){
                g.getCell(row, col).setStatus(states.get(i));
            }
        }
    }

    /**
     * Updates the APPEARANCE of a single cell based on is status
     * @param c - the cell to be updated
     */
    public void updateCellStyle(Cell c) {
        for (int i = 0; i < states.size();i++) {
            if (c.getStatus().equals(states.get(i))) {
                c.getShape().getStyleClass().add(statesCSS.get(i));
            }
        }
    }


}
