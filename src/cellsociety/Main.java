package cellsociety;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;


public class Main extends Application {
    public static final String TITLE = "Simulation";
    public int DISPLAY_WIDTH; //400
    public int DISPLAY_HEIGHT; //500

    @Override
    public void start(Stage stage) {
        SimulationModel model = new GameOfLife("test/GOLconfig.csv");
        SimulationView display = new SimulationView(model);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter width: ");
        DISPLAY_WIDTH = scanner.nextInt();
        System.out.println("Enter height: ");
        DISPLAY_HEIGHT = scanner.nextInt();

        stage.setTitle(TITLE);
        stage.setScene(display.makeScene(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        stage.show();
    }

    public static void main (String[] args) {
        launch(args);
    }

}
