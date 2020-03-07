package cellsociety.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

/**
 * This class is responsible for handling Property files.
 */
public class PropertiesConfiguration {
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
