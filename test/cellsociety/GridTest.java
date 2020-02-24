package cellsociety;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void testInitialGrid() {
        Grid g = new Grid(5, 10); //initialize a grid with 5 rows and 10 columns
        assertEquals(10, g.getCols());
        assertEquals(5, g.getRows());
    }

}