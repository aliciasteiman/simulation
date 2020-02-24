package cellsociety;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
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
import java.util.concurrent.TimeUnit;

public class SimulationView {
    private SimulationModel myModel;
    private GridPane pane;
    private int myCols;
    private int myRows;
    private Grid myGrid;
    private double cell_width;
    private double cell_height;
    private Button startButton;

    public static final String CELL_STYLESHEET = "cell.css";

    public SimulationView(SimulationModel model) {
        myModel = model;
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        myGrid = myModel.getMySimulationGrid();
    }

    public Scene makeScene(int width, int height) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(CELL_STYLESHEET);
        myCols = myGrid.getCols();
        myRows = myGrid.getRows();
        cell_width = width/ myRows;
        cell_height = (height-100)/ myCols;
        updateGridAppearance();
        root.setCenter(pane);
        root.setBottom(addButtons());
        return scene;
    }

    public void updateCellAppearance(int row, int col) {
        Cell c = myGrid.getCell(row, col);
        c.setShape(new Rectangle(cell_width, cell_height));
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

    /*
    public void runAnimation() {
        AnimationTimer runAnimation = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos(500)) {
                    step();
                    lastUpdate = now;
                }
            }
        };
        startButton.setOnAction(l -> runAnimation.start());
    }
     */

    public void setAnimation() {
        KeyFrame frame = new KeyFrame(Duration.seconds(5.0/60), e -> step()); //what should the Duration.seconds be?
        Timeline myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        startButton.setOnAction(l -> myAnimation.play());
    }

    public Node addButtons() {
        HBox userButtons = new HBox();
        startButton = new Button("Start");
        startButton.setMaxSize(50,20);
        //runAnimation();
        setAnimation();
        userButtons.getChildren().add(startButton);
        return userButtons;
    }

}
