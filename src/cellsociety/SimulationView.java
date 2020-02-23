package cellsociety;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;


public class SimulationView {
    private SimulationModel myModel;
    private GridPane pane;
    private int numCols;
    private int numRows;

    public static final String CELL_STYLESHEET = "cell.css";

    public SimulationView(SimulationModel model) {
        myModel = model;
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
    }

    public Scene makeScene(int width, int height) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(CELL_STYLESHEET);
        numCols = 9;
        numRows = 9;
        updateGridAppearance(width/numRows, (height-100)/numCols);
        root.setCenter(pane);
        root.setBottom(addButtons());
        return scene;
    }

    public void updateCellAppearance(int row, int col, double cell_width, double cell_height) {
        Rectangle cell = new Rectangle(cell_width, cell_height);
        cell.getStyleClass().add("dead-cell");
        pane.add(cell, col, row);

    }
    public void updateGridAppearance(double cell_width, double cell_height) {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                updateCellAppearance(row, col, cell_width, cell_height);
            }
        }
    }

    public Node addButtons() {
        HBox userButtons = new HBox();
        Button startButton = new Button("Start");
        startButton.setMaxSize(50,20);
        userButtons.getChildren().add(startButton);
        return userButtons;
    }

}
