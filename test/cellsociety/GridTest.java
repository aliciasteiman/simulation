package cellsociety;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    Grid g = new Grid(5,10); //initialize a grid with 5 rows and 10 columns

    /**
     * Tests that the Grid constructor creates a Grid with the right number of rows and columns.
     * Also checks that all cells in the initialGrid are set to "false" = dead.
     */
    @Test
    void testInitialGrid() {
        assertEquals(10, g.getCols());
        assertEquals(5, g.getRows());
        for (int r = 0; r < g.getRows();r++) {
            for (int c = 0; c < g.getCols();c++) {
                assertTrue(g.getCell(r,c).getStatus().equals(""));
            }
        }
    }

    /**
     * Testing that the getCell() method returns the cell in the proper row/column.
     */
    @Test
    void testGetCell() {
        Cell c = g.getCell(4,5);
        assertEquals(4, c.getRow());
        assertEquals(5, c.getCol());
    }

    /**
     * Testing that the setCell() method updates the cell to what it was set to.
     */
    @Test
    void testSetCell() {
        Cell c = new Cell(2,3,"dead");
        g.setCell(c);
        assertEquals("dead", g.getCell(2,3).getStatus());

    }

}