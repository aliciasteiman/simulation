package cellsociety;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ResourceBundle;
import java.util.Scanner;

public class SimulationView {

    private SimulationModel myModel;
    private Timeline myAnimation;
    private Scene myScene;
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

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String buttonNamesFile = "ButtonNames";
    public static final String CELL_STYLESHEET = "resources/style.css";

    public SimulationView(SimulationModel model) {
        myModel = model;
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + buttonNamesFile);
    }

    public Scene makeScene(int width, int height) {
        BorderPane root = new BorderPane();
        myScene = new Scene(root, width, height);
        myScene.getStylesheets().add(CELL_STYLESHEET);
        myGrid = myModel.getMySimulationGrid();
        myCols = myGrid.getCols();
        myRows = myGrid.getRows();
        CELL_HEIGHT = (height - 100) / myCols;
        CELL_WIDTH = width/ myRows;
        BUTTON_SPACING = width/15;
        updateGridAppearance();
        root.setCenter(pane);
        root.setBottom(addButtons());
        return myScene;
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
        //startButton.setOnMouseClicked(e -> setAnimation());

        pauseButton = makeButton("pauseCommand", e -> myAnimation.pause());
        //pauseButton.setOnMouseClicked(e -> myAnimation.pause());

        saveButton = makeButton("saveCommand", e -> myModel.saveCurrentConfig(myGrid));
        //saveButton.setOnMouseClicked(e -> myModel.saveCurrentConfig(myGrid));

        stepButton = makeButton("stepCommand", e -> step());
        //stepButton.setOnMouseClicked(e -> step());

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

}
