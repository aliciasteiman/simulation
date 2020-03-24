package cellsociety.visualization;

import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ChoicePanel extends HorizontalPanel{
    private ComboBox mySimulations;
    private ComboBox myConfigurations;

    public ChoicePanel(ResourceBundle comboBoxNames) {
        super(comboBoxNames);
    }
    /**
     * Creates a drop down menu that contains all possible simulations that a user can select/run
     * @return result -- HBox
     */
    public Node makeConfigurationsMenu(double height, Map<String, List<String>> simMap, ChangeListener<String> listener) {
        positionPanel(height);
        mySimulations = new ComboBox<>();
        mySimulations.setId("SimulationsMenu");
        mySimulations.setPromptText(elementNames.getString("SimulationsMenu"));
        myPanel.getChildren().add(mySimulations);
        addSimulationTypes(simMap, listener);
        return myPanel;
    }

    private void addSimulationTypes(Map<String, List<String>> simMap, ChangeListener<String> listener) {
        for (String k: simMap.keySet()) {
            mySimulations.getItems().add(k);
        }
        myConfigurations = new ComboBox();
        myConfigurations.setId("ConfigurationsMenu");
        myPanel.getChildren().add(myConfigurations);
        mySimulations.valueProperty().addListener((observableValue, oldValue, selected) -> addConfigsBox(simMap.get((String) selected), listener));
    }

    private void addConfigsBox(List<String> configs, ChangeListener<String> listener) {
        myConfigurations.setPromptText(elementNames.getString("ConfigurationsMenu"));
        List<Object> list = new ArrayList<>(myConfigurations.getItems());
        myConfigurations.getItems().removeAll(list);
        for (String s: configs) {
            myConfigurations.getItems().add(s);
        }
        myConfigurations.valueProperty().addListener(listener);
    }

}
