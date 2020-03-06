package cellsociety;
import cellsociety.configuration.SimulationModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PercolationTest {
    private List<String> states = List.of("blocked","open","full");
    SimulationModel myModel = new SimulationModel();
    private Grid myGrid = myModel.initSimulation("Percolate_Test");
    int[][] expectedInitGrid = {
        {0,0,2,1,1,0,0,0},
        {1,0,0,1,1,1,1,1},
        {1,1,1,0,0,1,1,0},
        {0,0,1,1,0,1,1,1},
        {0,0,1,1,0,1,1,1},
        {0,1,1,1,0,1,1,0},
        {0,1,0,0,0,0,1,1},
        {1,0,1,0,1,1,1,1},
        {1,1,1,1,0,1,0,0}};

    @Test
    void testInitialPercConfig() {
        for (int r = 0; r < myGrid.getRows(); r++) {
            for (int c = 0; c < myGrid.getCols();c++) {
                assertEquals(myGrid.getCell(r,c).getStatus(), states.get(expectedInitGrid[r][c]));
            }

        }
    }

    @Test
    void testUpdateCells() {
        myGrid = myModel.updateGrid();
        //check for a open cell next to a FULL cell - should become full
        assertEquals("full", myGrid.getCell(0,3).getStatus());
        //check for a open cell not next to a full cell - should stay open
        assertEquals("open", myGrid.getCell(2,1).getStatus());
        //check that blocked cell stays blocked
        assertEquals("blocked", myGrid.getCell(0,0).getStatus());

    }
}
