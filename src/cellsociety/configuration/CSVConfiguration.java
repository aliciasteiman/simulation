package cellsociety.configuration;

import cellsociety.Grid;
import cellsociety.simulation.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVConfiguration  {
    private Scanner input;
    private String file;

    public CSVConfiguration(String f) {
        file = f;
        input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(f));
    }

    public Grid readConfigFromFile(Simulation sim) throws ConfigurationException{
        //Scanner input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(f));
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
                sim.setCellFromFile(row, col, ch, g);
            }
            row++;
        }
        if (row != numRows) {
            throw new ConfigurationException("Configuration file does not match given number of rows");
        }
        return g;
    }


    public File saveCurrentConfig(Simulation sim, Grid g, String name) {
        File writtenFile = new File("data/" + name + ".csv"); //needs to access user inputted file name
        FileWriter fr = null;
        try {
            fr = new FileWriter(writtenFile);
            fr.write(g.getRows() + "," + g.getCols() + "\n");
            for (int i = 0; i < g.getRows(); i++) {
                for (int j = 0; j < g.getCols(); j++) {
                    sim.writeCellToFile(fr, i, j, g);
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
