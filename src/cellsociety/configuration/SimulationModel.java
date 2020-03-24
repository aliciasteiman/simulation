package cellsociety.configuration;

import cellsociety.*;
import cellsociety.simulation.*;
import javafx.scene.paint.ImagePattern;

import java.io.File;
import java.util.*;

/**
 * I chose this class as my masterpiece because I think it effectively demonstrates the "model"
 * part of the MVC structure, as well as exhibiting important design principles of good communication,
 * modularity/encapsulation, and polymorphism.
 *
 * Firstly, this class, as its name suggests, serves as the model for the SimulationView, holding all the
 * data that the SimulationView would need in order to run a simulation. It has no dependencies on the
 * SimulationView,meaning it independently controls information relating to deciding which simulation
 * should be run,controlling the changes to a simulation, and either reading a configuration
 * or writing a configuration.This SimulationModel essentially serves as a middle piece between
 * the SimulationView and the back-end components of the Configuration classes and Simulation classes.
 * Only the SimulationModel interfaces directly with these configuration components
 * (ex. CSVConfiguration, PropertiesConfiguration) and simulation components (ex. GameOfLife,Percolation,etc.).
 * Therefore, the SimulationView never has to interact with a Configuration or Simulation object -
 * the SimulationView ONLY has to create a SimulationModel object and the details of configuration/simulation
 * rules remain hidden to the view.
 *
 * Communication - All pubic methods are well-commented to reveal their use cases/assumptions; methods and
 * variables are well-named to avoid confusion and clearly indicate their purpose.
 *
 * Modularity/Encapsulation - all instance field variables are private, helper methods (ex. determineSimulation)
 * that don't need to be seen by other classes are also private. The SimulationModel has a clear purpose,
 * and calls on other classes to serve purposes that are different (tell, dont ask principles). For instance,
 * it calls the readConfigFromFile() method on the CSVConfiguration object, rather than overextending its job
 * and trying to do this action as well.
 *
 * Polymorphism - In the determineSimulation method, the principle of turning switch statements into polymorphism
 * is demonstrated. Based on the name of the simulation (the parameter passed in), a different kind of
 * Simulation object is created. This means the SimulationView can run ANY kind of simulation - it just
 * has to be added as another case statement to this block.
 *
 */

/**
 * This class is responsible for holding all the back-end information about the simulation
 * for the view. It is responsible for initializing and maintaining all the necessary
 * components to make a simulation run, as well as communicating changes about the Grid
 * and configuration to the view.
 *
 * @author: Jessica
 */
public class SimulationModel {

    private Simulation currSimulation;
    private CSVConfiguration csvConfiguration;
    private String myPropertiesFile;
    private PropertiesConfiguration propertiesConfiguration;
    private final String SIMULATION_FILE = "Simulations";

    public SimulationModel() { }

    /**
     * PURPOSE: This method is responsible for providing data about all the possible simulations and their
     * corresponding configurations to the view.
     * ASSUMPTIONS: any information about the simulations and their configurations should be in the
     * Simulation.Properties file.
     * @return - a map of simulations (keys) and configurations (values) that the SimulationView will
     * use to populate its drop down menus.
     */
    public Map<String, List<String>> getSimulationsAndConfigurationsMap() throws ResourceKeyException {
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
    public Grid initializeSimulationGrid(String file) throws ResourceKeyException {
        createSimulation(file);
        csvConfiguration = new CSVConfiguration(currSimulation);
        String f = propertiesConfiguration.getConfigFile();
        Grid simulationGrid = createInitialGridConfig(f);
        currSimulation.setGrid(simulationGrid);
        return simulationGrid;
    }

    //creates the Simulation by reading the SimulationName from the PropertiesFile
    //and calling the helper method determineSimulation
    private void createSimulation(String file) {
        propertiesConfiguration = new PropertiesConfiguration(file);
        myPropertiesFile = file;
        String simName = propertiesConfiguration.getSimulationFromFile();
        determineSimulation(simName);
    }

    //based on the simulation name read from the properties file, this method
    //determines the Simulation that the user wants to be run.
    private void determineSimulation(String simName) {
        List<String> simStates = propertiesConfiguration.getStatesFromFile();
        List<String> stateReps = propertiesConfiguration.getStatesRepsFromFile();
        List<String> stateCSS = propertiesConfiguration.getStatesCSSFromFile();

        switch (simName) {
            case "GameOfLife":
                currSimulation = new GameOfLife(simStates, stateReps, stateCSS);
                break;
            case "Percolation":
                currSimulation = new Percolate(simStates, stateReps, stateCSS);
                break;
            case "Schelling's Model of Segregation":
                double threshold = (double) new SegregationPropertiesConfiguration(myPropertiesFile).getSpecialVals().get("SatisfiedThreshold");
                currSimulation = new Segregation(simStates, stateReps, stateCSS, threshold);
                break;
            case "Spreading of Fire":
                double prob = (double) new SOFPropertiesConfiguration(myPropertiesFile).getSpecialVals().get("ProbabilityCatch");
                currSimulation = new SpreadingOfFire(simStates, stateReps, stateCSS, prob);
                break;
            case "Rock Paper Scissors":
                int RPSthreshold = (int) new RPSPropertiesConfiguration(myPropertiesFile).getSpecialVals().get("Threshold");
                currSimulation = new RPS(simStates, stateReps, stateCSS, RPSthreshold);
                break;
            case "WaTor":
                WaTorPropertiesConfiguration waTorConfig = new WaTorPropertiesConfiguration(myPropertiesFile);
                int fishRepTimer = (int) waTorConfig.getSpecialVals().get("FishReproductionTimer");
                int sharkRepTimer = (int) waTorConfig.getSpecialVals().get("SharkReproductionTimer");
                int sharkEatingGain = (int) waTorConfig.getSpecialVals().get("SharkEatingGain");
                int sharkInitEnergy = (int) waTorConfig.getSpecialVals().get("SharkInitEnergy");
                currSimulation = new WaTor(simStates, stateReps, stateCSS, fishRepTimer, sharkRepTimer, sharkEatingGain, sharkInitEnergy);
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
    public List<String> getSimulationStates() {
        return currSimulation.getMyStates();
    }

    /**
     * PURPOSE: Updates the Grid. Called by SimulationView and prevents the SimulationView
     * from having to access a Simulation object.
     * @return Grid - the updated Grid object after the rules for a specific
     * simulation have been applied ONCE.
     */
    public Grid updateGrid() {
        return currSimulation.updateCells();
    }

    /**
     * PURPOSE: Updates each cell in the Grid. Called by SimulationView and encapsulates
     * the Simulation object. Cell appearance can be overridden by user-chosen images/colors.
     * ASSUMPTIONS: the Properties file read contains all the CSS styles for the simulation,
     * images and overriddenColors are NOT empty.
     * @param c - Cell object whose style needs to be updated
     */
    public void updateCell(Map<String, ImagePattern> images, Map<String, String> overriddenColors, Cell c) {
        currSimulation.updateCellStyle(images, overriddenColors, c); }

    /**
     * PURPOSE: Initializes the starting Grid based on a CSV file. Prevents the SimulationView
     * from having to access the a CSVConfiguration object.
     * ASSUMPTIONS: CSV file f is not empty, Simulation type has already been determined.
     * @param f - CSV File from which the initial config will be read.
     * @return - the Grid read from the file
     */
    public Grid createInitialGridConfig(String f) {
        return csvConfiguration.readConfigFromFile(f);
    }

    /**
     * PURPOSE: Writes the current configuration to a CSV and a properties file.
     * ASSUMPTIONS: userInput is NOT empty.
     * @param name - desired name of file
     * @param userInput - list of items to be added to properties file.
     */
    public void writeCurrentGridConfig(String name, List<String> userInput) {
        File f = csvConfiguration.saveCurrentConfig(name);
        propertiesConfiguration.makePropertiesFile(f, userInput);
    }
}
