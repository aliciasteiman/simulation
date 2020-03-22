package cellsociety.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * This class is responsible for handling Property files.
 */
public class PropertiesConfiguration {
    private ResourceBundle myResources;
    private String file;
    protected Map<String, String> allInfo;

    public static final String RESOURCE_PACKAGE = "resources.";
    public static final String REGEX = ",";

    public PropertiesConfiguration(String f) {
        file = f;
        myResources = ResourceBundle.getBundle(RESOURCE_PACKAGE + f);
        allInfo = new HashMap<>();
        getAllInfoFromPropertiesFile();
    }

    public Map<String, Number> getSpecialVals() {
        return new HashMap<>();
    }

    public void getAllInfoFromPropertiesFile() {
        for (String key: myResources.keySet()) {
            allInfo.put(key, myResources.getString(key));
        }
    }

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

    public String getSimulationFromFile() {
        try {
            return allInfo.get("SimulationType");
        } catch (Exception e) {
            throw new ResourceKeyException("Key SimulationType does not exist in resource file" + file);
        }
    }

    public String getConfigFile() {
        try {
            return allInfo.get("FileName");
        } catch (Exception e) {
            throw new ResourceKeyException("Key FileName does not exist in resource file" + file);
        }
    }

    public List<String> getStatesFromFile() {
        try {
            return Arrays.asList(allInfo.get("States").split(REGEX));
        } catch (Exception e) {
            throw new ResourceKeyException("Key States does not exist in resource file" + file);
        }
    }

    public List<String> getStatesCSSFromFile() {
        try {
            return Arrays.asList(allInfo.get("StatesCSS").split(REGEX));
        } catch (Exception e) {
            throw new ResourceKeyException("Key StatesCSS does not exist in resource file" + file);
        }

    }

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
