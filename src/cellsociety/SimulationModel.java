package cellsociety;

import java.io.File;
import java.util.ResourceBundle;

public class SimulationModel {

    private Simulation mySimulation;
    private CSVConfiguration simConfig;
    private ResourceBundle myResources;
    private String myFile;
    public static final String RESOURCE_PACKAGE = "resources.";

    public SimulationModel() {
    }

    public Grid initSimulation(String file) {
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + file);
        String simName = myResources.getString("SimulationType");
        determineSimulation(simName);
        determineFileType();
        Grid newGrid = initializeConfig(myFile, mySimulation);
        mySimulation.setGrid(newGrid);
        return newGrid;
    }

    public void determineSimulation(String simName) {
        if (simName.equals("GameOfLife")) {
            mySimulation = new GameOfLife();
        }
        if (simName.equals("Percolation")) {
            mySimulation = new Percolate();
        }
        if (simName.equals("Schelling's Model of Segregation")) {
            mySimulation = new Segregation();
        }
        if (simName.equals("Spreading of Fire")){
            mySimulation = new SpreadingOfFire();
        }
        if (simName.equals("Rock Paper Scissors")){
            mySimulation = new RPS();
        }
    }

    public void determineFileType() {
        myFile = myResources.getString("FileName");
        simConfig = new CSVConfiguration();
    }

    public Grid updateGrid() {
        return mySimulation.updateCells();
    }

    public void updateCell(Cell c) {
        mySimulation.updateCellStyle(c);
    }

    public Grid initializeConfig(String f, Simulation sim) {
        return simConfig.readConfigFromFile(f, sim);
    }

    public File writeConfig(Grid g) {
        return simConfig.saveCurrentConfig(mySimulation, g);
    }
}
