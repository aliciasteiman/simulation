package cellsociety.visualization;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.ResourceBundle;


public class HorizontalPanel {
    protected HBox myPanel;
    protected ResourceBundle elementNames;

    public HorizontalPanel(ResourceBundle elmNames) {
        elementNames = elmNames;
        myPanel = new HBox();
    }

    public void positionPanel(double height) {
        myPanel.setPrefHeight(height/10);
        myPanel.setSpacing(20);
        myPanel.setAlignment(Pos.CENTER);
    }

    public double getHeight() {
        return myPanel.getPrefHeight();
    }
}
