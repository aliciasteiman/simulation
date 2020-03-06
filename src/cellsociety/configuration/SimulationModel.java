package cellsociety.configuration;

import cellsociety.*;
import cellsociety.simulation.*;

import java.io.File;
import java.util.*;

public class SimulationModel {

    private Simulation mySimulation;
    private CSVConfiguration simConfig;
    private ResourceBundle myResources;
    private ResourceBundle mySimulationResources;
    private Map<String, List<String>> simulationsMap;
    private List<String> simulations;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String SIMULATION_FILE = "Simulations";

    public SimulationModel() {
        simulationsMap = new HashMap<>();
        mySimulationResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + SIMULATION_FILE);
    }

    public Map<String, List<String>> getSimulationsMap() {
        List<String> sims = Arrays.asList(mySimulationResources.getString("Simulations").split(","));
        for (String s :sims) {
            simulationsMap.putIfAbsent(s, new ArrayList<String>());
            simulationsMap.put(s, Arrays.asList(mySimulationResources.getString(s).split(",")));
        }
        return simulationsMap;
    }

    public Grid initSimulation(String file) {
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + file);
        String simName = myResources.getString("SimulationType");
        determineSimulation(simName);
        determineFileType();
        Grid newGrid = initializeConfig(mySimulation);
        mySimulation.setGrid(newGrid);
        return newGrid;
    }

    private void determineSimulation(String simName) {
        if (simName.equals("GameOfLife")) {
            mySimulation = new GameOfLife();
        }
        if (simName.equals("Percolation")) {
            mySimulation = new Percolate();
        }
        if (simName.equals("Schelling's Model of Segregation")) {
            double threshold = Double.parseDouble(myResources.getString("SatisfiedThreshold"));
            mySimulation = new Segregation(threshold);
        }
        if (simName.equals("Spreading of Fire")){
            double prob = Double.parseDouble(myResources.getString("ProbabilityCatch"));
            mySimulation = new SpreadingOfFire(prob);
        }
        if (simName.equals("Rock Paper Scissors")){
            int RPSthreshold = Integer.parseInt(myResources.getString("Threshold"));
            mySimulation = new RPS(RPSthreshold);
        }
        mySimulation.setStateReps(SimulationStatesLists().get(0));
        mySimulation.setMyStates(SimulationStatesLists().get(1));
        mySimulation.setStatesCSS(SimulationStatesLists().get(2));
    }

    private void determineFileType() {
        String myFile = myResources.getString("FileName");
        simConfig = new CSVConfiguration(myFile);
    }

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

    public Grid updateGrid() {
        return mySimulation.updateCells();
    }

    public void updateCell(Cell c) {
        mySimulation.updateCellStyle(c);
    }

    public Grid initializeConfig(Simulation sim) {
        return simConfig.readConfigFromFile(sim);
    }

    public File writeConfig(Grid g, String name) {
        return simConfig.saveCurrentConfig(mySimulation, g, name);
    }
}
