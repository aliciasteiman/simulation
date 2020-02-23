package cellsociety;


import java.util.List;

public abstract class SimulationModel {

    protected Grid mySimulationGrid;
    protected List<List<Cell>> mySimulationCells;

    public SimulationModel(String file) {
        mySimulationGrid = new Grid(file);
        mySimulationCells = mySimulationGrid.createGrid();
    }

    public abstract List<List<Cell>> updateGrid();

    public abstract List<Cell> getNeighbors(int row, int col);

}
