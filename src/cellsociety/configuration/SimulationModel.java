package cellsociety.configuration;

import cellsociety.*;
import cellsociety.simulation.*;

import java.io.File;
import java.util.*;

/**
 * This class is responsible for holding all the back-end information about the simulation
 * for the view. It determines the initial configuration, the Simulation type, and communicates changes
 * about Grid updates to SimulationView.
 */
public class SimulationModel {

    private Simulation mySimulation;
    private CSVConfiguration csvConfig;
    private ResourceBundle myResources;
    private ResourceBundle mySimulationResources;
    private Map<String, List<String>> simulationsMap;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String SIMULATION_FILE = "Simulations";

    public SimulationModel() {
        simulationsMap = new HashMap<>();
        mySimulationResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + SIMULATION_FILE);
    }

    /**
     * @return - a map of simulations (keys) and configurations (values) that the SimulationView will
     * use to populate its drop down menus.
     */
    public Map<String, List<String>> getSimulationsMap() {
        List<String> sims = Arrays.asList(mySimulationResources.getString("Simulations").split(","));
        for (String s :sims) {
            simulationsMap.putIfAbsent(s, new ArrayList<String>());
            simulationsMap.put(s, Arrays.asList(mySimulationResources.getString(s).split(",")));
        }
        return simulationsMap;
    }

    /**
     * Initializes a simulation from a file.
     */
    public Grid initSimulation(String file) {
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + file);
        String simName = myResources.getString("SimulationType");
        determineSimulation(simName);
        String f = determineFileType();
        csvConfig = new CSVConfiguration(mySimulation);
        Grid newGrid = initializeConfig(f, mySimulation);
        mySimulation.setGrid(newGrid);
        return newGrid;
    }

    private void determineSimulation(String simName) {
        List<String> simStates = SimulationStatesLists().get(1);
        List<String> stateReps = SimulationStatesLists().get(0);
        List<String> stateCSS = SimulationStatesLists().get(2);
        if (simName.equals("GameOfLife")) {
            mySimulation = new GameOfLife(simStates, stateReps, stateCSS);
        }
        if (simName.equals("Percolation")) {
            mySimulation = new Percolate(simStates, stateReps, stateCSS);
        }
        if (simName.equals("Schelling's Model of Segregation")) {
            double threshold = Double.parseDouble(myResources.getString("SatisfiedThreshold"));
            mySimulation = new Segregation(simStates,stateReps, stateCSS, threshold);
        }

        if (simName.equals("Spreading of Fire")){
            double prob = Double.parseDouble(myResources.getString("ProbabilityCatch"));

            mySimulation = new SpreadingOfFire(simStates,stateReps, stateCSS,prob);
        }
        if (simName.equals("Rock Paper Scissors")) {
            int RPSthreshold = Integer.parseInt(myResources.getString("Threshold"));
            mySimulation = new RPS(simStates,stateReps,stateCSS,RPSthreshold);
        }

        if (simName.equals("WaTor")) {
            int fishRepTimer = Integer.parseInt(myResources.getString("FishReproductionTimer"));
            int sharkRepTimer = Integer.parseInt(myResources.getString("SharkReproductionTimer"));
            int sharkEatingGain = Integer.parseInt(myResources.getString("SharkEatingGain"));
            int sharkInitEnergy = Integer.parseInt(myResources.getString("SharkInitEnergy"));
            mySimulation = new WaTor(simStates, stateReps, stateCSS, fishRepTimer, sharkRepTimer, sharkEatingGain, sharkInitEnergy);
        }
    }

    private String determineFileType() {
        String myFile = myResources.getString("FileName");
        return myFile;
    }

    /**
     * @return a List of Lists, with each List representing an aspect of a Simulation. The first list
     * is the state representations (ex. 0,1,2), the second list is the states (ex. blocked, empty, etc),
     * and the third list is the CSS styles corresponding to each state.
     */
    public List<List<String>> SimulationStatesLists() {
        List<List<String>> ret = new ArrayList<>();
        List<String> stateReps = Arrays.asList(myResources.getString("StateRepresentations").split(","));
        List<String> states = Arrays.asList(myResources.getString("States").split(","));
        List<String> statesCSS = Arrays.asList(myResources.getString("StatesCSS").split(","));
        ret.add(stateReps);
        ret.add(states);
        ret.add(statesCSS);
        return ret;
    }

    /**
     * Updates the Grid. Called by SimulationView.
     */
    public Grid updateGrid() {
        return mySimulation.updateCells();
    }

    /**
     * Updates each cell in the Grid. Called by SimulationView.
     * @param c
     */
    public void updateCell(Cell c) {
        mySimulation.updateCellStyle(c);
    }

    /**
     * Initializes the starting Grid for the simulation.
     * @param f - File from which the initial config will be read.
     * @param sim - the Simulation corresponding to the Grid
     * @return - the Grid read from the file
     */
    public Grid initializeConfig(String f, Simulation sim) {
        return csvConfig.readConfigFromFile(f);
    }

    /**
     * Writes the configuration to a CSV and a properties file.
     * @param name - name of file
     * @param userInput - list of items to be added to properties file.
     */
    public void writeConfig(String name, List<String> userInput) {
         File f = csvConfig.saveCurrentConfig(name);
         new PropertiesConfiguration().makePropertiesFile(f, userInput);
    }
}
