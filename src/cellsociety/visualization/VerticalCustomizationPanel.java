package cellsociety.visualization;

import cellsociety.configuration.SimulationModel;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

public class VerticalCustomizationPanel {
    protected SimulationModel simulationModel;
    protected VBox customizePanel;

    public VerticalCustomizationPanel(SimulationModel simModel) {
        simulationModel = simModel;
    }

    public void makeCustomizePanel(int width, int height) {
        customizePanel = new VBox();
        customizePanel.setPrefWidth(width/5);
        customizePanel.setPadding(new Insets(10,10,10,0));
    }

    public double getWidth() {return customizePanel.getPrefWidth();}

    public VBox setUpCustomBoxOnPanel(int height) {
        VBox customBox = new VBox();
        customBox.setPrefHeight(height/5);
        customBox.setPadding(new Insets(10,10,10,10));
        customBox.setSpacing(10);
        customizePanel.getChildren().add(customBox);
        return customBox;
    }

    public void addLabelToBox(VBox customBox, String labelName) {
        Label customLabel = new Label(labelName);
        customBox.getChildren().add(customLabel);
    }

    public ComboBox generateComboBox(String state, List<String> values) {
        ComboBox box = new ComboBox();
        box.setPromptText(state);
        box.getItems().addAll(values);
        return box;
    }

    public void clearCustomBox(VBox customBox) {
        customBox.getChildren().clear();
    }
}
