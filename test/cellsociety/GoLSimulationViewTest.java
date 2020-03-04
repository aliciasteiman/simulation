package cellsociety;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GoLSimulationViewTest extends DukeApplicationTest {

    private SimulationModel myModel = new GameOfLife("GoL_Test");;
    private SimulationView myView;
    private Scene myScene;
    private Button myStartButton;
    private Button myPauseButton;
    private Button mySaveButton;
    private Button myStepButton;
    private List<List<Rectangle>> myGrid;

    private static final int NUM_ROWS = 9;
    private static final int NUM_COLS = 9;

    @Override
    public void start (Stage stage) {
        myView = new SimulationView(myModel);
        myScene = myView.makeScene(400,600);
        stage.setScene(myScene);
        stage.show();

        myGrid = new ArrayList<>();

        myStartButton = lookup("#startCommand").query();
        myPauseButton = lookup("#pauseCommand").query();
        mySaveButton = lookup("#saveCommand").query();
        myStepButton = lookup("#stepCommand").query();

        getCellsFromGrid();
    }

    /**
     * Test that the initial appearance of the Grid matches the text file
     * passed into the SimulationModel.
     */
    @Test
    void testGridAppearance() {
        //for initial configuration, testing the appearance of the four corners
        //for row 1, col 1 - cell is dead
        Rectangle cell_1_1 = myGrid.get(0).get(0);
        assertEquals("GOL-dead-cell", cell_1_1.getStyleClass().toString());

        //for row 1, col 9 - cell is dead
        Rectangle cell_1_9 = myGrid.get(0).get(8);
        assertEquals("GOL-dead-cell", cell_1_9.getStyleClass().toString());

        //for row 9, col 1 - cell is alive
        Rectangle cell_9_1 = myGrid.get(8).get(0);
        assertEquals("GOL-alive-cell", cell_9_1.getStyleClass().toString());

        //for row, col 9 - cell is alive
        Rectangle cell_9_9 = myGrid.get(8).get(8);
        assertEquals("GOL-alive-cell", cell_9_9.getStyleClass().toString());
    }

    /**
     * Test that the step() method works properly and updates the appearance of the Grid.
     */
    @Test
    void testStepAndUpdateGrid() {
        //making sure that after a single step, the grid updates according to the rules of the simulation
        javafxRun(() -> myView.step());
        getCellsFromGrid();
        //a cell that is alive and has two alive neighbors should show the alive-cell style
        //cell in row 9, col 1 is alive and has two alive neighbors
        Rectangle cell_9_1 = myGrid.get(8).get(0);
        assertEquals("GOL-alive-cell", cell_9_1.getStyleClass().toString());

        //a cell that has three alive neighbors should be alive
        //cell in row 1, col 2 should come back from dead because it has 3 alive neighbors
        Rectangle cell_1_2 = myGrid.get(0).get(1);
        assertEquals("GOL-alive-cell", cell_1_2.getStyleClass().toString());

        //otherwise, a cell should be dead
        //cell in row 3, col 4 has 5 alive neighbors
        Rectangle cell_3_4 = myGrid.get(2).get(3);
        assertEquals("GOL-dead-cell", cell_3_4.getStyleClass().toString());
    }

    /**
     * Test for common Game of Life configurations.
     */
    @Test
    void GameOfLifeBlockTest() {
        myModel = new GameOfLife("GOL_Block");
        javafxRun(() -> start(new Stage()));

        getCellsFromGrid();
        //initial config - BLOCK IN MIDDLE
        Rectangle cell_1_1 = myGrid.get(1).get(1);
        assertEquals("GOL-alive-cell", cell_1_1.getStyleClass().toString());
        Rectangle cell_1_2 = myGrid.get(1).get(2);
        assertEquals("GOL-alive-cell", cell_1_2.getStyleClass().toString());

        //after step - MAKING SURE IT DOESN'T CHANGE
        javafxRun(() -> myView.step());
        getCellsFromGrid();
        assertEquals("GOL-alive-cell", cell_1_1.getStyleClass().toString());
        assertEquals("GOL-alive-cell", cell_1_2.getStyleClass().toString());
    }

    /**
     * Helper method that finds the new cells by query after
     * the grid appearance gets updated.
     */
    private void getCellsFromGrid() {
        for (int r = 0; r < NUM_ROWS; r++) {
            List<Rectangle> row = new ArrayList<>();
            for (int c = 0; c < NUM_COLS; c++) {
                Rectangle cell = lookup("#cell" + r + c).query();
                row.add(cell);
            }
            myGrid.add(row);
        }
    }



}