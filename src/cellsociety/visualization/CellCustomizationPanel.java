package cellsociety.visualization;

import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import cellsociety.configuration.SimulationModel;
import javafx.scene.paint.ImagePattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CellCustomizationPanel extends VerticalCustomizationPanel{

    private VBox colorChooser;
    private VBox imageChooser;
    private Map<String, String> overriddenColors;
    private Map<String, ImagePattern> overriddenImages;

    public static final List<String> imageOptions = List.of("angel", "blocked", "fish", "ocean", "shark", "skull", "water",
            "fire", "tree", "scissor", "rock", "paper", "dust");
    public static final List<String> colorOptions = List.of("red", "orange", "yellow", "green", "blue", "purple");

    public CellCustomizationPanel(SimulationModel simModel) {
        super(simModel);
    }

    public VBox getCustomizePanel(int width, int height) {
        makeCustomizePanel(width, height);
//        customizePanel = new VBox();
//        customizePanel.setPrefWidth(width/5);
//        customizePanel.setPadding(new Insets(10,10,10,0));

        colorChooser = setUpCustomBoxOnPanel(height); //new VBox();
        imageChooser = setUpCustomBoxOnPanel(height);//new VBox();
//        setUpCustomPanel(height, colorChooser);
//        setUpCustomPanel(height, imageChooser);
        return customizePanel;
    }


    public void colorAndImageOptions(GridView gridView) {
        overriddenColors = new HashMap<>();
        overriddenImages = new HashMap<>();
        customizePanel.getStyleClass().add("panel-border");
        addLabelToBox(colorChooser, "COLOR");
        addLabelToBox(imageChooser, "IMAGE");
        List<String> states = simulationModel.getSimulationStates();
        for (String s: states) {
            generateComboBoxForColor(gridView, s);
            generateComboBoxForImage(gridView, s);
        }
    }

    private void generateComboBoxForImage(GridView gridView, String state) {
        ComboBox imageBox = generateComboBox(state, imageOptions);
        imageBox.valueProperty().addListener((observableValue, oldValue, selected) ->
        {if ((String) selected != null)
        {
            String imgName = (String) selected+".png";
            Image im = new Image(this.getClass().getClassLoader().getResourceAsStream("Images/"+imgName));
            overriddenImages.put(state, new ImagePattern(im));
            gridView.changeCellImages(overriddenImages, state);
        }
        });
        imageChooser.getChildren().add(imageBox);
    }

    private void generateComboBoxForColor(GridView gridView, String state) {
        ComboBox colorBox = generateComboBox(state, colorOptions);
        colorBox.valueProperty().addListener((observableValue, oldValue, selected) ->
        {if ((String) selected != null)
        {
            overriddenColors.put(state, (String) selected);
            gridView.changeCellColors(state, (String) selected);
        }
        });
        colorChooser.getChildren().add(colorBox);
    }

    public Map<String, String> getOverriddenColors() {
        return overriddenColors;
    }

    public Map<String, ImagePattern> getOverriddenImages() {
        return overriddenImages;
    }

    public void clearComboBoxes() {
        clearCustomBox(colorChooser);
        clearCustomBox(imageChooser);
    }


}
