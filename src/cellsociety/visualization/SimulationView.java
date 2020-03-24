package cellsociety.visualization;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.configuration.SimulationModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import java.util.*;

/**
 * Class used to display the viewer for a Simulation
 */
public class SimulationView {
    private SimulationModel simulationModel;
    private Timeline myAnimation;
    private Scene myScene;
    private Pane gridPane;
    private Grid myGrid;
    private ResourceBundle myButtons;
    private int buttonSpacing;
    private BorderPane myScreen;
    private ChoicePanel choicePanel;
    private GridView gridView;
    private OperationsPanel operationsPanel;
    private double speed;
    private CellCustomizationPanel customizationPanel;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String BUTTON_NAMES_FILE = "ButtonNames";
    public static final String CELL_STYLESHEET = "resources/style.css";
    public final int FRAMES_PER_SECOND = 60;
    public double SECOND_DELAY = 20.0;

    /**
     * Constructs the view of a given SimulationModel
     * @param model -- e.g. GameOfLife
     */
    public SimulationView(SimulationModel model) {
        simulationModel = model;
        myButtons = ResourceBundle.getBundle(RESOURCE_PACKAGE + BUTTON_NAMES_FILE);
        choicePanel = new ChoicePanel(myButtons);
        operationsPanel = new OperationsPanel(myButtons);
        customizationPanel = new CellCustomizationPanel(simulationModel);
    }

    /**
     * Returns scene for the simulation so it can be added to the stage in Main
     * @param width
     * @param height
     * @return scene
     */
    public Scene makeScene(int width, int height) {
        myScreen = new BorderPane();
        handleScreenSetUp(width, height);
        myScene = new Scene(myScreen, width, height);
        myScene.getStylesheets().add(CELL_STYLESHEET);
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        return myScene;
    }

    private void handleScreenSetUp(int width, int height) {
        Map<String, List<String>> simMap = simulationModel.getSimulationsAndConfigurationsMap();
        myScreen.setTop(choicePanel.makeConfigurationsMenu(height, simMap,
                (observableValue, oldValue, selected) ->
                {if (((String) selected)!=null) {loadNewConfig((String) selected);}}));

        myScreen.setRight(customizationPanel.getCustomizePanel(width, height));

        buttonSpacing = width/20;
        myScreen.setBottom(operationsPanel.addButtons(height, buttonSpacing,
                e -> start(),
                e -> myAnimation.pause(),
                e -> saveConfigDialogBox(),
                e -> step(),
                e -> changeSpeed(-10),
                e-> changeSpeed(10)));

        gridView = new RectangleGridView(width-customizationPanel.getWidth(),
                height-(choicePanel.getHeight()+operationsPanel.getHeight()));
        gridPane = gridView.getInitGridPane();
        myScreen.setCenter(gridPane);
    }

    /**
     * Loops through through each cell in the grid and updates the individual cell using the helper method
     */
    public void updateGridAppearance() {
        gridPane = gridView.handleGridSetUp(myGrid);
        for (int row = 0; row < myGrid.getRows(); row++) {
            for (int col = 0; col < myGrid.getCols(); col++) {
                updateCellAppearance(row, col);
            }
        }
    }

    private void updateCellAppearance(int row, int col) {
        Cell c = myGrid.getCell(row, col);
        simulationModel.updateCell(customizationPanel.getOverriddenImages(),
                customizationPanel.getOverriddenColors(),c);
        allowUserClick(c);
    }

    private void allowUserClick(Cell c) {
        EventHandler<MouseEvent> userClick = e -> {
            c.setStatus(chooseStateDialog());
            simulationModel.updateCell(customizationPanel.getOverriddenImages(),
                    customizationPanel.getOverriddenColors(),c);
        };
        c.getShape().addEventHandler(MouseEvent.MOUSE_CLICKED, userClick);
    }

    private String chooseStateDialog() {
        List<String> states = simulationModel.getSimulationStates();
        ChoiceDialog statesDialog = new ChoiceDialog(states.get(0), states);
        statesDialog.setHeaderText("Select state");
        Optional<String> response = statesDialog.showAndWait();
        String selected = "";
        if (response.isPresent()) {
            selected = response.get();
        }
        return selected;
    }

    /**
     * In each "step" of the simulation, the grid appearance is updated to reflect changes between
     * cell generations.
     */
    public void step() {
        myGrid = simulationModel.updateGrid();
        updateGridAppearance();
    }

    private void start() {
        speed = SECOND_DELAY / FRAMES_PER_SECOND;
        KeyFrame frame = new KeyFrame(Duration.seconds(speed), e -> step());
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
    }

    private void changeSpeed(int speed) {
        SECOND_DELAY += speed;
        myAnimation.stop();
        start();
    }
    /**
     * Loads a new configuration based on the file selected by the user.
     * @param file
     */
    public void loadNewConfig(String file) {
        gridPane.getChildren().clear();
        customizationPanel.clearComboBoxes();
        myAnimation.pause();
        myGrid = simulationModel.initializeSimulationGrid(file);
        updateGridAppearance();
        customizationPanel.colorAndImageOptions(gridView);
    }

    private void saveConfigDialogBox() {
        TextInputDialog input = new TextInputDialog();
        List<String> userInput = new ArrayList<>();
        input.setTitle("Save Current Configuration");
        input.setHeaderText("Input the following information for the configuration.");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        TextField title = makeTextBox("Title", 0, 0, grid);
        TextField author = makeTextBox("Author", 0, 1, grid);
        TextField description = makeTextBox("Description", 0, 2, grid);
        input.getDialogPane().setContent(grid);

        Optional<String> res = input.showAndWait();
        if (res.isPresent()) {
            userInput.add(title.getText());
            userInput.add(author.getText());
            userInput.add(description.getText());
            simulationModel.writeCurrentGridConfig(userInput.get(0), userInput);
        }
    }
    private TextField makeTextBox(String str, int colIdx, int rowIdx, GridPane gp) {
        TextField tf = new TextField();
        gp.add(new Label(str), colIdx, rowIdx);
        gp.add(tf, colIdx + 1, rowIdx);
        return tf;
    }
}
