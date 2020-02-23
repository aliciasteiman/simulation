package cellsociety;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    public static final String TITLE = "Simulation";
    public static final int DISPLAY_WIDTH = 300;
    public static final int DISPLAY_HEIGHT = 400;
    @Override
    public void start(Stage stage) throws Exception {
        SimulationModel model = new SimulationModel();
        SimulationView display = new SimulationView(model);
        stage.setTitle(TITLE);
        stage.setScene(display.makeScene(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        stage.show();
    }
    public static void main (String[] args) {
        launch(args);

    }

}
