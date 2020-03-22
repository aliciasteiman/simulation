package cellsociety.configuration;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesConfigurationTest {
    //testing that a properties file is made and the keys match the inputs entered.
    @Test
    void testMakePropertiesFile() {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration("GoL_Blinker");
        List<String> input = List.of("TestConfigurationsWrite", "Jessica", "This file is for testing the make properties function.");
        File f = new File("testProperties.csv");
        propertiesConfiguration.makePropertiesFile(f,input);
        ResourceBundle myResources = ResourceBundle.getBundle("resources." + input.get(0));
        assertEquals("TestConfigurationsWrite", myResources.getString("Title"));
        assertEquals("Jessica", myResources.getString("Author"));
        assertEquals("This file is for testing the make properties function.", myResources.getString("Description"));
    }

}