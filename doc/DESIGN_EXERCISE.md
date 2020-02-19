# Simulation Lab Discussion
## Sanya Kocchar sk489, Alicia Steiman abs76, Jessica Su js803

### High Level Design Ideas



### CRC Card Classes

This class's purpose or value is to manage something:
```java
 public class Something {
     public int getTotal (Collection<Integer> data)
     public Value getValue ()
 }
```

This class's purpose or value is to be useful:
```java
 public class Value {
     public void update (int data)
 }
```

### Use Cases

 * Apply the rules to a cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all of its neighbors)
 ```java
 Something thing = new Something();
 Value v = thing.getValue();
 v.update(13);
 ```

 * Move to the next generation: update all cells in a simulation from their current state to their next state
 ```java
 Something thing = new Something();
 Value v = thing.getValue();
 v.update(13);
 ```

 * Switch simulations: load a new simulation from a data file, replacing the current running simulation with the newly loaded one
 ```java
 Something thing = new Something();
 Value v = thing.getValue();
 v.update(13);
 ```
