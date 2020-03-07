package cellsociety.simulation;

import cellsociety.Grid;
import cellsociety.configuration.SimulationModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SegregationTest {
    private List<String> states = List.of("empty","agent1","agent2");
    SimulationModel myModel = new SimulationModel();
    private Grid myGrid = myModel.initSimulation("MOS_Test");
    int[][] initSegConfig = {
            {2,2,1,2,1},
            {0,1,1,1,1},
            {2,2,0,0,0},
            {2,1,2,2,2},
            {2,1,1,0,1}};

    int[][] stepSegConfig = {
            {2,1,1,1,1},
            {1,1,1,1,1},
            {2,2,2,1,0},
            {2,0,2,2,2},
            {2,0,0,2,0}};

    //testing initial config for Percolation.
    @Test
    void testInitialConfig() {
        for (int r = 0; r < myGrid.getRows(); r++) {
            for (int c = 0; c < myGrid.getCols();c++) {
                assertEquals(myGrid.getCell(r,c).getStatus(), states.get(initSegConfig[r][c]));
            }
        }
    }

    //testing that Segregation follows the appropriate rules.
    @Test
    void testSegregationUpdateCells() {
        myGrid = myModel.updateGrid();
        //test that dissatisfied agent leaves - less than 30% of neighbors are also agent2
        assertNotEquals("agent2", myGrid.getCell(0,3).getStatus());
        //test that satisfied agent stays - agent1 in row 5, col 1 stays b/c 50% of neighbors are same
        assertEquals("agent1", myGrid.getCell(1,2).getStatus());

    }



}