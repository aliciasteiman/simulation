package cellsociety.configuration;

import cellsociety.Grid;
import cellsociety.simulation.Simulation;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is responsible for reading CSV configurations to set up a grid's initial cell configuration and writing
 * a current configuration to a new file
 */
public class CSVConfiguration  {
    private Simulation mySimulation;

    public CSVConfiguration(Simulation sim) {
        mySimulation = sim;

    }

    /**
     * This method reads an initial grid configuration from a CSV file and communicates it to the simulation to set
     * up the initial cell statuses.
     * @param f - CSV file
     * @return - Grid based on the CSV configuration
     * @throws ConfigurationException
     */
    public Grid readConfigFromFile(String f) throws ConfigurationException{
        Scanner input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(f));
        String[] header = input.nextLine().split(",");
        int numRows = Integer.parseInt(header[0]);
        int numCols = Integer.parseInt(header[1]);
        Grid g = new Grid(numRows, numCols);
        int row = 0;
        while (input.hasNextLine()) {
            String rowConfig = input.nextLine();
            String cells = rowConfig.replaceAll(",", "");
            if (cells.length() != numCols) {
                throw new ConfigurationException("Configuration file does not match given number of columns");
            }
            for (int col = 0; col < numCols; col++) {
                char ch = cells.charAt(col);
                mySimulation.setCellFromFile(row, col, ch, g);
            }
            row++;
        }
        if (row != numRows) {
            throw new ConfigurationException("Configuration file does not match given number of rows");
        }
        return g;
    }

    /**
     * This method saves a current configuration to a csv file.
     * @param name - name of file
     * @return - file that is saved
     */
    public File saveCurrentConfig(String name) {
        Grid g = mySimulation.getGrid();
        File writtenFile = new File("data/" + name + ".csv"); //needs to access user inputted file name
        FileWriter fr = null;
        try {
            fr = new FileWriter(writtenFile);
            fr.write(g.getRows() + "," + g.getCols() + "\n");
            for (int i = 0; i < g.getRows(); i++) {
                for (int j = 0; j < g.getCols(); j++) {
                    mySimulation.writeCellToFile(fr, i, j, g);
                }
                fr.write("\n");
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace(); //need to change this... look at nanobrowser example
        }
        return writtenFile;
    }
}
