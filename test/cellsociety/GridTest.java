package cellsociety;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    /**
     * Tests that the Grid constructor creates a Grid with the right number of rows and columns.
     * Also checks that all cells in the initialGrid are set to "false" = dead.
     */
    @Test
    void testInitialGrid() {
        Grid g = new Grid(5, 10); //initialize a grid with 5 rows and 10 columns
        assertEquals(10, g.getCols());
        assertEquals(5, g.getRows());
        for (int r = 0; r < g.getRows();r++) {
            for (int c = 0; c < g.getCols();c++) {
                assertFalse(g.getCell(r,c).getStatus());
            }
        }
    }

}