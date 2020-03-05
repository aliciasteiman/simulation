package cellsociety;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CSVConfiguration  {
    private Scanner input;
    private String file;
    private File writtenFile;

    public CSVConfiguration(String f) {
        file = f;
        input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(f));
    }



    public Grid readConfigFromFile(Simulation sim) {
        //Scanner input = new Scanner(Grid.class.getClassLoader().getResourceAsStream(f));
        String[] header = input.nextLine().split(",");
        int num_rows = Integer.parseInt(header[0]);
        int num_cols = Integer.parseInt(header[1]);
        Grid g = new Grid(num_rows, num_cols);
        int row = 0;
        while (input.hasNextLine()) {
            String rowConfig = input.nextLine();
            String cells = rowConfig.replaceAll(",", "");
            for (int col = 0; col < num_cols; col++) {
                char ch = cells.charAt(col);
                sim.setCellFromFile(row, col, ch, g);
            }
            row++;
        }
        return g;
    }


    public File saveCurrentConfig(Simulation sim, Grid g) {
        writtenFile = new File("data/new.csv"); //needs to access user inputted file name
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
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
