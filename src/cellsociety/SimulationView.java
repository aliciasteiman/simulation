package cellsociety;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.security.Key;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public class SimulationView {
    private SimulationModel myModel;
    private GridPane pane;
    private int myCols;
    private int myRows;
    private Grid myGrid;
    private double cell_width;
    private double cell_height;
    private ResourceBundle myResources;
    private Button startButton;
    private Button saveButton;
    private Button pauseButton;
    private Timeline myAnimation;
    private Button stepButton;
    private Button pauseAndSaveButton;
    private int buttonSpacing;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String buttonNamesFile = "ButtonNames";
    public static final String CELL_STYLESHEET = "resources/style.css";


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
        cell_height = (height - 100) / myCols;
        cell_width = width/ myRows;
        buttonSpacing = width/15;
        updateGridAppearance();
        root.setCenter(pane);
        root.setBottom(addButtons());
        return scene;
    }


    public void updateCellAppearance(int row, int col) {
        Cell c = myGrid.getCell(row, col);
        c.setShape(new Rectangle(cell_width, cell_height));
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
        userButtons.setSpacing(buttonSpacing);

        startButton = makeButton("startCommand");
        startButton.setOnMouseClicked(e -> setAnimation());

        pauseButton = makeButton("pauseCommand");
        pauseButton.setOnMouseClicked(e -> myAnimation.pause());

        saveButton = makeButton("pauseAndSaveCommand");
        saveButton.setOnMouseClicked(e -> myModel.saveCurrentConfig(myGrid));

        stepButton = makeButton("stepCommand");

        userButtons.getChildren().add(startButton);
        userButtons.getChildren().add(pauseButton);
        userButtons.getChildren().add(saveButton);
        userButtons.getChildren().add(stepButton);
        return userButtons;
    }

    private Button makeButton(String name) {
        Button b = new Button();
        b.setText(myResources.getString(name));
        b.setMaxSize(60,20);
        b.setId(name);
        return b;
    }

}
