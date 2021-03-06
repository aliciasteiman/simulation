package cellsociety.visualization;

import cellsociety.Cell;
import cellsociety.Grid;
import cellsociety.configuration.PropertiesConfiguration;
import cellsociety.configuration.SimulationModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.*;
import java.util.*;

/**
 * Class used to display the viewer for a Simulation
 */
public class SimulationView {
    private SimulationModel simulationModel;
    private Timeline myAnimation;
    private Scene myScene;
    private GridPane gridPane;
    private Grid myGrid;
    private double cellWidth;
    private double cellHeight;
    private ResourceBundle myButtons;
    private Button startButton;
    private Button saveButton;
    private Button pauseButton;
    private Button stepButton;
    private Button speedUpButton;
    private Button slowDownButton;
    private int BUTTON_SPACING;
    private ComboBox mySimulations;
    private ComboBox myConfigurations;
    private BorderPane myScreen;
    private HBox topBar;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String buttonNamesFile = "ButtonNames";
    public static final String CELL_STYLESHEET = "resources/style.css";

    public final int FRAMES_PER_SECOND = 60;
    public double SECOND_DELAY = 20.0;
    public double SPEED;

    /**
     * Constructs the view of a given SimulationModel
     * @param model -- e.g. GameOfLife
     */
    public SimulationView(SimulationModel model) {
        simulationModel = model;
        myButtons = ResourceBundle.getBundle(RESOURCE_PACKAGE + buttonNamesFile);
    }

    /**
     * Returns scene for the simulation so it can be added to the stage in Main
     * @param width
     * @param height
     * @return scene
     */
    public Scene makeScene(int width, int height) {
        myScreen = new BorderPane();
        BUTTON_SPACING = width/20;
        handleScreenSetUp();
        myScene = new Scene(myScreen, width, height);
        myScene.getStylesheets().add(CELL_STYLESHEET);
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        return myScene;
    }

    private void handleScreenSetUp() {
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        myScreen.setCenter(gridPane);
        myScreen.setTop(makeConfigurationsMenu());
    }

    /**
     * Determines the cell width, height, and updates the Grid appearance.
     * @param width
     * @param height
     */
    public void handleGridSetUp(int width, int height) {
        cellHeight = (height - 100) / myGrid.getCols();
        cellWidth = width/ myGrid.getCols();
        updateGridAppearance();
    }

    /**
     * Loops through through each cell in the grid and updates the individual cell using the helper method
     */
    private void updateGridAppearance() {
        for (int row = 0; row < myGrid.getRows(); row++) {
            for (int col = 0; col < myGrid.getCols(); col++) {
                updateCellAppearance(row, col);
            }
        }
    }

    /**
     * Updates the color of a cell based on its new status (i.e. alive vs. dead)
     * @param row - row position of the cell
     * @param col - column position of the cell
     */
    private void updateCellAppearance(int row, int col) {
        Cell c = myGrid.getCell(row, col);
        c.setShape(new Rectangle(cellWidth, cellHeight));
        c.getShape().setId("cell" + row + col);
        simulationModel.updateCell(c);
        allowUserClick(c);
        gridPane.add(c.getShape(), col, row);
    }

    private void allowUserClick(Cell c) {
        EventHandler<MouseEvent> userClick = e -> {
            c.setStatus(chooseStateDialog());
            simulationModel.updateCell(c);
        };
        c.getShape().addEventFilter(MouseEvent.MOUSE_CLICKED, userClick);
    }

    private String chooseStateDialog() {
        List<String> states = simulationModel.SimulationStatesLists().get(1);
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

    private void setAnimation() {
        SPEED = SECOND_DELAY / FRAMES_PER_SECOND;
        KeyFrame frame = new KeyFrame(Duration.seconds(SPEED), e -> step());
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
    }

    /**
     * Initializes an HBox of buttons and sets their alignment
     * Initializes a start, pause, save, and step button and adds them to the HBox (root)
     * @return userButtons -- HBox of buttons
     */
    private Node addButtons() {
        HBox userButtons = new HBox();
        userButtons.setAlignment(Pos.CENTER);
        userButtons.setSpacing(BUTTON_SPACING);

        startButton = makeButton("startCommand", e -> setAnimation());
        pauseButton = makeButton("pauseCommand", e -> myAnimation.pause());
        saveButton = makeButton("saveCommand", e -> saveConfigDialogBox());
        stepButton = makeButton("stepCommand", e -> step());
        speedUpButton = makeButton("speedUpCommand", e -> changeSpeed(-10));
        slowDownButton = makeButton("slowDownCommand", e -> changeSpeed(10));

        userButtons.getChildren().add(startButton);
        userButtons.getChildren().add(pauseButton);
        userButtons.getChildren().add(saveButton);
        userButtons.getChildren().add(stepButton);
        userButtons.getChildren().add(speedUpButton);
        userButtons.getChildren().add(slowDownButton);
        return userButtons;
    }

    private void changeSpeed(int speed) {
        SECOND_DELAY += speed;
        myAnimation.stop();
        setAnimation();
    }

    /**
     * Helper method to create a button given an identifying name and setting an event to occur on action
     * e.g. start button "Start" starts the animation when clicked on
     * @param name -- identifier in the .properties file
     * @param handler -- action the button should perform
     * @return b -- Button
     */
    private Button makeButton(String name, EventHandler<ActionEvent> handler) {
        Button b = new Button();
        b.setText(myButtons.getString(name));
        b.setMaxSize(100,20);
        b.setId(name);
        b.setOnAction(handler);
        return b;
    }

    /**
     * Creates a drop down menu that contains all possible simulations that a user can select/run
     * @return result -- HBox
     */
    private Node makeConfigurationsMenu() {
        topBar = new HBox();
        topBar.setSpacing(20);
        mySimulations = new ComboBox<>();
        mySimulations.setId("SimulationsMenu");
        mySimulations.setPromptText(myButtons.getString("SimulationsMenu"));
        topBar.getChildren().add(mySimulations);
        addSimulationTypes();
        return topBar;
    }

    private void addSimulationTypes() {
        Map<String, List<String>> simMap = simulationModel.getSimulationsMap();
        for (String k: simMap.keySet()) {
            mySimulations.getItems().add(k);
        }
        myConfigurations = new ComboBox();
        myConfigurations.setId("ConfigurationsMenu");
        topBar.getChildren().add(myConfigurations);
        mySimulations.valueProperty().addListener((observableValue, oldValue, selected) -> addConfigsBox(simMap.get((String) selected)));
    }

    private void addConfigsBox(List<String> configs) {
        myConfigurations.setPromptText(myButtons.getString("ConfigurationsMenu"));
        List<Object> list = new ArrayList<>(myConfigurations.getItems());
        myConfigurations.getItems().removeAll(list);
        for (String s: configs) {
            myConfigurations.getItems().add(s);
        }
        myConfigurations.valueProperty().addListener((observableValue, oldValue, selected) -> {if (((String) selected)!=null) {loadNewConfig((String) selected);}});
    }

    /**
     * Loads a new configuration based on the file selected by the user.
     * @param file
     */
    public void loadNewConfig(String file) {
        myScreen.setBottom(addButtons());
        gridPane.getChildren().clear();
        myAnimation.pause();
        myGrid = simulationModel.initSimulation(file);
        handleGridSetUp(500,500);
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
            simulationModel.writeConfig(userInput.get(0), userInput);
        }
    }

    /**
     * Helper function to make a text box.
     */
    private TextField makeTextBox(String str, int colIdx, int rowIdx, GridPane gp) {
        TextField tf = new TextField();
        gp.add(new Label(str), colIdx, rowIdx);
        gp.add(tf, colIdx + 1, rowIdx);
        return tf;
    }
}
