package cellsociety;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    Cell cellDead = new Cell(false);
    Cell cellAlive = new Cell(true);

    /**
     * Testing the getStatus method for the Cell class.
     */
    @Test
    void testGetStatus() {
        //test for Cell that is dead
        assertEquals(cellDead.getStatus(), false);
        //test for Cell that is alive
        assertEquals(cellAlive.getStatus(), true);
    }

    /**
     * Testing the setStatus method for the Cell class.
     */
    @Test
    void testSetStatus() {
        //setting the dead cell to be alive
        cellDead.setStatus(true);
        assertEquals(cellDead.getStatus(), true);
        //setting the alive cell to be dead
        cellAlive.setStatus(false);
        assertEquals(cellAlive.getStatus(), false);
    }

    /**
     * Testing the setShape method for the Cell class.
     */
    @Test
    void testSetShape() {
        //test for Cell that is Rectangular shape
        cellDead.setShape(new Rectangle(100,100));
        assertTrue(cellDead.getShape() instanceof javafx.scene.shape.Rectangle);
    }

    /**
     * Testing the getShape method for Cell class.
     */
    @Test
    void testGetShape() {
        //test for Cell that is Circular shape
        cellDead.setShape(new Circle(100));
        assertTrue(cellDead.getShape() instanceof javafx.scene.shape.Circle);
    }
}