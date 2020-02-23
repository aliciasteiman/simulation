package cellsociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Grid {

    private List<String> initialStates;
    private int NUM_ROWS;
    private int NUM_COLS;
    private Cell cell;
    private List<List<Cell>> allCells;

    public Grid(String file) {
        initialStates = new ArrayList<>();
        Scanner input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(file));

        while (input.hasNext()) {
            initialStates.add(input.nextLine());
        }
        createGrid();
    }

    public List<List<Cell>> createGrid() {
        allCells = new ArrayList<>();
        NUM_ROWS = Integer.parseInt(initialStates.get(0).substring(0,1));
        NUM_COLS = Integer.parseInt(initialStates.get(0).substring(2));

        for (int row = 1; row <= NUM_ROWS; row++) {
            String rowConfig = initialStates.get(row);
            String cells = rowConfig.replaceAll("\\s", "");
            List<Cell> cellsRow = new ArrayList<>();

            for (int col = 0; col < NUM_COLS; col++) {
                if (cells.charAt(col) == '0') {
                    cell = new Cell(false);
                }
                if (cells.charAt(col) == '1') {
                    cell = new Cell(true);
                }
                cellsRow.add(cell);
            }
            allCells.add(cellsRow);
        }
        return allCells;
    }

    public int getRows() {
        return NUM_ROWS;
    }

    public int getCols() {
        return NUM_COLS;
    }

    public Cell getCell(int row, int col) {
        return allCells.get(row).get(col);
    }

    public int countAliveNeighbors(List<Cell> neighbors) {
        int count  = 0;
        for (Cell cell : neighbors) {
            if (cell.getStatus() == true) {
                count += 1;
            }
        }
        return count;
    }

}
