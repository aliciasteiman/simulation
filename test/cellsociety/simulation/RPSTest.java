package cellsociety.simulation;

import cellsociety.Grid;
import cellsociety.configuration.SimulationModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RPSTest {

    private List<String> states = List.of("rock","paper","scissors");
    SimulationModel myModel = new SimulationModel();
    private Grid myGrid = myModel.initSimulation("RPS");
    int[][] expectedInitGrid = {
            {0,1,2,1,0},
            {1,1,2,1,1},
            {0,2,2,2,0},
            {1,0,0,0,1},
            {0,1,1,1,0}};

    @Test
    void testInitialRPSConfig() {
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
        // check: rock changes to paper
        assertEquals("paper", myGrid.getCell(0,0).getStatus());
        // check: paper changes to scissors
        assertEquals("scissors", myGrid.getCell(1,1).getStatus());
        // check: scissors change to rock
        assertEquals("rock", myGrid.getCell(2,1).getStatus());
        //check that cell doesn't change state if conditions not met
        assertEquals("rock", myGrid.getCell(4,0).getStatus());
        assertEquals("paper", myGrid.getCell(0,1).getStatus());
        assertEquals("scissors", myGrid.getCell(0,2).getStatus());


    }
}

//    @Test
//    void updateCells() {
//    }
//
//    @Test
//    void getNeighbors() {
//    }
//}