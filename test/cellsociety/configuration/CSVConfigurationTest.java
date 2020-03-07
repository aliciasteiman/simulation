package cellsociety.configuration;

import cellsociety.Grid;
import cellsociety.configuration.CSVConfiguration;
import cellsociety.configuration.SimulationModel;
import cellsociety.simulation.GameOfLife;
import cellsociety.simulation.Simulation;
import org.junit.jupiter.api.Test;
import org.testfx.assertions.api.Assertions;
import java.io.File;
import java.util.List;


class CSVConfigurationTest {

    SimulationModel myModel = new SimulationModel();
    private List<String> states = List.of("dead", "alive");
    private List<String> stateReps = List.of("0","1");
    private List<String> stateCSS = List.of("GOL-dead-cell","GOL-alive-cell");
    Simulation gameOfLife = new GameOfLife(states, stateReps, stateCSS);
    CSVConfiguration csvConfig = new CSVConfiguration(gameOfLife);

    //testing that the configuration is saved properly to a new.csv File.
    @Test
    void testSaveConfig() {
        Grid g = myModel.initSimulation("GoL_Block");
        gameOfLife.setGrid(g);
        File f = csvConfig.saveCurrentConfig("new");
        File blockFile = new File("data/GOLconfigurations/blockConfig.csv");
        Assertions.assertThat(blockFile).hasSameContentAs(f);
    }

}