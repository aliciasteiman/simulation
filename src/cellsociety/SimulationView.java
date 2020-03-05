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
    private SimulationModel simulationModel;
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
    private Button speedUpButton;
    private int BUTTON_SPACING;
    private ComboBox myConfigurations;
    private BorderPane root;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String buttonNamesFile = "ButtonNames";
    public static final String CELL_STYLESHEET = "resources/style.css";

    public final int FRAMES_PER_SECOND = 60;
    public double SECOND_DELAY = 20.0;
    public double SPEED = SECOND_DELAY / FRAMES_PER_SECOND;

    /**
     * Constructs the view of a given SimulationModel
     * @param model -- e.g. GameOfLife
     */
    public SimulationView(SimulationModel model) {
        simulationModel = model;
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + buttonNamesFile);
    }

    /**
     * Returns scene for the simulation so it can be added to the stage in Main
     * @param width
     * @param height
     * @return scene
     */
    public Scene makeScene(int width, int height) {
        root = new BorderPane();
        BUTTON_SPACING = width/15;
        handleRootSetUp();
        myScene = new Scene(root, width, height);
        myScene.getStylesheets().add(CELL_STYLESHEET);

        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);

        //myGrid = simulationModel.initSimulation("SoF_Test");
        myGrid = simulationModel.initSimulation("MoS_Test");
        handleGridSetUp(width, height);
        return myScene;
    }

    private void handleRootSetUp() {
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        root.setCenter(pane);
        root.setBottom(addButtons());
        root.setTop(makeConfigurationsMenu());
    }

    private void handleGridSetUp(int width, int height) {
        myCols = myGrid.getCols();
        myRows = myGrid.getRows();
        CELL_HEIGHT = (height - 100) / myCols;
        CELL_WIDTH = width/ myRows;
        updateGridAppearance();
    }

    /**
     * Updates the color of a cell based on its new status (i.e. alive vs. dead)
     * @param row - row position of the cell
     * @param col - column position of the cell
     */
    private void updateCellAppearance(int row, int col) {
        Cell c = myGrid.getCell(row, col);
        c.setShape(new Rectangle(CELL_WIDTH, CELL_HEIGHT));
        c.getShape().setId("cell" + row + col);
        //KeyFrame userClickFrame = new KeyFrame(Duration.seconds(SPEED), e -> allowUserClick(c));
        //myAnimation.getKeyFrames().add(userClickFrame);
        //myAnimation.play();
        simulationModel.updateCell(c);
        pane.add(c.getShape(), col, row);
    }

//    private void allowUserClick(Cell c) {
//        EventHandler<MouseEvent> userClick = new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent e) {
//                c.setStatus(! c.getStatus());
//                myModel.updateCellStyle(c);
//            }
//        };
//        c.getShape().addEventFilter(MouseEvent.MOUSE_CLICKED, userClick);
//    }

    /**
     * Loops through through each cell in the grid and updates the individual cell using the helper method
     */
    public void updateGridAppearance() {
        for (int row = 0; row < myGrid.getRows(); row++) {
            for (int col = 0; col < myGrid.getCols(); col++) {
                updateCellAppearance(row, col);
            }
        }
    }

    /**
     * In each "step" of the simulation, the grid appearance is updated to reflect changes between
     * cell generations.
     */
    public void step() {
        myGrid = simulationModel.updateGrid();
        updateGridAppearance();
    }

    /**
     * Sets the animation for the simulation, where the step (updating the cells) occurs indefinitely
     */
    public void setAnimation() {
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
    public Node addButtons() {
        HBox userButtons = new HBox();
        userButtons.setAlignment(Pos.CENTER);
        userButtons.setSpacing(BUTTON_SPACING);

        startButton = makeButton("startCommand", e -> setAnimation());
        pauseButton = makeButton("pauseCommand", e -> myAnimation.pause());
        saveButton = makeButton("saveCommand", e -> simulationModel.writeConfig(myGrid));
        stepButton = makeButton("stepCommand", e -> step());
        //speedUpButton = makeButton("speedUpCommand", e -> changeSpeed(-5));

        userButtons.getChildren().add(startButton);
        userButtons.getChildren().add(pauseButton);
        userButtons.getChildren().add(saveButton);
        userButtons.getChildren().add(stepButton);
        //userButtons.getChildren().add(speedUpButton);

        return userButtons;
    }

//    private void changeSpeed(int speed) {
//        SECOND_DELAY += speed;
//        setAnimation();
//    }

    /**
     * Helper method to create a button given an identifying name and setting an event to occur on action
     * e.g. start button "Start" starts the animation when clicked on
     * @param name -- identifier in the .properties file
     * @param handler -- action the button should perform
     * @return b -- Button
     */
    private Button makeButton(String name, EventHandler<ActionEvent> handler) {
        Button b = new Button();
        b.setText(myResources.getString(name));
        b.setMaxSize(60,20);
        b.setId(name);
        b.setOnAction(handler);
        return b;
    }

    /**
     * Creates a drop down menu that contains all possible simulations that a user can select/run
     * @return result -- HBox
     */
    private Node makeConfigurationsMenu() {
        HBox result = new HBox();
        myConfigurations = new ComboBox<>();
        myConfigurations.setId("ConfigurationsMenu");
        myConfigurations.setPromptText(myResources.getString("ConfigurationsMenu"));
        result.getChildren().add(myConfigurations);

        myConfigurations.getItems().add("GoL_Blinker");
        myConfigurations.getItems().add("GoL_Block");
        myConfigurations.getItems().add("GoL_Glider");
        myConfigurations.getItems().add("GoL_MWSS");
        myConfigurations.getItems().add("GoL_Pulsar");
        myConfigurations.valueProperty().addListener((observableValue, oldValue, selected) -> loadNewConfig((String) selected));
        return result;
    }

    private void loadNewConfig(String file) {
        pane.getChildren().clear();
        myAnimation.pause();
        //SimulationModel newModel = new GameOfLife(file);
        //myModel = newModel;
        myGrid = simulationModel.initSimulation(file);
        handleGridSetUp(500,500);
    }
}
