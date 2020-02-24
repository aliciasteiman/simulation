package cellsociety;


import java.util.List;

public abstract class SimulationModel {

    protected Grid mySimulationGrid;

    public SimulationModel(String file) {

        mySimulationGrid = new Grid(file);
    }

    public abstract Grid updateCells();

    public abstract List<Cell> getNeighbors(int row, int col);

    public void step() {
        mySimulationGrid = updateCells();

    }
}
