# Simulation Lab Discussion
## Sanya Kocchar sk489, Alicia Steiman abs76, Jessica Su js803

### High Level Design Ideas

### CRC Card Classes

```java
 public class Cell {
     public Value getState ();
     public void updateState ();
     public int getPosition ();
     public List<Cell> getNeighbors ();
 }
```

```java
 public class Grid {
     public void setRules (SimulationBuilder sb);
     public void updateGrid ();
     public void endSimulation ();
 }
```

```java
 public abstract class SimulationBuilder {
     public void createSimulation (String file);
     public void updateSimulation ();
 }
```

```java
 public class SimulationViewer {
     public Scene makeScene ();
     public void updateScene ();
     public void makeUserSelection ();
     public void displayInformation ();
     public saveSimulation ();
 } 
```

### Use Cases

 * Apply the rules to a cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all of its neighbors)
 ```java
 Cell cell = new Cell();
 List<cell> neighbors = cell.getNeighbors();
 for (Cell cell : neighbors) {
    if (cell.getState() != dead) {
        cell.updateState();
    }
}
 ```

 * Move to the next generation: update all cells in a simulation from their current state to their next state
 ```java
 Grid grid = new Grid();
 grid.updateGrid();
 ```

 * Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one
 ```java
 SimulationBuilder sb = new SimulationBuilder();
 sb.createSimulation(String dataFile);
 ```
