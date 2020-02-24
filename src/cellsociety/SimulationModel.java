package cellsociety;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class SimulationModel {

    protected Grid mySimulationGrid;

    public SimulationModel(String file) {
        readGridFromFile(file);
    }

    public abstract Grid updateCells();

    public abstract List<Cell> getNeighbors(int row, int col);

    public abstract void updateCell(Cell c);

    public abstract void setCellFromFile(int row, int col, char ch);

    public Grid getMySimulationGrid() {
        return mySimulationGrid;
    }

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
