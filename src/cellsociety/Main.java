package cellsociety;

import cellsociety.configuration.SimulationModel;
import cellsociety.visualization.SimulationView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;


/**
 * Cellular Automata -- simulations based on a model that consists of a regular grid of cells, each in one of a
 * finite number of states; cells are updated based on their simulation-specific rules
 */
public class Main extends Application {
    public static final String TITLE = "Simulation";
    public int DISPLAY_WIDTH = 800;
    public int DISPLAY_HEIGHT= 800;

    /**
     * Initializes a new SimulationModel and SimulationView of the model
     * Sets/displays the scene for a stage
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        SimulationModel model = new SimulationModel();
        SimulationView display = new SimulationView(model);
        stage.setTitle(TITLE);
        stage.setScene(display.makeScene(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        stage.show();
    }

    /**
     * Start of the program -- launches the JavaFX view of the simulation
     * @param args
     */
    public static void main (String[] args) {
        launch(args);
    }

}
