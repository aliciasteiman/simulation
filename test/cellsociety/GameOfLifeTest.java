package cellsociety;

import javafx.stage.Stage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameOfLifeTest  extends DukeApplicationTest {

    private SimulationModel myModel;
    private Cell myFirstCell;


    @Override
    public void start (Stage stage) {
        myModel = new GameOfLife("GoL_Test");
    }

    /** Tests to check if the correct neighbors are returned for:
     *  corner cells = 3 valid neighbors
     *  edge cells = 5 valid neighbors
     *  middle cells = 8 valid neighbors
     *   */
    @Test
    void testGetNeighborsCorner() {
        List<Cell> cornerCellNeighbors = myModel.getNeighbors(0,0);
        assertEquals(3, cornerCellNeighbors.size());
        assertEquals(myModel.mySimulationGrid.getCell(0,1), cornerCellNeighbors.get(0));
        assertEquals(myModel.mySimulationGrid.getCell(1,1), cornerCellNeighbors.get(1));
        assertEquals(myModel.mySimulationGrid.getCell(1,0), cornerCellNeighbors.get(2));
    }

    @Test
    void testGetNeighborsEdge() {
        List<Cell> edgeCellNeighbors = myModel.getNeighbors(1,0);
        assertEquals(5, edgeCellNeighbors.size());
        assertEquals(myModel.mySimulationGrid.getCell(0,1), edgeCellNeighbors.get(0));
        assertEquals(myModel.mySimulationGrid.getCell(1,1), edgeCellNeighbors.get(1));
        assertEquals(myModel.mySimulationGrid.getCell(2,1), edgeCellNeighbors.get(2));
        assertEquals(myModel.mySimulationGrid.getCell(0,0), edgeCellNeighbors.get(3));
        assertEquals(myModel.mySimulationGrid.getCell(2,0), edgeCellNeighbors.get(4));
    }

    @Test
    void testGetNeighborsMiddle() {
        List<Cell> middleCellNeighbors = myModel.getNeighbors(1,1);
        assertEquals(8, middleCellNeighbors.size());
        assertEquals(myModel.mySimulationGrid.getCell(0,2), middleCellNeighbors.get(0));
        assertEquals(myModel.mySimulationGrid.getCell(1,2), middleCellNeighbors.get(1));
        assertEquals(myModel.mySimulationGrid.getCell(2,2), middleCellNeighbors.get(2));
        assertEquals(myModel.mySimulationGrid.getCell(0,1), middleCellNeighbors.get(3));
        assertEquals(myModel.mySimulationGrid.getCell(2,1), middleCellNeighbors.get(4));
        assertEquals(myModel.mySimulationGrid.getCell(0,0), middleCellNeighbors.get(5));
        assertEquals(myModel.mySimulationGrid.getCell(1,0), middleCellNeighbors.get(6));
        assertEquals(myModel.mySimulationGrid.getCell(2,0), middleCellNeighbors.get(7));
    }

    /** Checks that the initial block configuration equals the saved file of the block configuration  */
    @Test
    void testSaveCurrConfig() {
        SimulationModel sm = new GameOfLife("GOL_Block");
        Grid g = sm.getMySimulationGrid();
        File f = sm.saveCurrentConfig(g);
        File f1 = new File("data/GOLconfigurations/blockConfig.csv");
        Assertions.assertThat(f1).hasSameContentAs(f);
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
        for (int r = 0; r < myModel.mySimulationGrid.getRows(); r++) {
            for (int c = 0; c < myModel.mySimulationGrid.getCols(); c++) {
                if (myModel.mySimulationGrid.getCell(r,c).getStatus()) {
                    assertEquals(expectedArray[r][c], 1);
                } else{
                    assertEquals(expectedArray[r][c], 0);
                }
            }
        }
    }
}