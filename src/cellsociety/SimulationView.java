package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ResourceBundle;

/**
 * Class used to display the viewer for a Simulation
 */
public class SimulationView {

    private SimulationModel myModel;
    private Timeline myAnimation;
    private GridPane pane;
    private int myCols;
    private int myRows;
    private Grid myGrid;
    private double CELL_WIDTH;
    private double CELL_HEIGHT;

    private ResourceBundle myResources;
    private Button startButton;
    private Button saveButton;
    private Button pauseButton;
    private Button stepButton;
    private int BUTTON_SPACING;

    private ComboBox myConfigurations;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String buttonNamesFile = "ButtonNames";
    public static final String CELL_STYLESHEET = "resources/style.css";

    private int WIDTH;
    private int HEIGHT;

    /**
     * Constructs the view of a given SimulationModel
     * @param model -- e.g. GameOfLife
     */
    public SimulationView(SimulationModel model) {
        myModel = model;
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        myGrid = myModel.getMySimulationGrid();
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + buttonNamesFile);
    }

    public Scene makeScene(int width, int height) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(CELL_STYLESHEET);
        myCols = myGrid.getCols();
        myRows = myGrid.getRows();
        CELL_HEIGHT = (height - 100) / myCols;
        CELL_WIDTH = width/ myRows;
        BUTTON_SPACING = width/15;
        updateGridAppearance();
        root.setCenter(pane);
        root.setBottom(addButtons());
        root.setTop(makeConfigurationsMenu());
        return scene;
    }


    public void updateCellAppearance(int row, int col) {
        Cell c = myGrid.getCell(row, col);
        c.setShape(new Rectangle(CELL_WIDTH, CELL_HEIGHT));
        c.getShape().setId("cell" + row + col);
        myModel.updateCellStyle(c);
        pane.add(c.getShape(), col, row);

    }

    public void updateGridAppearance() {
        for (int row = 0; row < myGrid.getRows(); row++) {
            for (int col = 0; col < myGrid.getCols(); col++) {
                updateCellAppearance(row, col);
            }
        }
    }

    public void step() {
        myGrid = myModel.updateCells();
        updateGridAppearance();
    }


    public void setAnimation() {
        KeyFrame frame = new KeyFrame(Duration.seconds(20.0 / 60), e -> step()); //what should the Duration.seconds be?
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
    }

    public Node addButtons() {
        HBox userButtons = new HBox();
        userButtons.setAlignment(Pos.CENTER);
        userButtons.setSpacing(BUTTON_SPACING);

        startButton = makeButton("startCommand", e -> setAnimation());
        pauseButton = makeButton("pauseCommand", e -> myAnimation.pause());
        saveButton = makeButton("saveCommand", e -> myModel.saveCurrentConfig(myGrid));
        stepButton = makeButton("stepCommand", e -> step());

        userButtons.getChildren().add(startButton);
        userButtons.getChildren().add(pauseButton);
        userButtons.getChildren().add(saveButton);
        userButtons.getChildren().add(stepButton);

        return userButtons;
    }

    private Button makeButton(String name, EventHandler<ActionEvent> handler) {
        Button b = new Button();
        b.setText(myResources.getString(name));
        b.setMaxSize(60,20);
        b.setId(name);
        b.setOnAction(handler);
        return b;
    }

    private Node makeConfigurationsMenu() {
        HBox result = new HBox();
        myConfigurations = new ComboBox<>();
        myConfigurations.setId("ConfigurationsMenu");
        myConfigurations.setPromptText(myResources.getString("ConfigurationsMenu"));
        result.getChildren().add(myConfigurations);
        SimulationModel newModel = new GameOfLife("GOLconfigurations/blinkerConfig.csv");
        //myConfigurations.getItems().add(makeButton("blinkerConfig", e -> ?);
        return result;
    }
}
