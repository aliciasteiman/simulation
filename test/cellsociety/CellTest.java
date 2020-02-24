package cellsociety;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    Cell cellDead = new Cell(false);
    Cell cellAlive = new Cell(true);

    @Test
    void getStatus() {
        //test for Cell that is dead
        assertEquals(cellDead.getStatus(), false);
        //test for Cell that is alive
        assertEquals(cellAlive.getStatus(), true);
    }

    @Test
    void setStatus() {
        //setting the dead cell to be alive
        cellDead.setStatus(true);
        assertEquals(cellDead.getStatus(), true);
        //setting the alive cell to be dead
        cellAlive.setStatus(false);
        assertEquals(cellAlive.getStatus(), false);
    }

    @Test
    void setShape() {
        //test for Cell that is Rectangular shape
        cellDead.setShape(new Rectangle(100,100));
        assertTrue(cellDead.getShape() instanceof javafx.scene.shape.Rectangle);
    }

    @Test
    void getShape() {
        //test for Cell that is Circular shape
        cellDead.setShape(new Circle(100));
        assertTrue(cellDead.getShape() instanceof javafx.scene.shape.Circle);
    }
}