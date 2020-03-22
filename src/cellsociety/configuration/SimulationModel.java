package cellsociety.configuration;

import cellsociety.*;
import cellsociety.simulation.*;

import java.io.File;
import java.util.*;

/**
 * This class is responsible for holding all the back-end information about the simulation
 * for the view. It is responsible for initializing and maintaining all the necessary
 * components to make a simulation run, as well as communicating changes about the Grid
 * and configuration to the view.
 */
public class SimulationModel {

    private Simulation mySimulation;
    private CSVConfiguration csvConfig;
    private String myFile;
    private PropertiesConfiguration propertiesConfiguration;
    public static final String SIMULATION_FILE = "Simulations";

    public SimulationModel() { }

    /**
     * PURPOSE: This method is responsible for providing data about all the possible simulations and their
     * corresponding configurations to the view.
     * ASSUMPTIONS: any information about the simulations and their configurations should be in the
     * Simulation.Properties file.
     * @return - a map of simulations (keys) and configurations (values) that the SimulationView will
     * use to populate its drop down menus.
     */
    public Map<String, List<String>> getSimulationsMap() throws ResourceKeyException {
        PropertiesConfiguration simulationInfo = new PropertiesConfiguration(SIMULATION_FILE);
        return simulationInfo.makeSimulationsMap();
    }

    /**
     * PURPOSE: Initializes a simulation from a Properties file. In order to initialize a simulation,
     * this method determines what kind of simulation it is, reads the initial configuration
     * from a CSV file, and returns a Grid that represents this starting configuration.
     * ASSUMPTION: the keys for the SimulationType and File are present in the Properties file.
     * @return - Grid with starting states based on the CSV file. The View will use the
     * data contained in this Grid object to handle the appearance/style of the grid that the
     * user sees.
     */
    public Grid initSimulation(String file) throws ResourceKeyException {
        propertiesConfiguration = new PropertiesConfiguration(file);
        myFile = file;
        String simName = propertiesConfiguration.getSimulationFromFile();
        determineSimulation(simName);
        csvConfig = new CSVConfiguration(mySimulation);
        String f = propertiesConfiguration.getConfigFile();
        Grid newGrid = initializeConfig(f, mySimulation);
        mySimulation.setGrid(newGrid);
        return newGrid;
    }

    //based on the simulation name read from the properties file, this method
    //determines the Simulation that the user wants to be run.
    private void determineSimulation(String simName) {
        List<String> simStates = propertiesConfiguration.getStatesFromFile();
        List<String> stateReps = propertiesConfiguration.getStatesRepsFromFile();
        List<String> stateCSS = propertiesConfiguration.getStatesCSSFromFile();

        switch (simName) {
            case "GameOfLife":
                mySimulation = new GameOfLife(simStates, stateReps, stateCSS);
                break;
            case "Percolation":
                mySimulation = new Percolate(simStates, stateReps, stateCSS);
                break;
            case "Schelling's Model of Segregation":
                double threshold = (double) new SegregationPropertiesConfiguration(myFile).getSpecialVals().get("SatisfiedThreshold");
                mySimulation = new Segregation(simStates, stateReps, stateCSS, threshold);
                break;
            case "Spreading of Fire":
                double prob = (double) new SOFPropertiesConfiguration(myFile).getSpecialVals().get("ProbabilityCatch");
                mySimulation = new SpreadingOfFire(simStates, stateReps, stateCSS, prob);
                break;
            case "Rock Paper Scissors":
                int RPSthreshold = (int) new RPSPropertiesConfiguration(myFile).getSpecialVals().get("Threshold");
                mySimulation = new RPS(simStates, stateReps, stateCSS, RPSthreshold);
                break;
            case "WaTor":
                WaTorPropertiesConfiguration waTorConfig = new WaTorPropertiesConfiguration(myFile);
                int fishRepTimer = (int) waTorConfig.getSpecialVals().get("FishReproductionTimer");
                int sharkRepTimer = (int) waTorConfig.getSpecialVals().get("SharkReproductionTimer");
                int sharkEatingGain = (int) waTorConfig.getSpecialVals().get("SharkEatingGain");
                int sharkInitEnergy = (int) waTorConfig.getSpecialVals().get("SharkInitEnergy");
                mySimulation = new WaTor(simStates, stateReps, stateCSS, fishRepTimer, sharkRepTimer, sharkEatingGain, sharkInitEnergy);
                break;
        }

    }


    /**
     * PURPOSE: Responsible for giving the View the information about all the states for a
     * specific simulation.
     * ASSUMPTIONS: the getMyStates() method from the Simulation class
     * returns an unmodifiable list to make sure no other class can tamper with the info.
     * @return an UNMODIFIABLE List of Strings that represent the states for a simulation.
     */
    public List<String> returnSimulationStates() {
        return mySimulation.getMyStates();
    }

    /**
     * PURPOSE: Updates the Grid. Called by SimulationView and prevents the SimulationView
     * from having to access a Simulation object.
     * @return Grid - the updated Grid object after the rules for a specific
     * simulation have been applied ONCE.
     */
    public Grid updateGrid() {
        return mySimulation.updateCells();
    }

    /**
     * PURPOSE: Updates each cell in the Grid. Called by SimulationView and encapsulates
     * the Simulation object.
     * ASSUMPTIONS: the Properties file read contains all the CSS styles for the simulation.
     * @param c - Cell object whose style needs to be updated
     */
    public void updateCell(Cell c) {
        mySimulation.updateCellStyle(c);
    }

    /**
     * PURPOSE: Initializes the starting Grid based on a CSV file. Prevents the SimulationView
     * from having to access the a CSVConfiguration object.
     * ASSUMPTIONS: CSV file f is not empty, Simulation type has already been determined.
     * @param f - CSV File from which the initial config will be read.
     * @param sim - the Simulation that the user wants to run (ex. GoL, Percolation, etc.)
     * @return - the Grid read from the file
     */
    public Grid initializeConfig(String f, Simulation sim) {
        return csvConfig.readConfigFromFile(f);
    }

    /**
     * PURPOSE: Writes the current configuration to a CSV and a properties file.
     * ASSUMPTIONS: userInput is NOT empty.
     * @param name - desired name of file
     * @param userInput - list of items to be added to properties file.
     */
    public void writeConfig(String name, List<String> userInput) {
         File f = csvConfig.saveCurrentConfig(name);
         propertiesConfiguration.makePropertiesFile(f, userInput);
    }
}
