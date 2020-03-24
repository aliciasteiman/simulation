package cellsociety.visualization;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

import java.util.ResourceBundle;

public class OperationsPanel extends HorizontalPanel {
    private Button startButton;
    private Button saveButton;
    private Button pauseButton;
    private Button stepButton;
    private Button speedUpButton;
    private Button slowDownButton;

    public OperationsPanel(ResourceBundle buttonNames) {
        super(buttonNames);
    }
    /**
     * Initializes an HBox of buttons and sets their alignment
     * Initializes a start, pause, save, and step button and adds them to the HBox (root)
     * @return userButtons -- HBox of buttons
     */
    public Node addButtons(double height, int BUTTON_SPACING, EventHandler<ActionEvent> setAnimation, EventHandler<ActionEvent> pauseAnimation, EventHandler<ActionEvent> saveConfig, EventHandler<ActionEvent> step, EventHandler<ActionEvent> speedUp, EventHandler<ActionEvent> slowDown) {
        positionPanel(height);

        startButton = makeButton("startCommand", setAnimation);
        pauseButton = makeButton("pauseCommand", pauseAnimation);
        saveButton = makeButton("saveCommand", saveConfig);
        stepButton = makeButton("stepCommand", step);
        speedUpButton = makeButton("speedUpCommand", speedUp);
        slowDownButton = makeButton("slowDownCommand", slowDown);

        myPanel.getChildren().add(startButton);
        myPanel.getChildren().add(pauseButton);
        myPanel.getChildren().add(saveButton);
        myPanel.getChildren().add(stepButton);
        myPanel.getChildren().add(speedUpButton);
        myPanel.getChildren().add(slowDownButton);
        return myPanel;
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
        b.setText(elementNames.getString(name));
        b.setMaxSize(100,20);
        b.setId(name);
        b.setOnAction(handler);
        return b;
    }
}
