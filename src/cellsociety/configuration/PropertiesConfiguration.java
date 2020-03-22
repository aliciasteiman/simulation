package cellsociety.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * This class is responsible for handling Properties files - extracting
 * the necessary information from them to run a simulation AND
 * writing all the aspects of a current configuration to a Properties file.
 */
public class PropertiesConfiguration {
    private ResourceBundle myResources;
    private String file;
    protected Map<String, String> allInfo;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String REGEX = ",";

    /**
     * Constructor for the PropertiesConfiguration object. Initializes all the
     * instance field variables needed to be used by this class, including
     * a ResourceBundle to read the Properties file, allInfo (HashMap) to store
     * the info from the file, and the properties file currently being read.
     * @param f - the String name of the Properties file
     */
    public PropertiesConfiguration(String f) {
        file = f;
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + f);
        allInfo = new HashMap<>();
        getAllInfoFromPropertiesFile();
    }

    /**
     * Responsible for returning a HashMap of all the "special values" needed for
     * a Simulation. These values can be things like the probability of catching fire
     * in the Spreading of Fire simulation, or the threshold for an RPS simulation.
     * @return - Map with keys as the name of the special value (ex. ProbabiltiyCatch) and
     * values as Numbers that repreesnt those values (ex. 0.80).
     */
    public Map<String, Number> getSpecialVals() {
        return new HashMap<>();
    }

    /**
     * Responsible for reading ALL the information from a Properties file
     * by putting the keys in the file as keys in a HashMap called allInfo
     * and values as the corresponding values from the file.
     */
    public void getAllInfoFromPropertiesFile() {
        for (String key: myResources.keySet()) {
            allInfo.put(key, myResources.getString(key));
        }
    }

    /**
     * @return a Map with keys as the names of the Simulations (ex. GoL, Percolation, etc.)
     * and values as a list of Configurations that correspond to each Simulation
     * (ex. GoL_Blinker, GoL_Pulsar, etc.for Game of Life Simulation).
     */
    public Map<String, List<String>> makeSimulationsMap() {
        Map<String, List<String>> simulationsMap = new HashMap<>();
        List<String> sims = null;
        try {
            sims = Arrays.asList(myResources.getString("Simulations").split(","));
        } catch (Exception e) {
            throw new ResourceKeyException("Key Simulations does not exist in resource file " + file + ".properties");
        }
        for (String s :sims) {
            simulationsMap.putIfAbsent(s, new ArrayList<String>());
            try {
                simulationsMap.put(s, Arrays.asList(myResources.getString(s).split(",")));
            } catch (Exception e) {
                throw new ResourceKeyException("Key " + s + " does not exist in resource file " + file + ".properties");
            }
        }
        return simulationsMap;
    }

    /**
     * @return the name of the Simulation the user is trying to run (ex. Game of Life,
     * Percolation, etc.)
     */
    public String getSimulationFromFile() {
        try {
            return allInfo.get("SimulationType");
        } catch (Exception e) {
            throw new ResourceKeyException("Key SimulationType does not exist in resource file" + file);
        }
    }

    /**
     * @return the name of the CSV file with the initial Grid configuration.
     */
    public String getConfigFile() {
        try {
            return allInfo.get("FileName");
        } catch (Exception e) {
            throw new ResourceKeyException("Key FileName does not exist in resource file" + file);
        }
    }

    /**
     * @return a List of Strings that represent all the states for a particular simulation.
     * For instance, if the Simulation is Game of Life, then the states would be Dead and Alive.
     */
    public List<String> getStatesFromFile() {
        try {
            return Arrays.asList(allInfo.get("States").split(REGEX));
        } catch (Exception e) {
            throw new ResourceKeyException("Key States does not exist in resource file" + file);
        }
    }

    /**
     * @return a List of Strings with all the CSS styles that correspond to the states
     * for a particular simulation.
     */
    public List<String> getStatesCSSFromFile() {
        try {
            return Arrays.asList(allInfo.get("StatesCSS").split(REGEX));
        } catch (Exception e) {
            throw new ResourceKeyException("Key StatesCSS does not exist in resource file" + file);
        }

    }

    /**
     * @return a List of Strings with all the representations (ex. 0,1,2, etc.) of
     * different states from the Properties file.
     */
    public List<String> getStatesRepsFromFile() {
        try {
            return Arrays.asList(allInfo.get("StateRepresentations").split(REGEX));
        } catch (Exception e) {
            throw new ResourceKeyException("Key StateRepresentations does not exist in resource file" + file);
        }
    }



    /**
     * This method makes the properties file with fields entered by the user when the
     * "save" button is hit by the user on the view.
     * @param info - the list of things that will be written to the Properties file
     */
    public void makePropertiesFile(File f,List<String> info) {
        try {
            String fileName = info.get(0);
            OutputStream output = new FileOutputStream(new File("src/resources/" + fileName + ".properties"));
            Properties prop = new Properties();
            prop.setProperty("Title", info.get(0));
            prop.setProperty("Author", info.get(1));
            prop.setProperty("Description", info.get(2));
            //File f = simulationModel.writeConfig(info.get(0));
            prop.setProperty("FileName", String.valueOf(f));
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace(); //obv fix this
        }
    }
}
