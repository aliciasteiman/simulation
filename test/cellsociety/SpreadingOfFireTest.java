package cellsociety;

import cellsociety.configuration.SimulationModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpreadingOfFireTest {
    private List<String> states = List.of("empty","tree","burning");
    SimulationModel myModel = new SimulationModel();
    private Grid myGrid = myModel.initSimulation("SoF_Test");
    int[][] sofConfig = {
            {0,0,0,0,0,0,0,0,0},
            {0,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,0},
            {0,1,1,1,2,1,1,1,0},
            {0,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,0},
            {0,1,1,1,1,1,1,1,0},
            {0,0,0,0,0,0,0,0,0}
            };

    @Test
    void initialSOFConfig() {
        for (int r = 0; r < myGrid.getRows(); r++) {
            for (int c = 0; c < myGrid.getCols();c++) {
                assertEquals(myGrid.getCell(r,c).getStatus(), states.get(sofConfig[r][c]));
            }
        }
    }

    @Test
    void testSOFUpdateCells() {
        myGrid = myModel.updateGrid();
        //check that a empty cell is still empty
        assertEquals("empty",myGrid.getCell(0,0).getStatus());

        //check that a burning cell becomes empty
        assertEquals("empty", myGrid.getCell(4,4).getStatus());

        //check that tree surrounded by no burning cells is still a tree
        assertEquals("tree", myGrid.getCell(1,1).getStatus());
    }



}
