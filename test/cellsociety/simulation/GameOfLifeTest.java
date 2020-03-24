package cellsociety.simulation;

import cellsociety.Cell;
import cellsociety.configuration.SimulationModel;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeTest  extends DukeApplicationTest {

    private Simulation sim;
    private SimulationModel simulationModel;
    private List<String> states = List.of("dead", "alive");
    private List<String> stateReps = List.of("0","1");
    private List<String> stateCSS = List.of("GOL-dead-cell","GOL-alive-cell");


    @Override
    public void start (Stage stage) {
        sim = new GameOfLife(states, stateReps, stateCSS);
        simulationModel = new SimulationModel();
        sim.setGrid(simulationModel.initializeSimulationGrid("GOL_Test"));
    }

    /** Tests to check if the correct neighbors are returned for:
     *  corner cells = 3 valid neighbors
     *  edge cells = 5 valid neighbors
     *  middle cells = 8 valid neighbors
     *   */
    @Test
    void testGetNeighborsCorner() {
        List<Cell> cornerCellNeighbors = sim.getNeighbors(0,0);
        assertEquals(3, cornerCellNeighbors.size());
        assertEquals(sim.getGrid().getCell(0,1), cornerCellNeighbors.get(0));
        assertEquals(sim.getGrid().getCell(1,1), cornerCellNeighbors.get(1));
        assertEquals(sim.getGrid().getCell(1,0), cornerCellNeighbors.get(2));
    }

    @Test
    void testGetNeighborsEdge() {
        List<Cell> edgeCellNeighbors = sim.getNeighbors(1,0);
        assertEquals(5, edgeCellNeighbors.size());
        assertEquals(sim.getGrid().getCell(0,1), edgeCellNeighbors.get(0));
        assertEquals(sim.getGrid().getCell(1,1), edgeCellNeighbors.get(1));
        assertEquals(sim.getGrid().getCell(2,1), edgeCellNeighbors.get(2));
        assertEquals(sim.getGrid().getCell(0,0), edgeCellNeighbors.get(3));
        assertEquals(sim.getGrid().getCell(2,0), edgeCellNeighbors.get(4));
    }

    @Test
    void testGetNeighborsMiddle() {
        List<Cell> middleCellNeighbors = sim.getNeighbors(1,1);
        assertEquals(8, middleCellNeighbors.size());
        assertEquals(sim.getGrid().getCell(0,2), middleCellNeighbors.get(0));
        assertEquals(sim.getGrid().getCell(1,2), middleCellNeighbors.get(1));
        assertEquals(sim.getGrid().getCell(2,2), middleCellNeighbors.get(2));
        assertEquals(sim.getGrid().getCell(0,1), middleCellNeighbors.get(3));
        assertEquals(sim.getGrid().getCell(2,1), middleCellNeighbors.get(4));
        assertEquals(sim.getGrid().getCell(0,0), middleCellNeighbors.get(5));
        assertEquals(sim.getGrid().getCell(1,0), middleCellNeighbors.get(6));
        assertEquals(sim.getGrid().getCell(2,0), middleCellNeighbors.get(7));
    }


    /** Check if cell at a particular i,j position is updated to alive if passed in a '1' */
    @Test
    void testSetCellFromFile() {
        int[][] expectedArray = {{0,1,0,1,0,1,0,1,0},
                {1,1,1,0,1,0,1,1,0},
                {0,0,0,0,0,0,0,0,0},
                {1,1,1,1,1,1,1,1,1},
                {0,0,0,1,1,1,0,0,0},
                {1,0,1,0,1,0,1,0,1},
                {1,1,0,0,1,1,0,0,1},
                {0,1,1,1,0,0,0,1,1},
                {1,1,1,0,0,0,1,1,1}};
        for (int r = 0; r < sim.getGrid().getRows(); r++) {
            for (int c = 0; c < sim.getGrid().getCols(); c++) {
                if (sim.getGrid().getCell(r,c).getStatus().equals("alive")) {
                    assertEquals(expectedArray[r][c], 1);
                } else{
                    assertEquals(expectedArray[r][c], 0);
                }
            }
        }
    }
}