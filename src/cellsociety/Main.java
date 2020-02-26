package cellsociety;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;


/**
 * Cellular Automata -- simulations based on a model that consists of a regular grid of cells, each in one of a
 * finite number of states; cells are updated based on their simulation-specific rules
 */
public class Main extends Application {
    public static final String TITLE = "Simulation";
    public int DISPLAY_WIDTH; //400
    public int DISPLAY_HEIGHT; //500

    /**
     * Initializes a new SimulationModel and SimulationView of the model
     * Sets/displays the scene for a stage
     * @param stage
     */
    @Override
    public void start(Stage stage) {
        SimulationModel model = new GameOfLife("GOLconfigurations/gliderConfig.csv");
        SimulationView display = new SimulationView(model);

        Scanner scanner = new Scanner(System.in); //will eventually change to be an interface feature
        System.out.println("Enter width: ");
        DISPLAY_WIDTH = scanner.nextInt();
        System.out.println("Enter height: ");
        DISPLAY_HEIGHT = scanner.nextInt();

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
