package cellsociety;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;

import java.util.List;


public class SimulationView {
    private SimulationModel myModel;
    private GridPane pane;
    private int myCols;
    private int myRows;
    private Grid myGrid;
    private List<List<Cell>> myCells;

    public static final String CELL_STYLESHEET = "cell.css";

    public SimulationView(SimulationModel model) {
        myModel = model;
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        myGrid = new Grid("test/testing");
        myCells = myGrid.createGrid();
    }

    public Scene makeScene(int width, int height) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(CELL_STYLESHEET);
        myCols = myGrid.getCols();
        myRows = myGrid.getRows();
        updateGridAppearance(width/ myRows, (height-100)/ myCols);
        root.setCenter(pane);
        root.setBottom(addButtons());
        return scene;
    }

    public void updateCellAppearance(int row, int col, double cell_width, double cell_height) {
        Cell c = myCells.get(row).get(col);
        c.setShape(new Rectangle(cell_width, cell_height));
        if (c.getStatus()==true) {
            c.getShape().getStyleClass().add("alive-cell");
        } else {
            c.getShape().getStyleClass().add("dead-cell");
        }
        pane.add(c.getShape(), col, row);

    }
    public void updateGridAppearance(double cell_width, double cell_height) {
        for (int row = 0; row < myRows; row++) {
            for (int col = 0; col < myCols; col++) {
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
